require_relative 'npc_movement'

module Tutorial
  class Npc
    include GameMachine::Commands

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
      
      post_init
    end

    def post_init
    end


    def set_spawn_point
      position.x = rand(2000) + 1
      position.y = rand(2000) + 1
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
      @target.x = position.x + rand(-30..30)
      @target.y = position.y + rand(-30..30)
      if @target.x < 2 || @target.x > 2000
        @target.x = rand(5..2000)
      end
      if @target.y < 2 || @target.y > 2000
        @target.x = rand(5..2000)
      end
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
