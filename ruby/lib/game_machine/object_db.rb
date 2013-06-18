module GameMachine
  class ObjectDb < GameActor

    UpdateMessage = Struct.new(:entity_id, :blk)
    GetMessage = Struct.new(:entity_id)
    PutMessage = Struct.new(:entity)

    class << self
      def update(entity_id, &blk)
        ref = find_distributed(entity_id)
        message = UpdateMessage.new(entity_id,blk)
        ref.send_message(message, :blocking => true)
      end

      def put(entity)
        ref = find_distributed(entity.get_id)
        ref.send_message(PutMessage.new(entity))
      end

      def get(entity_id)
        ref = find_distributed(entity_id)
        ref.send_message(GetMessage.new(entity_id), :blocking => true)
      end
    end

    def post_init(*args)
      @entities = {}
    end

    def on_receive(message)
      GameMachine.logger.info("ObjectDb message #{message.inspect}")
      if message.is_a?(UpdateMessage)
        message.blk.call @entities[message.entity_id]
        self.sender.tell(true,nil)
      elsif message.is_a?(PutMessage)
        @entities[message.entity.get_id] = message.entity
        self.sender.tell(true,nil)
      elsif message.is_a?(GetMessage)
        self.sender.tell(@entities[message.entity_id] || false,nil)
      else
        unhandled(message)
      end
    end
  end
end
