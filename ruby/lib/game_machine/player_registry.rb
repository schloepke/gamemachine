module GameMachine
  class PlayerRegistry

      class << self

        def client_disconnect(client_id)
          player_id = player(client_id)
          notify_observers(player_id,client_id)
        end

        def player_logout(player_id)
          client_id = client(player_id)
          notify_observers(player_id,client_id)
        end

        def notify_observers(player_id,client_id)
          if actor_refs = observers.fetch(player_id,nil)
            actor_refs.each do |actor_ref|
              message = Disconnected.new.
                set_client_id(client_id).
                set_player_id(player_id)
              actor_ref.tell(message)
            end
            remove_player(client_id)
            remove_client(player_id)
            remove_observers(player_id)
          end
        end

        def remove_player(client_id)
          players.delete(client_id)
        end

        def remove_client(player_id)
          clients.delete(player_id)
        end

        def register_observer(player_id,actor_ref)
          observers[player_id] ||= []
          observers[player_id] << actor_ref
          true
        end

        def remove_observers(player_id)
          if observer = observers.fetch(player_id,nil)
            observer.clear
          end
        end

        def register_player(player_id,client_id)
          players[client_id] = player_id
          clients[player_id] = client_id
        end

        def observers_for(player_id)
          observers.fetch(player_id,nil)
        end

        def player(client_id)
          players.fetch(client_id,nil)
        end

        def client(player_id)
          clients.fetch(player_id,nil)
        end

        def clear_observers
          observers.clear
        end

        def clear_players
          players.clear
        end

        def clear_clients
          clients.clear
        end

        private

        def observers
          @observers ||= java.util.concurrent.ConcurrentHashMap.new
        end

        def players
          @players ||= java.util.concurrent.ConcurrentHashMap.new
        end

        def clients
          @clients ||= java.util.concurrent.ConcurrentHashMap.new
        end


      end
  end
end
