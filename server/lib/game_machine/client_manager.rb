
module GameMachine
  class ClientManager < Actor::Base

    attr_reader :local_actors, :remote_clients, :local_clients, :channel, :players, :client_to_player
    def post_init(*args)
      @server = Application.config.name
      @channel = 'client_events'
      @local_actors = {}
      @remote_clients = {}
      @local_clients = {}
      @players = {}
      @client_to_player = {}
      subscribe(channel)
      ClusterMonitor.find.tell('notify_on_up')
      @cluster = JavaLib::Cluster.get(getContext.system)
    end

    def on_receive(message)

      if message.is_a?(MessageLib::Entity)

        # Outgoing message to player
        if message.send_to_player
          process_player_message(message)

        # client events come from other client managers
        elsif message.has_client_event
          if message.client_event.sender_id.match(/#@server/)
            return
          end
          process_client_event(message.client_event)
        
        # Unregister requests come from clients only
        elsif message.has_client_manager_unregister
          unregister_sender(message)

        # Register requests come from actors and clients
        elsif message.has_client_manager_register
          register_sender(message)
        else
          unhandled(message)
        end
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        update_new_member(address)
      else
        unhandled(message)
      end
    end

    # TODO have the remote ack this, and resend it periodically until we get ack
    def update_new_member(message)
      client_events = MessageLib::ClientEvents.new
      client_to_player.each do |client_id,player_id|
        client_events.add_client_event(
          create_client_event(client_id,player_id,'connected')
        )
      end
      entity = MessageLib::Entity.new.set_id('0').set_client_events(client_events)
      remote_ref = Actor::Base.find_remote(message.member.address.to_string,self.class.name)
      remote_ref.tell(entity,get_self)
    end

    def process_player_message(message)
      #GameMachine.logger.info("#{self.class.name} process_player_message")
      if client_id = players.fetch(message.player.id,nil)
        if client_connection = local_clients.fetch(client_id,nil)
          send_to_player(message,client_connection)
        elsif remote_ref = remote_clients.fetch(client_id,nil)
          remote_ref.tell(message)
        end
      end
    end

    def send_to_player(message,client_connection)
      client_message = MessageLib::ClientMessage.new
      client_message.set_client_connection(client_connection)
      message.set_send_to_player(false)
      client_message.add_entity(message)
      Handlers::Gateway.find.tell(client_message)
      #Actor::Base.find(client_connection.gateway).tell(client_message)
    end

    def sender_id_to_actor_ref(sender_id)
      server,name = sender_id.split('|')
      Actor::Base.find_remote(server,name)
    end

    def process_client_event(client_event)
      if client_event.event == 'disconnected'
        remote_clients.delete(client_event.client_id)
        client_to_player.delete(client_event.client_id)
        players.delete(client_event.player_id)
        GameMachine.logger.info("#{self.class.name} client #{client_event.client_id} disconnected")
      elsif client_event.event == 'connected'
        remote_ref = sender_id_to_actor_ref(client_event.sender_id)
        remote_clients[client_event.client_id] = remote_ref
        players[client_event.player_id] = client_event.client_id
        client_to_player[client_event.client_id] = client_event.player_id
        GameMachine.logger.info("#{self.class.name} #{Application.config.name} client #{client_event.client_id} connected")
      end
    end

    def unregister_sender(message)
      unregister_msg = message.client_manager_unregister
      register_type = unregister_msg.register_type
      name = unregister_msg.name
      if register_type == 'client'
        send_client_event(name,message.player.id,'disconnected')
        local_clients.delete(name)
        players.delete(message.player.id)
        GameMachine.logger.debug("#{self.class.name} client #{name} unregistered")
      end
    end

    def register_sender(message)
      register_msg = message.client_manager_register
      register_type = register_msg.register_type
      name = register_msg.name
      events = register_msg.events

      # Client register
      if register_type == 'client'
        send_client_event(name,message.player.id,'connected')
        local_clients[name] = message.client_connection
        players[message.player.id] = name
        get_sender.tell(message,get_self)
        GameMachine.logger.debug("#{self.class.name} client #{name} registered")
      # Actor register
      elsif register_type == 'actor'
        local_actors[name] = events
      end
    end


    def create_client_event(client_id,player_id,event)
      client_event = MessageLib::ClientEvent.new.set_client_id(client_id).
        set_event(event).set_player_id(player_id)
      client_event.set_sender_id(Application.config.name + '|' + self.class.name)
    end

    def send_client_event(client_id,player_id,event)
      client_event = create_client_event(client_id,player_id,event)
      entity = MessageLib::Entity.new.set_id('0').set_client_event(client_event)
      publish = MessageLib::Publish.new
      publish.set_topic(channel).set_message(entity)
      MessageQueue.find.tell(publish,get_self)
    end

    def subscribe(topic)
      message = MessageLib::Subscribe.new.set_topic(topic)
      MessageQueue.find.tell(message,get_self)
    end
  end
end
