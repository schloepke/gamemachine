require 'benchmark'
require 'active_support/inflector'
module GameMachine
  module GameSystems
    class SingletonRouter < Actor::Base
      
      def post_init
        @singleton_controllers = {}
      end

      def destroy_child_controller(message)
        id = message.destroy_singleton.id
        if controller = @singleton_controllers.fetch(id,nil)
          controller.tell(JavaLib::PoisonPill.get_instance,get_self)
          @singleton_controllers.delete(id)
        else
          GameMachine.logger.error("Singleton Controller for #{id} not found")
        end
      end

      def create_child_controller(message)
        id = message.create_singleton.id
        controller_class = message.create_singleton.controller.constantize
        builder = Actor::Builder.new(controller_class,message)
        child = builder.with_parent(context).with_name(id).start
        @singleton_controllers[id] = child
      rescue Exception => e
        GameMachine.logger.error("CreateSingleton error: #{e.class} #{e.message}")
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'update'
            @singleton_controllers.each do |id,controller|
              controller.tell(message,get_self)
            end
          end
        elsif message.is_a?(Entity)
          if message.has_notify_singleton
            if singleton_controller = @singleton_controllers.fetch(message.notify_singleton.id,nil)
              singleton_controller.tell(message,get_self)
            else
              GameMachine.logger.error("Singleton Controller for #{message.notify_singleton.id} not found")
            end
          elsif message.has_create_singleton
            create_child_controller(message)
          elsif message.has_destroy_singleton
            destroy_child_controller(message)
          end
        end
      end

    end
  end
end

