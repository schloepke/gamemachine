require 'benchmark'
module Example
  class AggressiveNpc < Npc
    include GameMachine::Commands

    attr_reader :target_id

    def post_init
      @target_id = nil
      @check_home_counter = 0
      @check_home_interval = 5
      @acquire_target_counter = 0
      @acquire_target_interval = 5
      @agro_radius = 10
      @leash_radius = 15
    end

    def set_spawn_point
      position.x = rand(1000) + 1
      position.y = rand(1000) + 1
      #position.z = 40
    end

    def has_target?
      target_id.nil? ? false : true
    end

    def update_target
      if target = players.fetch(target_id,nil)
        movement.update_target(target[:vector])
      else
        @target_id = nil
      end
    end

    def choose_target
      if player = sorted_players.first
        if position.distance(player[:vector]) <= @agro_radius
          movement.set_target(player[:vector])
          @target_id = player[:id]
        end
      end
    end

    def leash?
      if @check_home_counter >= @check_home_interval
        @check_home_counter = 0
        if movement.position.distance(home) > @leash_radius
          return true
        end
      end
      false
    end

    def go_home!
      @target_id = 'home'
      movement.set_target(@home)
    end

    def going_home?
      @target_id == 'home'
    end

    def reached_home?
      going_home? && movement.reached_target
    end

    def reset_target
      movement.drop_target
      @target_id = nil
    end

    def update
      if going_home?
        if reached_home?
          reset_target
        else
          movement.update
        end
        return
      end

      if leash?
        reset_target
        go_home!
        return
      end

      if @acquire_target_counter >= @acquire_target_interval
        check_players
        if has_players? && !has_target?
          choose_target
        end
        @acquire_target_counter = 0
      end

      if has_target?
        update_target
        movement.update
      end

      @acquire_target_counter += 1
      @check_home_counter += 1
    end

  end
end
