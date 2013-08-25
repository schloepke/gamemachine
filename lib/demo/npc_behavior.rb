module Demo
  class NpcBehavior
    

    def initialize(npc,parent)
      @npc = npc
      @parent = parent
      @position = GameMachine::Vector.new(
        npc.transform.vector3.x.to_f,
        npc.transform.vector3.y.to_f,
        npc.transform.vector3.z.to_f
      )

      @home_position = @position.clone
      @target_position = GameMachine::Vector.new
      @player_position = GameMachine::Vector.new
      @last_update = Time.now.to_f
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
      #puts "#{player.x} #{player.y} #{player.z}"
      set_target(player)
      @has_target = true
      #puts "target=#{@target_position.inspect}"
    end

    def update(neighbors=nil)
      neighbors = GameMachine::GameSystems::EntityTracking.neighbors_from_grid(@position.x,@position.y,nil,'player',nil)
      if neighbors.size > 1
        puts "too many neighbors #{neighbors.size}"
      end
      if neighbors.size >= 1
        @found = true
        update_target(neighbors.get(0))
      end
      move if @has_target
      track_entity
    end

    def move
      @delta_time = Time.now.to_f - @last_update.to_f
      speed = 0.8
      @position.interpolate(@target_position,speed * @delta_time)
      update_npc_vector
      @last_update = Time.now.to_f
      #puts "target=#{@target_position.inspect}"
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
      if @track_entity
        @track_entity.transform.vector3.set_x(@position.x).
          set_y(@position.y).set_z(@position.z)
      else
        @track_entity = Entity.new.set_track_entity(
          TrackEntity.new.set_value(true)
        ).set_id(@npc.id)
        @track_entity.set_entity_type('npc')
        @track_entity.set_transform(@npc.transform)
      end

      unless @actor_ref
        @actor_ref = GameMachine::GameSystems::EntityTracking.find.actor
      end
      @actor_ref.tell(@track_entity)
    end

  end
end
