module GameMachine
  module GameSystems
    class TeamManager < Actor::Base
      include Models
      include Commands

      def post_init(*args)
        define_update_procs
      end

      def chat_channel(topic,flags)
        MessageLib::ChatChannel.new.set_name(topic).set_flags(flags)
      end

      def player_entity(player_id)
        MessageLib::Entity.new.set_player(MessageLib::Player.new.set_id(player_id))
      end

      def flags
        'subscribers'
      end

      def leave_chat(topic,player_id)
        leave_chat = MessageLib::LeaveChat.new.add_chat_channel(chat_channel(topic,flags))
        entity = player_entity(player_id).set_leave_chat(leave_chat)
        ChatManager.find.tell(entity)
      end

      def join_chat(topic,player_id)
        join_chat = MessageLib::JoinChat.new.add_chat_channel(chat_channel(topic,flags))
        entity = player_entity(player_id).set_join_chat(join_chat)
        ChatManager.find.tell(entity)
      end

      def send_team_joined(team_name,player_id)
        commands.player.send_message(TeamJoined.new(:name => team_name),player_id)
      end

      def send_team_left(team_name,player_id)
        commands.player.send_message(TeamLeft.new(:name => team_name),player_id)
      end

      def create_player_team(team_name,player_id)
        player_team = PlayerTeam.new(:id => player_id, :name => team_name)
        player_team.save!
      end

      def destroy_player_team(player_id)
        if player_team = PlayerTeam.find!(player_id)
          player_team.destroy!
        end
      end

      def team_id(team_name)
        Uniqueid.generate_token(team_name)
      end

      def create_team(message)
        if team = Team.find!(message.name)
          send_team_joined(team.name,message.player_id)
          return false
        end

        team = Team.new(
          :id => message.name,
          :team_id => team_id(message.name),
          :name => message.name,
          :owner => message.owner,
          :access => message.access,
          :members => [message.owner]
        )

        create_player_team(team.name,message.player_id)
        if team.access == 'private'
          team.invite_id = Uniqueid.generate_token(team.name)
        end

        team.save!
        commands.datastore.call_dbproc(:team_add_team,'teams',team.to_storage_entity,false)
        join_chat(team.name,team.owner)
        send_team_joined(team.name,message.player_id)
        GameMachine.logger.info "Team #{team.name} created"
      end

      def destroy_team(message)
        if team = Team.find!(message.name)
          if team.owner == message.player_id
            team.members.each do |member|
              leave_chat(message.name,member)
              destroy_player_team(member)
              send_team_left(team.name,member)
            end
            commands.datastore.call_dbproc(:team_remove_team,'teams',team.to_storage_entity,false)
            team.destroy!
            GameMachine.logger.info "Team #{team.name} destroyed"
          end
        end
        send_team_left(message.name,message.player_id)
      end

      def join_team(message,invite_accepted=false)
        if team = Team.find!(message.name)
          if team.access == 'public' || invite_accepted
            unless team.members.include?(message.player_id)
              team.members << message.player_id
              team.save!
              create_player_team(team.name,message.player_id)
              commands.datastore.call_dbproc(:team_update_team,'teams',team.to_storage_entity,false)
            end
            join_chat(team.name,message.player_id)
            send_team_joined(message.name,message.player_id)
          end
        end
      end

      def leave_team(message)
        if team = Team.find!(message.name)
          if message.player_id == team.owner
            destroy_team(
              DestroyTeam.new(
                :name => team.name,
                :player_id => message.player_id
              )
            )
          else
            team.members.delete_if {|member| member == message.player_id}
            team.save!
            commands.datastore.call_dbproc(:team_update_team,'teams',team.to_storage_entity,false)
            destroy_player_team(message.player_id)
            send_team_left(message.name,message.player_id)
            leave_chat(message.name,message.player_id)
          end
        else
          send_team_left(message.name,message.player_id)
        end
      end

      def team_invite(message)
        if team = Team.find!(message.name)
          if message.player_id == team.owner
            message.invite_id = team.invite_id
            commands.player.send_message(message,message.invitee)
          end
        end
      end

      def team_accept_invite(message)
        if team = Team.find!(message.name)
          if message.invite_id == team.invite_id
            join_team(JoinTeam.new(:name => team.name, :player_id => message.player_id),true)
          end
        end
      end

      def teams_request(message)
        if teams = Teams.find('teams')
          commands.player.send_message(teams,message.player_id)
        end
        if player_team = PlayerTeam.find!(message.player_id)
          if team = Team.find!(player_team.name)
            commands.player.send_message(team,message.player_id)
          end
        end
      end

      def on_receive(message)
        if message.is_a?(CreateTeam)
          create_team(message)
          teams_request(message)
        elsif message.is_a?(DestroyTeam)
          destroy_team(message)
        elsif message.is_a?(TeamInvite)
          team_invite(message)
        elsif message.is_a?(TeamAcceptInvite)
          team_accept_invite(message)
        elsif message.is_a?(JoinTeam)
          join_team(message)
          teams_request(message)
        elsif message.is_a?(LeaveTeam)
          leave_team(message)
        elsif message.is_a?(TeamsRequest)
          teams_request(message)
        end
      end

      def define_update_procs
        commands.datastore.define_dbproc(:team_update_team) do |id,current_entity,update_entity|
          if current_entity.nil?
            teams = Teams.new(:teams => [],:id => id)
          else
            teams = Teams.from_entity(current_entity,:json_storage)
          end

          team = Team.from_entity(update_entity,:json_storage)
          team_to_update = teams.teams.select {|t| t.name == team.name}.first
          if team_to_update
            teams.teams.delete_if {|t| t.name == team.name}
            teams.teams << team
          end
          teams.to_storage_entity
        end

        commands.datastore.define_dbproc(:team_add_team) do |id,current_entity,update_entity|
          if current_entity.nil?
            teams = Teams.new(:teams => [],:id => id)
          else
            teams = Teams.from_entity(current_entity,:json_storage)
          end

          team = Team.from_entity(update_entity,:json_storage)
          teams.teams.delete_if {|t| t.name == team.name}
          teams.teams << team
          teams.to_storage_entity
        end

        commands.datastore.define_dbproc(:team_remove_team) do |id,current_entity,update_entity|
          if current_entity.nil?
            teams = Teams.new(:teams => [],:id => id)
          else
            teams = Teams.from_entity(current_entity,:json_storage)
          end

          team = Team.from_entity(update_entity,:json_storage)
          teams.teams.delete_if {|t| t.name == team.name}
          teams.to_storage_entity
        end

      end
    end
  end
end
