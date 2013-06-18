module GameMachine
  class ObjectDb < GameSystem

    UpdateMessage = Struct.new(:entity_id, :blk)
    GetMessage = Struct.new(:entity_id)
    PutMessage = Struct.new(:entity)

    class << self
      def update(entity_id, &blk)
        ask(UpdateMessage.new(entity_id,blk), :key => entity_id)
      end

      def put(entity)
        tell(PutMessage.new(entity), :key => entity.get_id)
      end

      def get(entity_id)
        ask(GetMessage.new(entity_id), :key => entity_id)
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
        @entities[message.get_id] = message
        self.sender.tell(true,nil)
      elsif message.is_a?(GetMessage)
        self.sender.tell(@entities[message.entity_id] || false,nil)
      else
        unhandled(message)
      end
    end
  end
end
