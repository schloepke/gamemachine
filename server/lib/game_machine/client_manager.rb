
module GameMachine
  class ClientManager < Actor::Base
    attr_reader :local_actors, :remote_clients, :local_clients, :channel, :players, :client_to_player

    def self.local_players
      if @local_players
        @local_players
      else
        @local_players = java.util.concurrent.ConcurrentHashMap.new
      end
    end

    # Includes region + combined.
    def self.local_connections
      local_players.select {|player_id,type| type != 'cluster'}
    end

    # Cluster only connections.  We should not be handling things like
    # entity tracking for this type of connection.  This is for stuff that is
    # not latency sensitive such as chat, groups, etc..
    def self.cluster_connections
      local_players.select {|player_id,type| type == 'cluster'}
    end

    def self.send_to_player(message)
      if local_players.has_key?(message.player.id)
        Actor::Base.find(message.player.id).tell(message)
      else
        find.tell(message)
      end
    end

    def post_init(*args)
      @server = Akka.instance.address
      @channel = 'client_events'
      @local_actors = {}
      @remote_clients = {}
      @local_clients = {}
      @players = {}
      @client_to_player = {}
      subscribe(channel)
      ClusterMonitor.find.tell('notify_on_up',get_self)
      @cluster = JavaLib::Cluster.get(getContext.system)
    end

    def on_receive(message)

      if message.is_a?(MessageLib::Entity)

        # Outgoing message to player
        if message.send_to_player
          if self.class.local_players.has_key?(message.player.id)
            Actor::Base.find(message.player.id).tell(message)
          else
           send_to_remote_player(message)
          end

        # client events come from other client managers
        elsif message.has_client_event
          if message.client_event.sender_id.match(/#{@server}/)
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

    def send_to_remote_player(message)
      if client_id = players.fetch(message.player.id,nil)
        if remote_ref = remote_clients.fetch(client_id,nil)
          remote_ref.tell(message)
        end
      end
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
        self.class.logger.debug("client #{client_event.client_id} disconnected")
      elsif client_event.event == 'connected'
        remote_ref = sender_id_to_actor_ref(client_event.sender_id)
        remote_clients[client_event.client_id] = remote_ref
        players[client_event.player_id] = client_event.client_id
        client_to_player[client_event.client_id] = client_event.player_id
        self.class.logger.debug("#{Application.config.name} client #{client_event.client_id} connected")
      end
    end

    def cluster_connection?(client_connection)
      case client_connection.type
      when 'cluster'
        true
      when 'combined'
        true
      else
        false
      end
    end

    def unregister_sender(message)
      unregister_msg = message.client_manager_unregister
      register_type = unregister_msg.register_type
      name = unregister_msg.name
      if register_type == 'client'
        if cluster_connection?(message.client_connection)
          send_client_event(name,message.player.id,'disconnected')
        end

        # Notify registered actors using a client manager event
        send_client_manager_event(name,message.player.id,'disconnected')

        local_clients.delete(name)
        players.delete(message.player.id)
        self.class.local_players.delete(message.player.id)
        self.class.logger.debug("client #{name} unregistered")
      end
    end

    def register_sender(message)
      register_msg = message.client_manager_register
      register_type = register_msg.register_type
      name = register_msg.name
      events = register_msg.events

      # Client register
      if register_type == 'client'
        if cluster_connection?(message.client_connection)
          send_client_event(name,message.player.id,'connected')
        end
        local_clients[name] = message.client_connection
        players[message.player.id] = name
        self.class.local_players[message.player.id] = message.client_connection.type
        get_sender.tell(message,get_self)
        self.class.logger.info("client #{name} registered")
      # Actor register
      elsif register_type == 'actor'
        self.class.logger.info "Actor #{name} registered"
        local_actors[name] = events
      end
    end


    def send_client_manager_event(client_id,player_id,event)
      local_actors.each do |name,events|
        client_manager_event = create_client_manager_event(client_id,player_id,event)
        Actor::Base.find(name).tell(client_manager_event)
        self.class.logger.info "Send #{event} to #{name}"
      end
    end

    def create_client_manager_event(client_id,player_id,event)
      client_manager_event = MessageLib::ClientManagerEvent.new.
        set_client_id(client_id).set_player_id(player_id).set_event(event)
    end

    def create_client_event(client_id,player_id,event)
      client_event = MessageLib::ClientEvent.new.set_client_id(client_id).
        set_event(event).set_player_id(player_id)
      client_event.set_sender_id(Akka.instance.address + '|' + self.class.name)
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
