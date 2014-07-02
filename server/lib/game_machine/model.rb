require 'json'
# JSON models that will get converted appropriately when sent to or
# received by actors.

# JsonEntity messages always get converted.  JsonStorage messages
# are a special case for the object database and are left untouched
# by the tell/ask/on_receive filters

require 'ostruct'

module GameMachine
  class Model < OpenStruct
    include GameMachine::Commands

    class << self

      def attribute(*args)

      end

      def delete(id)
        commands.datastore.delete(id)
      end

      def delete!(id)
        commands.datastore.delete!(id)
      end

      def find!(id)
        scoped_id = scope_for(id)
        if entity = Commands::Base.commands.datastore.get!(scoped_id)
          from_entity(entity,:json_storage)
        else
          nil
        end
      end

      def find(id,timeout=1000)
        scoped_id = scope_for(id)
        if entity = Commands::Base.commands.datastore.get(scoped_id,timeout)
          from_entity(entity,:json_storage)
        else
          nil
        end
      end

      # TODO cache klass names in hash to avoid constantize
      def from_entity(entity,type=:json_entity)
        if type == :json_storage
          json = entity.json_storage.json
        else
          json = entity.json_entity.json
        end

        attributes = JSON.parse(json)
        if klass = attributes.delete('klass')
          model = klass.constantize.new(attributes)
          model.id = model.unscoped_id
          model
        else
          raise "Unable to find klass attribute in #{attributes.inspect}"
        end
      end

      def scope_for(id)
        if id_scope
          "#{id_scope}|#{id}"
        else
          id
        end
      end

      def set_id_scope(scope)
        @id_scope = scope
      end

      def id_scope
        @id_scope
      end
    end

    def attributes
      self.marshal_dump
    end

    def scoped_id
      if self.class.id_scope
        self.class.scope_for(id)
      else
        id
      end
    end

    def unscoped_id
      if self.class.id_scope
        id.sub(/^#{self.class.id_scope}\|/,'')
      else
        id
      end
    end

    def to_json
      attributes['id'] = scoped_id
      JSON.dump(attributes.merge(:klass => self.class.name))
    end

    def to_entity
      MessageLib::Entity.new.set_id(scoped_id).set_json_entity(to_json_entity)
    end

    def to_json_entity
      MessageLib::JsonEntity.new.set_json(to_json).set_klass(self.class.name)
    end

    def to_storage_entity
      MessageLib::Entity.new.set_id(scoped_id).set_json_storage(to_json_storage)
    end

    def to_json_storage
      MessageLib::JsonStorage.new.set_json(to_json)
    end

    def save
      commands.datastore.put(to_storage_entity)
    end

    def save!
      commands.datastore.put!(to_storage_entity)
    end

    def destroy
      commands.datastore.delete(scoped_id)
    end

    def destroy!
      commands.datastore.delete!(scoped_id)
    end

  end
end

