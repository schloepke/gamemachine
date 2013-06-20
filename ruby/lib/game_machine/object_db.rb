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

  class ObjectDb < GameActor

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
    end

    def on_receive(message)
      if message.is_a?(ObjectdbUpdate)
        procname = message.get_update_method.to_sym
        if entity = @entities[message.get_entity_id]
          returned_entity = self.class.dbprocs[procname].call(entity)
          @entities[message.get_entity_id] = returned_entity
          self.sender.tell(returned_entity || false,nil)
        else
          self.sender.tell(false,nil)
        end
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
