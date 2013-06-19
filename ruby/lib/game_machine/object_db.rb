module GameMachine
  class ObjectDb < GameActor

    class << self
      def update(entity_id, klass,method)
        ref = find_distributed(entity_id)
        message = ObjectdbUpdate.new.set_entity_id(entity_id).set_update_class(klass).set_update_method(method)
        ref.send_message(message, :blocking => true)
      end

      def put(entity)
        ref = find_distributed(entity.get_id)
        ref.send_message(ObjectdbPut.new.set_entity(entity))
      end

      def get(entity_id)
        ref = find_distributed(entity_id)
        ref.send_message(ObjectdbGet.new.set_entity_id(entity_id), :blocking => true)
      end
    end

    def post_init(*args)
      @entities = {}
    end

    def on_receive(message)
      if message.is_a?(ObjectdbUpdate)
        entity = @entities[message.get_entity_id]
        Object.const_get(message.get_update_class).send(message.get_update_method.to_sym,entity)
        self.sender.tell(true,nil)
      elsif message.is_a?(ObjectdbPut)
        @entities[message.get_entity.get_id] = message.get_entity
        self.sender.tell(true,nil)
      elsif message.is_a?(ObjectdbGet)
        self.sender.tell(@entities[message.get_entity_id] || false,nil)
      else
        unhandled(message)
      end
    end
  end
end
