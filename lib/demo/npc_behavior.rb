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
      @target_position = Jme::Vector3f.new
      @target_position.zero
      @player_position = Jme::Vector3f.new
      @player_position.zero
      @last_update = Time.now.to_f
      @agro_distance = 25.0
      @speed = 1.0
    end

    def print_target_position
      puts "#{@target_position.x} #{@target_position.y} #{@target_position.z}"
    end

    def print_position
      puts "#{@position.x} #{@position.y} #{@position.z}"
    end

    def update_npc_vector
      vector = @npc.transform.vector3
      vector.x = @position.x
      vector.y = @position.y
      vector.z = @position.z
    end

    def set_target_home
      set_target(@home_position)
    end

    def set_target(new_vector)
      @target_position.set(new_vector.x,new_vector.y,new_vector.z)
    end

    def set_player_position(new_vector)
      @player_position.set(new_vector.x,new_vector.y,new_vector.z)
    end
    def update_neighbors(neighbors)
      neighbors.npc.each do |npc|
        puts "#{npc.transform.vector3.x} #{npc.transform.vector3.y} #{npc.transform.vector3.z}"
      end
    end

    def update_target(player)
      #puts "#{target.transform.vector3.x} #{target.transform.vector3.y} #{target.transform.vector3.z}"
      #set_player_position(target.transform.vector3)
      #distance_to_player = @position.distance(@player_position)
      #puts "distance_to_target=#{distance_to_target} distance_to_home=#{distance_to_home}"
      set_target(player.transform.vector3)
      #print_target_position
      @has_target = true
    end

    def update(neighbors=nil)
      if neighbors
        if players = neighbors.get_player_list
          if target = players.first
            update_target(target)
          end
        end
        return
      end
      if neighbors = GameMachine::GameSystems::EntityTracking.neighbors_from_grid(@position.x,@position.y,nil,'player')
        if neighbors.size >= 1
          update_target(neighbors.get(0).grid_value.entity)
        end
      end
      move if @has_target
      track_entity
      #entity_updates
    end

    def move
      @delta_time = Time.now.to_f - @last_update.to_f
      speed = 0.8
      #direction = @target_position.subtract(@position)
      #direction.y = 0
      #puts "direction magnitude=#{direction.length}"
      @position = @position.interpolate(@target_position,speed * @delta_time)
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
      @track_entity.set_transform(@npc.transform)
      @track_entity.set_entity_type('npc')
      @actor_ref ||= GameMachine::GameSystems::EntityTracking.find
      @actor_ref.tell(@track_entity)
    end

  end
end
