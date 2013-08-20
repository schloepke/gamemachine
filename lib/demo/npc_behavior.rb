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
      @last_update = Time.now.to_f
      @target_is_player = false
      @update_count = 0
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
        if target = neighbors.player.first
          update_target_vector(target.transform.vector3)
          @target_is_player = true
          move
          @start_time += 0.0001
          update_npc_vector
        end
      end
    end

    def update(neighbors=nil)
      if neighbors
        update_target(neighbors)
      else
        @delta_time = Time.now.to_f - @last_update.to_f
        if @update_count >= 2
          get_neighbors
          @update_count = 0
        end
        track_entity
        @last_update = Time.now.to_f
        if @target_is_player
          #GameMachine.logger.info("target=#{@target_position.x} #{@target_position.y} #{@target_position.z}")
        end
      end
      @update_count += 1
    end

    def move
      direction = @target_position.subtract(@position)
      direction.y = 0
      #puts "direction magnitude=#{direction.length}"
      @position = @position.interpolate(@target_position,1 * @delta_time)
      #puts "position=#{@position}"
    end

    def get_neighbors
        entity = Entity.new.set_id(@npc.id).set_get_neighbors(
          GetNeighbors.new.
          set_value(true).
          set_vector3(@npc.transform.vector3)#.set_search_radius(25)
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
