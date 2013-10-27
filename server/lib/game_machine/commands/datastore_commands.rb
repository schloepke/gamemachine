module GameMachine
  module Commands
    class DatastoreCommands

      def define_dbproc(name,&blk)
        ObjectDb.dbprocs[name] = blk
      end

      def call_dbproc(name,entity_id,blocking=true)
        ref = ObjectDb.find_distributed(entity_id)
        message = MessageLib::ObjectdbUpdate.new.set_entity_id(entity_id).set_update_class('deprecated').set_update_method(name)
        if blocking
          ref.ask(message, 100)
        else
          ref.tell(message)
        end
      end

      def put(entity)
        ref = ObjectDb.find_distributed(entity.get_id)
        ref.tell(MessageLib::ObjectdbPut.new.set_entity(entity))
      end

      def get(entity_id)
        ref = ObjectDb.find_distributed(entity_id)
        ref.ask(MessageLib::ObjectdbGet.new.set_entity_id(entity_id), 100)
      end

    end
  end
end
