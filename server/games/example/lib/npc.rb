module Example
  class Npc
    include GameMachine::Commands
    include Models

    attr_accessor :position, :id, :players, :movement, :player_index, :home, :vitals

    def initialize(id)
      @id = id
      @position = GameMachine::Vector.new
      @last_ai = Time.now.to_i
      @player_check_counter = 0
      @players = {}
      set_spawn_point
      @movement = NpcMovement.new(id,position)
      @home = GameMachine::Vector.from(position)
      get_players
      #GameMachine.logger.info "Npc #{id} created"
      find_or_create_vitals
      post_init
    end

    def find_or_create_vitals
      unless @vitals = Vitals.find(id,5000)
        @vitals = Vitals.new(
          :id => id,
          :max_health => 50,
          :health => 50,
          :defense_skill => 5,
          :offense_skill => 5,
          :entity_type => 'npc'
        )
        @vitals.save
      end
    end

    def post_init
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
      @players = {}
      commands.grid.neighbors(position.x,position.y).each do |player|
        @players[player.id] = {:id => player.id, :vector => GameMachine::Vector.from(player)}
      end
    end

    def check_players
      get_players
    end

    def run_ai
      pick_random_target
    end

    def pick_random_target
      target = GameMachine::Vector.new
      target.x = position.x + rand(-30..30)
      target.y = position.y + rand(-30..30)
      @target = target
      movement.set_target(target)
    end

    def update
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
