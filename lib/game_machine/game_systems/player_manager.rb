require 'active_support/inflector'
module GameMachine
  module GameSystems
    class PlayerManager < Actor::Base
        PLAYER_CONTROLLERS = java.util.concurrent.ConcurrentHashMap.new
      
      def post_init
      end

      def destroy_player_controller(message)
        id = message.player.id
        if controller = PLAYER_CONTROLLERS.fetch(id,nil)
          controller.tell(JavaLib::PoisonPill.get_instance,get_self)
          PLAYER_CONTROLLERS.delete(id)
        else
          GameMachine.logger.error("Player Controller for #{id} not found")
        end
      end

      def create_player_controller(message)
        id = message.player.id
        controller_class = Actor::Base.player_controller
        builder = Actor::Builder.new(controller_class,message)
        child = builder.with_parent(context).with_name(id).start
        PLAYER_CONTROLLERS[id] = child
      rescue Exception => e
        GameMachine.logger.error("Create Player Controller error: #{e.class} #{e.message}")
      end

      def send_to_player_controller(message)
        if player_controller = PLAYER_CONTROLLERS.fetch(message.player.id,nil)
          player_controller.tell(message,get_self)
        else
          GameMachine.logger.error("Player Controller for #{message.player.id} not found")
        end
      end

      def on_receive(message)
        if message.is_a?(Entity)
          if message.has_player_logout
            destroy_player_controller(message)
          elsif message.has_player_authenticated
            create_player_controller(message)
          end
          send_to_player_controller(message)
        else
          unhandled(message)
        end
      end

    end
  end
end


