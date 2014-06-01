
require 'json'
require 'hashie'
module GameMachine
  module Helpers
    class JsonObject < Hashie::Mash

      def self.from_entity(entity)
        new(JSON.parse(entity.json_entity.json))
      end

      def to_entity
        entity = MessageLib::Entity.new.set_id(id)
        json_entity = MessageLib::JsonEntity.new.set_json(JSON.generate(self))
        entity.set_json_entity(json_entity)
      end
    end
  end
end
