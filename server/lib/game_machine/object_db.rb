module GameMachine
  class ObjectDb < Actor::Base

    class << self
      def dbprocs
        if @dbprocs
          @dbprocs
        else
          @dbprocs = java.util.concurrent.ConcurrentHashMap.new
        end
      end
    end

    attr_accessor :entities, :store
    def post_init(*args)
      @entities = {}
      @store = DataStore.instance
    end

    def delete_entity(entity_id)
      entities.delete(entity_id)
      @store.delete(entity_id)
    end

    def delete_all
      entities = {}
      store.delete_all
    end

    def set_entity(entity)
      entities[entity.id] = entity
      WriteBehindCache.find_distributed(entity.id).tell(entity)
    end

    def get_entity(entity_id)
      entity = entities.fetch(entity_id,nil)
      if entity.nil?
        entity = store.get(entity_id)
      end
      entity
    end

    def on_receive(message)
      if message.is_a?(MessageLib::ObjectdbUpdate)
        procname = message.get_update_method.to_sym
        current_entity_id = message.get_current_entity_id
        update_entity = message.get_update_entity
        current_entity = get_entity(current_entity_id)
        if self.class.dbprocs.has_key?(procname)
          dbproc = self.class.dbprocs[procname]
          returned_entity = dbproc.call(
            current_entity_id,current_entity,update_entity
          )
          set_entity(returned_entity)
          sender.tell(returned_entity || false)
        else
          GameMachine.logger.warn("Unable to find dbproc #{procname}")
        end

      elsif message.is_a?(MessageLib::ObjectdbPut)
        set_entity(message.get_entity)
        sender.tell(true)
      elsif message.is_a?(MessageLib::ObjectdbGet)
        sender.tell(get_entity(message.get_entity_id) || false)
      elsif message.is_a?(MessageLib::ObjectdbDel)
        delete_entity(message.get_entity_id)
      else
        unhandled(message)
      end
    end

  end
end
