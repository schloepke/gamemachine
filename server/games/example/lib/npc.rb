module Example
  class Npc
    include GameMachine::Commands
    include Models

    attr_accessor :position, :id, :players, :movement, :player_index, :home, :vitals, :dead

    def initialize(id)
      @dead = false
      @id = id
      @position = GameMachine::Vector.new
      @last_ai = Time.now.to_i
      @player_check_counter = 0
      @players = {}
      set_spawn_point
      @movement = NpcMovement.new(id,position)
      movement.speed_scale = 2
      @home = GameMachine::Vector.from(position)
      @target = GameMachine::Vector.new
      get_players
      #GameMachine.logger.info "Npc #{id} created"
      initialize_vitals
      post_init
    end

    def initialize_vitals
      if @vitals = Vitals.find(id,5000)

        # Put dead npc's back to full health
        if @vitals.health < @vitals.max_health
          @vitals.health = @vitals.max_health
          @vitals.save
        end
      else
        @vitals = Vitals.new(
          :id => id,
          :max_health => 50,
          :health => 50,
          :entity_type => 'npc'
        )
        @vitals.save
      end
    end

    def post_init
    end

    def update_combat(combat_update)
      if combat_update.target == id && combat_update.target_health == 0
        @dead = true
        GameMachine.logger.info "Npc #{id} died!"
        commands.grid.remove(id)
      end
    end

    def set_spawn_point
      position.x = rand(1000) + 1
      position.y = rand(1000) + 1
    end

    def has_players?
      players.size > 0
    end

    def sorted_players
      players.values.sort do |a,b|
        position.distance(a[:vector]) <=> position.distance(b[:vector])
      end
    end

    def get_players
      commands.grid.neighbors(position.x,position.y).each do |player|
        if @players[player.id]
          @players[player.id][:vector].x = player.x
          @players[player.id][:vector].y = player.y
          @players[player.id][:vector].z = player.z
        else
          @players[player.id] = {:id => player.id, :vector => GameMachine::Vector.from(player)}
        end
      end
    end

    def check_players
      get_players
    end

    def run_ai
      pick_random_target
    end

    def pick_random_target
      @target.x = position.x + rand(-30..30)
      @target.y = position.y + rand(-30..30)
      movement.set_target(@target)
    end

    def update
      return if dead
      if movement.has_target
        if movement.reached_target
          run_ai
        end
      else
        run_ai
      end

      movement.update
    end

  end
end
