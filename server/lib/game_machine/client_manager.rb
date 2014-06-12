
module GameMachine
  class ClientManager < Actor::Base

    attr_reader :local_actors, :remote_clients, :local_clients, :channel, :local_players
    def post_init(*args)
      @channel = 'client_events'
      @local_actors = {}
      @remote_clients = {}
      @local_clients = {}
      @local_players = {}
      subscribe(channel)
    end

    def on_receive(message)
      if message.is_a?(MessageLib::Entity)

        # Outgoing message to player
        if message.send_to_player
          process_player_message(message)
          return
        end

        # client events come from other client managers
        if message.has_client_event
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
      end
    end

    def process_player_message(message)
      #GameMachine.logger.info("#{self.class.name} process_player_message")
      if client_id = local_players.fetch(message.player.id,nil)
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
      Actor::Base.find(client_connection.gateway).tell(client_message)
    end

    def process_client_event(client_event)
      if client_event.event == 'disconnected'
        remote_clients.delete(client_event.client_id)
        GameMachine.logger.info("#{self.class.name} client #{client_event.client_id} disconnected")
      elsif client_event.event == 'connected'
        server,name = client_event.sender_id.split('|')
        remote_ref = Actor::Base.find_remote(server,name)
        remote_clients[client_event.client_id] = remote_ref
        GameMachine.logger.info("#{self.class.name} client #{client_event.client_id} connected")
      end
    end

    def unregister_sender(message)
      unregister_msg = message.client_manager_unregister
      register_type = unregister_msg.register_type
      name = unregister_msg.name
      if register_type == 'client'
        send_client_event(name,'disconnected')
        local_clients.delete(name)
        local_players.delete(message.player.id)
        GameMachine.logger.info("#{self.class.name} client #{name} unregistered")
      end
    end

    def register_sender(message)
      register_msg = message.client_manager_register
      register_type = register_msg.register_type
      name = register_msg.name
      events = register_msg.events

      # Client register
      if register_type == 'client'
        send_client_event(name,'connected')
        local_clients[name] = message.client_connection
        local_players[message.player.id] = name
        GameMachine.logger.info("#{self.class.name} client #{name} registered")
      # Actor register
      elsif register_type == 'actor'
        local_actors[name] = events
      end
    end


    def send_client_event(client_id,event)
      client_event = MessageLib::ClientEvent.new.set_client_id(client_id).
        set_event(event)
      client_event.set_sender_id(Application.config.name + '|' + self.class.name)
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
