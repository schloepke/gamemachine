module GameMachine
  module GameSystems
    class TeamManager < Actor::Base
      include Models
      include Commands

      attr_reader :team_handler
      def post_init(*args)
        if Application.config.team_handler
          @team_handler = Application.config.team_handler.constantize.new
        end
        define_update_procs
        commands.misc.client_manager_register(self.class.name)
      end

      def self.match_id_for(team_names)
        team_names.sort.join('_')
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

      def send_team(message)
        if player_team = PlayerTeam.find!(message.player_id)
          if team = Team.find!(player_team.name)
            commands.player.send_message(team,message.player_id)
          end
        end
      end

      def teams_request(message)
        if teams = Teams.find('teams')
          commands.player.send_message(teams,message.player_id)
        end
        send_team(message)
      end

      def destroy_player_team(player_id)
        if player_team = PlayerTeam.find!(player_id)
          player_team.destroy!
        end
      end

      def team_id(team_name)
        Uniqueid.generate_token(team_name)
      end

      def default_max_members
        100
      end

      def can_add_member?(team_name,player_id)
        return true unless team_handler
        team_handler.can_add_member?(team_name, player_id)
      end

      def can_create_team?(team_name,player_id)
        return true unless team_handler
        team_handler.can_create_team?(team_name, player_id)
      end

      def handler_update_teams
        return true unless team_handler
        team_handler.update_teams
      end

      def destroy_on_owner_leave?
        return true unless team_handler
        team_handler.destroy_on_owner_leave?
      end

      def handler_find_match(team_name)
        return true unless team_handler
        team_handler.match!(team_name)
      end

      def handler_match_started(match)
        return true unless team_handler
        team_handler.match_started(match)
      end

      def create_team(message)
        unless can_create_team?(message.name,message.player_id)
          return false
        end

        if team = Team.find!(message.name)
          send_team_joined(team.name,message.player_id)
          return false
        end

        message.max_members ||= default_max_members

        team = Team.new(
          :id => message.name,
          :team_id => team_id(message.name),
          :name => message.name,
          :owner => message.owner,
          :access => message.access,
          :max_members => message.max_members,
          :destroy_on_owner_leave => destroy_on_owner_leave?,
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
        handler_update_teams
      end

      def member_disconnected(message)
        if player_team = PlayerTeam.find!(message.player_id)
          leave = LeaveTeam.new(
            :name => player_team.name,
            :player_id => message.player_id
          )
          leave_team(leave)
        end
      end

      def destroy_team(message,force=false)
        if team = Team.find!(message.name)
          if team.owner == message.player_id || force
            team.members.each do |member|
              leave_chat(message.name,member)
              destroy_player_team(member)
              send_team_left(team.name,member)
            end
            commands.datastore.call_dbproc(:team_remove_team,'teams',team.to_storage_entity,false)
            team.destroy!
            handler_update_teams

            # team destroyed ends match
            if team.match_id
              end_match(EndMatch.new(:match_id => team.match_id))
            end
          end
        end
        unless force
          send_team_left(message.name,message.player_id)
        end
      end

      def join_team(message,invite_accepted=false)
        unless can_add_member?(message.name,message.player_id)
          return false
        end

        if team = Team.find!(message.name)

          if (team.max_members.nil? || team.max_members == 0)
            team.max_members = default_max_members
          end

          if team.members.size >= team.max_members
            return
          end

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
          if message.player_id == team.owner && destroy_on_owner_leave?
            destroy_team(
              DestroyTeam.new(
                :name => team.name,
                :player_id => message.player_id
              )
            )
            return
          end


          # Last member, just destroy
          if team.members.include?(message.player_id) && team.members.size == 1
            destroy_team(
              DestroyTeam.new(
                :name => team.name,
                :player_id => message.player_id
              ),
              true
            )
            return
          end

          team.members.delete_if {|member| member == message.player_id}

          # Reassign owner if they leave
          if team.owner == message.player_id
            team.owner = team.members.first
          end

          team.save!
          commands.datastore.call_dbproc(:team_update_team,'teams',team.to_storage_entity,false)
          destroy_player_team(message.player_id)
          send_team_left(message.name,message.player_id)
          leave_chat(message.name,message.player_id)
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

      def end_match(message)
        if match = Match.find!(message.match_id)
          match.teams.each do |team|
            if team = Team.find!(team.name)
              team.match_id = nil
              team.save!
            end
          end
          match.destroy
        end
      end

      # Called internally or from another actor to start an already defined match
      def start_match(message)
        # Don't accept requests from clients
        if message.player_id
          return
        end

        match_id = self.class.match_id_for(message.team_names)
        if match = Match.find!(match_id)
          GameMachine.logger.warn "Match #{match_id} already created"
          return
        end

        teams = message.team_names.collect {|name| Team.find!(name)}.compact
        if teams.size != message.team_names.size
          GameMachne.logger.warn "#{self.class.name} Unable to find all teams for match"
          return
        end

        server = Akka.instance.hashring.bucket_for(match_id)
        server = server.sub('akka.tcp://cluster@','').split(':').first

        match = Match.new(
          :id => match_id,
          :teams => teams,
          :server => server,
          :game_handler => message.game_handler
        )

        teams.each do |team|
          team.match_id = match_id
          team.match_server = server
          team.save
          commands.datastore.call_dbproc(:team_update_team,'teams',team.to_storage_entity,false)
        end

        match.save
        handler_match_started(match)
        GameMachine.logger.info "#{self.class.name} Match #{match} started"
      end

      def find_match(message)
        if team = Team.find(message.team_name)
          if s = handler_find_match(message.team_name)
            GameMachine.logger.info "Match found: #{s}"
            start_match(s)
          end
        end
      end

      def on_receive(message)
        if message.is_a?(CreateTeam)
          create_team(message)
          send_team(message)
        elsif message.is_a?(DestroyTeam)
          destroy_team(message)
        elsif message.is_a?(TeamInvite)
          team_invite(message)
        elsif message.is_a?(TeamAcceptInvite)
          team_accept_invite(message)
        elsif message.is_a?(JoinTeam)
          join_team(message)
          send_team(message)
        elsif message.is_a?(LeaveTeam)
          leave_team(message)
        elsif message.is_a?(TeamsRequest)
          teams_request(message)
        elsif message.is_a?(FindMatch)
          find_match(message)
        elsif message.is_a?(StartMatch)
          start_match(message)
        elsif message.is_a?(EndMatch)
          end_match(message)
        elsif message.is_a?(MessageLib::ClientManagerEvent)
          if message.event == 'disconnected'
            member_disconnected(message)
            GameMachine.logger.info "#{self.class.name} #{message.player_id} removed from team(disconnected)"
          end
        else
          unhandled(message)
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
