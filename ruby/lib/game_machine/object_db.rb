module GameMachine

  module ObjectDbProc
    def self.included(base)
      base.extend ClassMethods
    end

    module ClassMethods
      def dbproc(name,&blk)
        ObjectDb.dbprocs[name] = blk
      end
    end
  end

  class ObjectDb < Actor

    class << self

      def dbprocs
        @@dbprocs ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def call_dbproc(name,entity_id,blocking=true)
        ref = find_distributed(entity_id)
        message = ObjectdbUpdate.new.set_entity_id(entity_id).set_update_class('deprecated').set_update_method(name)
        ref.send_message(message, :blocking => blocking)
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
      @client = DataStores::Couchbase.instance.client
    end

    def set_entity(entity)
      @entities[entity.id] = entity
      WriteBehindCache.find_distributed(entity.id).tell(entity)
    end

    def get_entity(entity_id)
      entity = @entities.fetch(entity_id,nil)
      if entity.nil?
        if bytes = @client.get(entity_id)
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
          self.sender.send_message(returned_entity || false)
        else
          self.sender.send_message(false)
        end
      elsif message.is_a?(ObjectdbPut)
        set_entity(message.get_entity)
        self.sender.send_message(true)
      elsif message.is_a?(ObjectdbGet)
        self.sender.send_message(get_entity(message.get_entity_id) || false)
      else
        unhandled(message)
      end
    end
  end
end
