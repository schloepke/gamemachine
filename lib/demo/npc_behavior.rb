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

      @home_position = @position.clone
      @target_position = Jme::Vector3f.new(10,10,0)
      @has_target = false
      @last_update = Time.now.to_f
    end

    def update_npc_vector
      vector = @npc.transform.vector3
      vector.x = @position.x
      vector.y = @position.y
      vector.z = @position.z
    end

    def set_target_home
      @target_position = @home_position
    end

    def set_target(new_vector)
      @target_position.set(new_vector.x,new_vector.y,new_vector.z)
    end

    def update_neighbors(neighbors)
      neighbors.npc.each do |npc|
        puts "#{npc.transform.vector3.x} #{npc.transform.vector3.y} #{npc.transform.vector3.z}"
      end
    end

    def update_target(neighbors)
      @has_target = false
      if neighbors[:players]
        if target = neighbors[:players].first
          #puts "#{target.transform.vector3.x} #{target.transform.vector3.y} #{target.transform.vector3.z}"
          set_target(target.transform.vector3)
          @has_target = true
        end
      end
    end

    def update(neighbors=nil)
      if neighbors
        update_target(neighbors)
      else
        move
        track_entity
        #entity_updates
        neighbors = GameMachine::GameSystems::EntityTracking.neighbors_from_grid(@position.x,@position.y,nil,'player')
        update_target(neighbors)
      end
    end

    def move
      if @has_target
        target_position = @target_position
      else
        target_position = @home_position
        if @position.equals(target_position)
          return
        end
      end
      @delta_time = Time.now.to_f - @last_update.to_f
      speed = 1.0
      direction = target_position.subtract(@position)
      #direction.y = 0
      #puts "direction magnitude=#{direction.length}"
      @position = @position.interpolate(target_position,1.0 * @delta_time)
      update_npc_vector
      #puts "position=#{@position}"
      @last_update = Time.now.to_f
    end

    def entity_updates
      if @update_entity
        @update_entity.set_npc(@npc).get_neighbors.set_vector3(@npc.transform.vector3)
      end
      @update_entity = Entity.new.set_id(@npc.id).set_npc(@npc).set_track_entity(
        TrackEntity.new.set_value(true)
      ).set_get_neighbors(
        GetNeighbors.new.
        set_vector3(@npc.transform.vector3).
        set_neighbor_type('player')#.set_search_radius(100)
      )
      @actor_ref ||= GameMachine::GameSystems::EntityTracking.find
      @actor_ref.tell(@update_entity,@parent)
    end

    def get_neighbors
      entity = Entity.new.set_id(@npc.id).set_get_neighbors(
        GetNeighbors.new.
        set_vector3(@npc.transform.vector3).
        set_neighbor_type('player')#.set_search_radius(25)
      )
      GameMachine::GameSystems::EntityTracking.find.tell(entity,@parent)
    end

    def track_entity
      @track_entity ||= Entity.new.set_track_entity(
        TrackEntity.new.set_value(true)
      ).set_id(@npc.id)
      @track_entity.set_npc(@npc)
      @actor_ref ||= GameMachine::GameSystems::EntityTracking.find
      @actor_ref.tell(@track_entity)
    end

  end
end
