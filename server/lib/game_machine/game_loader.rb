require 'pathname'
module GameMachine
  class GameLoader

    class << self

      def games_root
        File.join(GameMachine.app_root,'/games')
      end
    end


    def game_dirs
      Pathname.glob("#{self.class.games_root}/*/")
    end

    def load_from_dir(dir)
      bootfile = File.join(dir,'boot.rb')
      if File.exists?(bootfile)
        load_game(bootfile)
        GameMachine.logger.info "#{bootfile} loaded"
        GameMachine.stdout "#{bootfile} loaded"
      else
        GameMachine.logger.info "#{bootfile} not found"
        GameMachine.stdout "#{bootfile} not found"
      end
    end

    def load_all
      return if GameMachine.env == 'test'
      game_dirs.each do |game_dir|
        load_from_dir(game_dir)
      end
    end

    def start_player_manager
      if klass = Application.config.player_manager
        Actor::Builder.new(klass.constantize).start
      end
    end

    def load_game(bootfile)
      require bootfile
    end
  end
end
