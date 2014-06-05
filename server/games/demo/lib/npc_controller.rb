require 'state_machine'
module Demo
  class NpcController < GameMachine::GameSystems::SingletonController
    include GameMachine::Helpers::StateMachine
    include GameMachine::Commands

    state_machine :state, :initial => :inactive do
      event :activate do
        transition :inactive => :idle
      end

      event :attack do
        transition [:idle, :going_home] => :attacking
      end

      event :go_home do
        transition :attacking => :going_home
      end

      event :reached_home do
        transition :going_home => :idle
      end

      after_transition :on => :going_home, :do => :set_target_home
      after_transition :on => :idle, :do => :clear_target
    end

    # Start is our initializer, called at start and restart
    attr_accessor :target_position, :target_id
    def start
      @target_id = nil
      @target_position = GameMachine::Vector.new

      @pathfinder = commands.navigation.query_ref(1)
      position.set(entity.vector3.x, entity.vector3.y, entity.vector3.z)
      @home_position = position.clone
      @move_to = GameMachine::Vector.new
      @last_target_position = GameMachine::Vector.new
      @last_move = Time.now.to_f
      @last_combat_update = Time.now.to_f
      @speed = 0.8
      @path = GameMachine::Navigation::Path.new([],position)
      unless saved_entity = commands.datastore.get(entity.id)
        commands.datastore.put(entity)
      end
      load_state(entity.id) do
        activate
      end
      track
    end

    def find_path
      #puts "#{position.x},#{position.y},#{position.z} to #{@target_position.x},#{@target_position.y},#{@target_position.z}"
      @path.current_location = position
      next_point = @path.next_point
      if next_point.nil? || @target_position.distance(@last_target_position) > 1
        @last_target_position = @target_position.clone
        detour_path = @pathfinder.find_path(
          position.x,position.y,position.z,
          @target_position.x,@target_position.y, @target_position.z, 1
        )
        if @pathfinder.error
          puts @pathfinder.error
          return
        end
        @path.set_path(detour_path)
        next_point = @path.next_point
        #puts "TARGET #{@target_position.inspect} PATH #{detour_path.inspect}"
      end

      if next_point.nil?
        return
      end
      #puts "#{position.x},#{position.y},#{position.z} to #{@target_position.x},#{@target_position.y},#{@target_position.z} via #{target_path.inspect}"
      @move_to = next_point
      #puts @move_to.inspect
    end

    def acquire_target
      grid_value = nil
      if has_player_target?
        grid_value = find_grid_value_by_id(target_id)
      else
        players = neighbors('player')
        if players.size >= 1
          grid_value = players.get(0)
        end
      end

      if grid_value
        set_target(grid_value.id,grid_value)
      else
        clear_target
        false
      end
    end

    def clear_target
      @has_target = false
      target_position = nil
      target_id = nil
    end

    def set_target_home
      set_target(:home,home)
    end

    def set_target(id,vector)
      target_id = id
      target_position.set(vector.x,vector.z,vector.y)
      @has_target = true
    end

    def has_target?
      @has_target
    end

    def has_player_target?
      has_target? && target_id && target_id != :home
    end

    def update
      load_state(entity.id)

      if going_home?
        distance_home = position.distance(target_position)
        if distance_home < 0.5
          reached_home!
        else
          move_towards_target
        end
        return
      end

      if idle? or attacking?
        acquire_target
        if has_player_target?
          if idle?
            attack!
          end
        else
          if attacking?
            go_home!
          end
        end

        if has_target?
          move_towards_target
          #attack
        end
      end

      save_state(entity.id)
    end

    def move_towards_target
      find_path
      unless @move_to.zero?
        move
        track(position)
      end
    end

    def attack_player

      # Eligible to attack every 2 seconds
      combat_delta_time = Time.now.to_f - @last_combat_update.to_f
      return if combat_delta_time < 2

      # If target is within range, attack.  We choose the ability to use
      # and hand it off to the combat controller.
      distance_to_target = position.distance(@target_position)
      if distance_to_target <= 3
        attack_entity = GameMachine::MessageLib::Entity.new
        attack_entity.set_id(entity.id)
        attack_entity.set_attack(
          GameMachine::MessageLib::Attack.new.set_attacker(entity.id).
            set_target(@target_id).
            set_combat_ability_id(1)
        )
        CombatController.find.tell(attack_entity,get_self)
      end
      @last_combat_update = Time.now.to_f
    end

    def move
      @delta_time = Time.now.to_f - @last_move.to_f

      # Especially at startup, delta time can be too long adjust if
      # necessary
      if @delta_time >= 0.100
        @delta_time = 0.100
      end

      # Simple interpolation towards the target
      position.interpolate(@move_to,@speed * @delta_time)
      @last_move = Time.now.to_f
    end

    # The singleton manager sends update messages to all singleton
    # controllers at a configurable interval.  Default 100ms.
    def on_receive(message)
      update
    end

  end
end
