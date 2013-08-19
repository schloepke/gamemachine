module Demo
  class NpcBehavior
    
    def initialize(npc,parent)
      @npc = npc
      @parent = parent
      @position = Jme::Vector3f.new(
        npc.transform.vector3.x.to_f,
        npc.transform.vector3.y.to_f,
        npc.transform.vector3.z.to_f
      )

      @target_position = Jme::Vector3f.new(10,10,0)
      @start_time = 0
      @last_update = Time.now
    end

    def update_npc_vector
      vector = @npc.transform.vector3
      vector.x = @position.x
      vector.y = @position.y
      vector.z = @position.z
    end

    def update_target_vector(new_vector)
      @target_position.set(new_vector.x,new_vector.y,new_vector.z)
    end

    def update_neighbors(neighbors)
      neighbors.npc.each do |npc|
        puts "#{npc.transform.vector3.x} #{npc.transform.vector3.y} #{npc.transform.vector3.z}"
      end
    end

    def update_target(neighbors)
      if neighbors.player
        target = neighbor.player.first
        update_target_vector(target.transform.vector)
      end
    end

    def update(neighbors=nil)
      if neighbors
        update_target(neighbors)
      else
        @delta_time = Time.now - @last_update
        move
        @start_time += 0.0001
        update_npc_vector
        get_neighbors
        track_entity
        @last_update = Time.now
      end
    end

    def move
      direction = @target_position.subtract(@position)
      direction.y = 0
      #puts "direction magnitude=#{direction.length}"
      @position = @position.interpolate(@target_position,10 * @start_time)
      #puts "position=#{@position}"
    end

    def get_neighbors
        entity = Entity.new.set_id(@npc.id).set_get_neighbors(
          GetNeighbors.new.set_value(true).set_vector3(@npc.transform.vector3)
        )
        GameMachine::GameSystems::EntityTracking.find.tell(entity,@parent)
    end

    def track_entity
        entity = Entity.new.set_id(@npc.id).set_npc(@npc).set_track_entity(
          TrackEntity.new.set_value(true)
        )
        GameMachine::GameSystems::EntityTracking.find.tell(entity)
    end

  end
end
