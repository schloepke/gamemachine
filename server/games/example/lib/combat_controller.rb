
# Handles all the combat.  Npc controllers and clients send Attack messages
# to this actor which calculates damage, persists any changes, and sends out
# combat updates to all players in the area as well as to any npc's that were
# hit.

# If the attack combat_ability is 'aoe', we use the aoe grid which is a 
# secondary grid with a smaller radius.  Currently we use the attacker as the
# point to query on, but it could be easily extended to use any point, so you
# could have aoe damage with a center anywhere you want it to be.


module Example
  class CombatController < GameMachine::Actor::Base
    include GameMachine::Commands
    include Models

    attr_reader :aoe_grid
    def post_init(*args)
      @aoe_grid = GameMachine::Grid.find_or_create('aoe')
    end

    def hit?
      chance = rand(0..10)
      chance >= 5 ? true : false
    end

    def damage(entity_type)
      if entity_type == 'player'
        rand(0..4)
      else
        rand(20..120)
      end
    end

    def on_receive(message)
      if message.is_a?(Attack)
        attack = message
        attacker_vitals = Vitals.find(attack.attacker)

        if attacker_vitals.entity_type == 'player'
          player_id = attack.attacker
        else
          player_id = attack.target
        end

        # gets our targets.  Does range query if aoe
        targets = find_targets(attack,attacker_vitals)
        #GameMachine.logger.info "targets #{targets.map{|target| target.id}.join(',')}"

        # calculates/applies damage and returns a combat update for each hit
        combat_updates = attack_targets(targets,attack)

        # Get all players within our larger radius so we can send them the updates
        neighbors = commands.grid.get_neighbors_for(attack.attacker)

        combat_updates.each do |combat_update|
          #GameMachine.logger.info "#{combat_update.attacker} hit #{combat_update.target} for #{combat_update.damage} #{attack.combat_ability} damage"
          commands.player.send_message(combat_update,player_id)

          if neighbors
            neighbors.each do |neighbor|
              next if neighbor.id == player_id
              commands.player.send_message(combat_update,neighbor.id)
            end
          end
        end

      end
    end

    def attack_targets(targets,attack)
      combat_updates = []

      targets.each do |target_vitals|
          
        if target_vitals.entity_type.blank?
          target_vitals.entity_type = 'npc'
          GameMachine.logger.info "#{target_vitals.id} has no entity type, fixing"
        end
        if hit?
          dmg = damage(target_vitals.entity_type)
          next if dmg == 0
          if target_vitals.health - dmg <= 0
            target_vitals.health = 0
          else
            target_vitals.health -= dmg
          end
          target_vitals.save
          #GameMachine.logger.info "#{target_vitals.id}(#{target_vitals.entity_type}) has #{target_vitals.health}"

          combat_update = CombatUpdate.new(
            :target => target_vitals.id,
            :attacker => attack.attacker,
            :damage => dmg,
            :target_health => target_vitals.health
          )
          combat_updates << combat_update

          if target_vitals.entity_type == 'npc'
            if npc_group_actor = Game.npcs.fetch(target_vitals.id,nil)
              GameMachine::Actor::Base.find(npc_group_actor).tell(combat_update)
            else
              GameMachine.logger.info "group actor for #{target_vitals.id} not found"
            end
          end

        end
      end
      combat_updates
    end

    # If it's an aoe attack, use our aoe grid to find targets in range, otherwise
    # use the specified target
    def find_targets(attack,attacker)
      targets = []

      # npc's only hit players, players hit everything
      if attacker.entity_type == 'npc'
        search_type = 'player'
      else
        search_type = nil
      end

      if attack.combat_ability == 'aoe'
        if grid_value = @aoe_grid.get(attack.attacker)
          @aoe_grid.neighbors(grid_value.x,grid_value.y,search_type).each do |grid_value|
            next if grid_value.id == attack.attacker
            if target_vitals = Vitals.find(grid_value.id)
              targets << target_vitals
            end
          end
        end
      else
        if target_vitals = Vitals.find(attack.target)
          targets << target_vitals
        end
      end
      targets
    end


  end
end
