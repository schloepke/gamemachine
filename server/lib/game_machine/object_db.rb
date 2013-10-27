module GameMachine

  class ObjectDb < Actor::Base

    class << self
      def dbprocs
        @dbprocs ||= java.util.concurrent.ConcurrentHashMap.new
      end
    end

    def post_init(*args)
      @entities = {}
      @store = DataStore.instance
    end

    def set_entity(entity)
      @entities[entity.id] = entity
      WriteBehindCache.find_distributed(entity.id).tell(entity)
    end

    def get_entity(entity_id)
      entity = @entities.fetch(entity_id,nil)
      if entity.nil?
        if bytes = @store.get(entity_id)
          entity = MessageLib::Entity.parse_from(bytes)
        end
      end
      entity
    end

    def on_receive(message)
      if message.is_a?(MessageLib::ObjectdbUpdate)
        procname = message.get_update_method.to_sym
        if entity = get_entity(message.get_entity_id)
          returned_entity = self.class.dbprocs[procname].call(entity)
          set_entity(returned_entity)
          sender.tell(returned_entity || false)
        else
          sender.tell(false)
        end
      elsif message.is_a?(MessageLib::ObjectdbPut)
        set_entity(message.get_entity)
        sender.tell(true)
      elsif message.is_a?(MessageLib::ObjectdbGet)
        sender.tell(get_entity(message.get_entity_id) || false)
      else
        unhandled(message)
      end
    end
  end
end
