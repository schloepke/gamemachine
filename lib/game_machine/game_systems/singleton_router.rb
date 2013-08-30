require 'benchmark'
require 'active_support/inflector'
module GameMachine
  module GameSystems
    class SingletonRouter < Actor::Base
      
      def post_init
        @npc_controllers = {}
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'update'
            @npc_controllers.each do |npc_id,controller|
              controller.update
            end
          end
        elsif message.is_a?(Entity)
          if message.has_notify_singleton
            if npc_controller = @npc_controllers.fetch(message.notify_singleton.npc_id,nil)
              npc_controller.on_receive(message)
            else
              GameMachine.logger.error("Npc Controller for #{message.notify_singleton.npc_id} not found")
            end
          elsif message.has_create_singleton
            begin
              npc_id = message.create_singleton.npc_id
              controller_class = message.create_singleton.controller.constantize
              @npc_controllers[npc_id] = controller_class.new(message,self)
            rescue Exception => e
              GameMachine.logger.error("CreateSingleton error: #{e.class} #{e.message}")
            end
          elsif message.has_destroy_singleton
            if npc_controller = @npc_controllers.fetch(message.destroy_singleton.npc_id,nil)
              npc_controller.destroy(message)
              @npc_controllers.delete(message.destroy_singleton.npc_id)
            else
              GameMachine.logger.error("Npc Controller for #{message.destroy_singleton.npc_id} not found")
            end
          end
        end
      end

    end
  end
end

