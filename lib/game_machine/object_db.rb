module GameMachine

  module ObjectDbProc
    def self.included(base)
      base.extend ClassMethods
    end

    module ClassMethods
      # Defines a stored  procedure that can be called later and run on the
      #   actor that contains the entity you want to update.
      def dbproc(name,&blk)
        ObjectDb.dbprocs[name] = blk
      end
    end
  end

  class ObjectDb < Actor::Base

    class << self

      def dbprocs
        @dbprocs ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def call_dbproc(name,entity_id,blocking=true)
        ref = find_distributed(entity_id)
        message = ObjectdbUpdate.new.set_entity_id(entity_id).set_update_class('deprecated').set_update_method(name)
        if blocking
          ref.ask(message, 100)
        else
          ref.tell(message)
        end
      end

      def put(entity)
        ref = find_distributed(entity.get_id)
        ref.tell(ObjectdbPut.new.set_entity(entity))
      end

      def get(entity_id)
        ref = find_distributed(entity_id)
        ref.ask(ObjectdbGet.new.set_entity_id(entity_id), 100)
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
          entity = Entity.parse_from(bytes)
        end
      end
      entity
    end

    def on_receive(message)
      if message.is_a?(ObjectdbUpdate)
        procname = message.get_update_method.to_sym
        if entity = get_entity(message.get_entity_id)
          returned_entity = self.class.dbprocs[procname].call(entity)
          set_entity(returned_entity)
          sender.tell(returned_entity || false)
        else
          sender.tell(false)
        end
      elsif message.is_a?(ObjectdbPut)
        set_entity(message.get_entity)
        sender.tell(true)
      elsif message.is_a?(ObjectdbGet)
        sender.tell(get_entity(message.get_entity_id) || false)
      else
        unhandled(message)
      end
    end
  end
end
