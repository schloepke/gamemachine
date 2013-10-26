module GameMachine
  module ProtobufExtensions
    module EntityHelper
      def add_component(component)
        send(:"set#{component.message_name}",component)
      end
    end
  end
end

Entity.send(:include, GameMachine::ProtobufExtensions::EntityHelper)
