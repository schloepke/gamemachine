module GameMachine
  module GameSystems

    # @note All chat system requests go through the ChatManager
    #   Chat can be topic based or private player to player messages
    #   Topics are automatically created by the first player to send a join
    #   request to a topic that does not already exist.  
    #   Chat topic channels are distributed across the cluster, and guaranteed
    #   to be delivered.
    #
    # @aspects ChatMessage Player
    # @aspects JoinChat Player
    # @aspects LeaveChat Player
    class ChatManager < Actor::Base
      include GameMachine::Commands


      aspect %w(ChatMessage Player)
      aspect %w(JoinChat Player)
      aspect %w(LeaveChat Player)
      aspect %w(ChatStatus Player)
      aspect %w(ChatInvite Player)

      def define_update_procs
        commands.datastore.define_dbproc(:chat_remove_subscriber) do |id,current_entity,update_entity|
          if current_entity.nil?
            current_entity = MessageLib::Entity.new.set_id(id)
          end

          if current_entity.has_subscribers
            if subscriber_id_list = current_entity.subscribers.get_subscriber_id_list.to_a
              current_entity.subscribers.set_subscriber_id_list(nil)
              subscriber_id_list.each do |name|
                unless name == update_entity.id
                  current_entity.subscribers.add_subscriber_id(name)
                end
              end
            end
          end
          current_entity
        end

        commands.datastore.define_dbproc(:chat_add_subscriber) do |id,current_entity,update_entity|
          if current_entity.nil?
            current_entity = MessageLib::Entity.new.set_id(id)
          end

          unless current_entity.has_subscribers
            current_entity.set_subscribers(MessageLib::Subscribers.new)
          end

          subscriber_id_list = current_entity.subscribers.get_subscriber_id_list.to_a
          unless subscriber_id_list.include?(update_entity.id)
            current_entity.subscribers.add_subscriber_id(update_entity.id)
          end
          current_entity
        end
      end

      def post_init(*args)
        @chat_actors = {}
        define_update_procs
      end

      def on_receive(message)
        if message.is_a?(MessageLib::ChatDestroy)
          if @chat_actors.has_key?(message.player_id)
            ask_child(message.player_id,message)
            destroy_child(message.player_id)
          end
          return
        end

        if message.has_chat_invite
          send_invite(message.chat_invite)
        elsif message.has_chat_register
          unless @chat_actors.has_key?(message.chat_register.chat_id)
            create_child(message.chat_register.chat_id,message.chat_register.register_as)
          end
        else
          unless @chat_actors.has_key?(message.player.id)
            create_child(message.player.id)

            # Give it a second or the message will be lost
            sleep 1
          end
          forward_chat_request(message.player.id,message)
        end
      end

      private

      # TODO implement expiring cache store for stuff like this (invites)
      # We cannot just delete the invite when everyone leaves the channel,
      # because we have no way of knowing when everyone has left
      def send_invite(chat_invite)
        invite_id = Uniqueid.generate_token(chat_invite.inviter)
        stored_id = "invite_#{chat_invite.channel_name}_#{invite_id}"
        #puts "#{chat_invite.inviter} #{chat_invite.invitee}"
        commands.datastore.put(MessageLib::Entity.new.set_id(stored_id))
        chat_invite.set_invite_id(invite_id)
        commands.player.send_message(chat_invite,chat_invite.invitee)
      end

      def destroy_child(chat_id)
        if @chat_actors.has_key?(chat_id)
          forward_chat_request(chat_id,JavaLib::PoisonPill.get_instance)
          @chat_actors.delete(chat_id)
          self.class.logger.debug "Chat child for #{chat_id} killed"
        else
          self.class.logger.debug "chat actor for chat_id #{chat_id} not found"
        end
      end

      def ask_child(chat_id,message)
        @chat_actors[chat_id].ask(message,500)
      end

      def forward_chat_request(chat_id,message)
        @chat_actors[chat_id].tell(message,nil)
      end

      def child_name(chat_id)
        "chat#{chat_id}"
      end

      def create_child(chat_id,register_as='player')
        name = child_name(chat_id)
        builder = Actor::Builder.new(GameSystems::Chat,chat_id,register_as)
        child = builder.with_parent(context).with_name(name).start
        @chat_actors[chat_id] = Actor::Ref.new(child,GameSystems::Chat.name)
        self.class.logger.debug "Chat child for #{chat_id} created"
      end
    end
  end
end
