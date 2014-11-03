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

    attr_accessor :store, :cache, :classmap
    def post_init(*args)
      @store = DbLib::Store.get_instance
      @cache = DbLib::MemoryMap.get_instance.get_map
      @classmap = {}
    end

    def class_cache(classname)
      return MessageLib::Entity if classname.nil?

      if cached = classmap.fetch(classname,nil)
        return cached
      else
        classmap[classname] = "GameMachine::MessageLib::#{classname}".constantize
        classmap[classname]
      end
    end

    def set_entity(entity)
      cache.put(entity.id,entity.to_byte_array)
      store.set(entity.id,entity)
    end

    def get_entity(entity_id,klass)
      if bytes = cache.get(entity_id)
        class_cache(klass).parse_from(bytes)
      else
        store.get(entity_id,klass)
      end
    end

    def on_receive(message)
      if message.is_a?(MessageLib::ObjectdbUpdate)
        procname = message.get_update_method.to_sym
        current_entity_id = message.get_current_entity_id
        update_entity = message.get_update_entity
        current_entity = get_entity(current_entity_id,'Entity')
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
      else
        unhandled(message)
      end
    end

  end
end
