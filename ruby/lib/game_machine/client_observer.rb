module GameMachine
  class ClientObserver

      class << self
        def client_observers
          @client_observers ||= java.util.concurrent.ConcurrentHashMap.new
        end
      
        def notify_observers(client_id)
          if observers = client_observers.fetch(client_id,nil)
            observers.each do |actor_ref|
              message = ClientDisconnect.new.set_client_id(client_id)
              actor_ref.tell(message)
            end
          end
        end

        def register_observer(client_id,actor_ref)
          client_observers[client_id] ||= []
          client_observers[client_id] << actor_ref
        end
      end
  end
end
