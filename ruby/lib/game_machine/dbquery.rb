module GameMachine
  class Dbquery

    attr_reader :entity, :blk
    def initialize(entity,blk)
      @entity = entity
      @blk = blk
    end

    def self.update(entity,&blk)
      result = ObjectDb.ask(new(entity,blk))
    end
  end
end
