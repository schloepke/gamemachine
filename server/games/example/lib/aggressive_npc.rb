require 'benchmark'
module Example
  class AggressiveNpc < Npc
    include GameMachine::Commands
    include Models

    attr_reader :target_id

    def post_init
      @target_id = nil
      @check_home_counter = 0
      @check_home_interval = 5
      @acquire_target_counter = 0
      @acquire_target_interval = 5
      @agro_radius = 15
      @leash_radius = 30
  
      movement.speed_scale = 4
      @last_attack = Time.now.to_i
      @attack_interval = 2
    end

    def log(msg)
      GameMachine.logger.info "#{id} #{msg}"
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
        true
      else
        @target_id = nil
        false
      end
    end

    def choose_target
      if player = sorted_players.first
        if position.distance(player[:vector]) <= @agro_radius
          movement.set_target(player[:vector])
          @target_id = player[:id]
          #log("target chosen #{@target_id}")
        end
      end
    end

    def leash?
      if @check_home_counter >= @check_home_interval
        @check_home_counter = 0
        if movement.position.distance(home) > @leash_radius
          return true
        elsif movement.current_target &&
          (movement.position.distance(movement.current_target) > @leash_radius)
          return true
        end
      end
      false
    end

    def go_home!
      log("going home")
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

    def attack_target
      if Time.now.to_i - @last_attack < @attack_interval
        return
      end

      attack = Attack.new(
        :target => target_id,
        :attacker => id,
        :combat_ability => 'bite'
      )
      CombatController.find.tell(attack)
      #log("attacked target #{target_id}")
      @last_attack = Time.now.to_i
    end

    def update
      return if dead
      if going_home?
        if reached_home?
          log("home reached")
          reset_target
        else
          movement.update_target(home)
          movement.update
        end
        return
      end

      if leash?
        log("leashed!")
        reset_target
        go_home!
        return
      end

      # if we have a target, acquire their location every tick, otherwise only
      # check every acquire_target_interval updates
      if has_target? || @acquire_target_counter >= @acquire_target_interval
        movement.updates_between_move = 1
        check_players
        if has_players?
          unless has_target?
            choose_target
          end
        else
          # No players but we have a player target?  Player must have logged out
          # or otherwise disappeared, so we need to reset
          if @target_id && @target_id != 'home'
            GameMachine.logger.info "#{id} No players but has target!?? (target: #{@target_id})"
            reset_target
            go_home!
          elsif movement.position.distance(home) > 0
            GameMachine.logger.info "#{id} No players but is #{position.distance(home)} from home (target: #{@target_id})"
            reset_target
            go_home!
          end
        end

        @acquire_target_counter = 0
      else
        movement.updates_between_move = 20
      end

      if has_target?
        if movement.distance_to_target <= 3
          attack_target
        end

        if movement.reached_target
          reset_target
        else
          if update_target
            movement.update
          else
            # Target out of range for some reason (logged out, etc..)
            GameMachine.logger.info "#{id} lost target! #{@target_id}"
            reset_target
            go_home!
          end
        end
      end

      @acquire_target_counter += 1
      @check_home_counter += 1
    end

  end
end
