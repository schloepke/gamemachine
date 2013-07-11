module GameMachine
  class PlayerRegistry < Actor::Base

    def on_receive(message)
      if message.is_a?(PlayerRegister)
        self.class.register_player(message.player_id,message.client_connection)
        if message.observer
          observer_ref = Actor::Base.find(message.observer)
          self.class.register_observer(message.player_id,observer_ref)
        end
        get_sender.tell(true,nil)
        GameMachine.logger.info "PlayerRegister #{message.player_id}"

      elsif message.is_a?(ClientDisconnect)
        player_id = self.class.client_disconnect(message.client_connection)
        GameMachine.logger.info "ClientDisconnect #{player_id}"

      elsif message.is_a?(PlayerLogout)
        self.class.player_logout(message.player_id)
        GameMachine.logger.info "PlayerLogout #{message.player_id}"

      elsif message.is_a?(RegisterPlayerObserver)
        self.class.register_observer(message.playerId,sender)
      end
    end

    class << self

      def client_disconnect(client_connection)
        if player_id = player_id_for(client_connection.id)
          notify_observers(player_id,client_connection.id)
        end
        player_id
      end

      def player_logout(player_id)
        if client_id = client_id_for(player_id)
          notify_observers(player_id,client_id)
        end
      end

      def notify_observers(player_id,client_id)
        if actor_refs = observers.fetch(player_id,nil)
          actor_refs.each do |actor_ref|
            message = Disconnected.new.
              set_client_id(client_id).
              set_player_id(player_id)
            actor_ref.tell(message)
            GameMachine.logger.debug "Oberver Notified #{player_id} #{actor_ref}"
          end
          unregister_player(player_id)
          remove_observers(player_id)
        end
      end

      def unregister_player(player_id)
        client_id = client_id_for(player_id)
        remove_player_id(client_id)
        remove_client_connection(player_id)
      end

      def remove_player_id(client_id)
        player_ids.delete(client_id)
      end

      def remove_client_connection(player_id)
        client_connections.delete(player_id)
      end

      def register_observer(player_id,actor_ref)
        unless observers.fetch(player_id,nil)
          observers[player_id] = []
        end
        observers[player_id] << actor_ref
        GameMachine.logger.debug "Observer Registered #{player_id} #{actor_ref}"
        true
      end

      def remove_observers(player_id)
        if observer = observers.fetch(player_id,nil)
          observer.clear
        end
      end

      def register_player(player_id,client_connection)
        client_id = client_connection.id
        player_ids[client_id] = player_id
        client_connections[player_id] = client_connection
      end

      def observers_for(player_id)
        observers.fetch(player_id,nil)
      end

      def player_id_for(client_id)
        player_ids.fetch(client_id,nil)
      end

      def client_id_for(player_id)
        if client_connection = client_connection_for(player_id)
          client_connection.id
        else
          nil
        end
      end

      def client_connection_for(player_id)
        client_connections.fetch(player_id,nil)
      end

      # clear mainly needed for unit tests, rspec can't reset 
      # ConcurrentHashMap correctly
      def clear_observers
        observers.clear
      end

      def clear_player_ids
        player_ids.clear
      end

      def clear_client_connections
        client_connections.clear
      end

      private

      def observers
        @observers ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def player_ids
        @player_ids ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def client_connections
        @client_connections ||= java.util.concurrent.ConcurrentHashMap.new
      end

    end
  end
end
