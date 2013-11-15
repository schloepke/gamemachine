require 'pathname'
module GameMachine
  class GameLoader

    def game_dirs
      games_root = File.join(GameMachine.app_root,'../games')
      Pathname.glob("#{games_root}/*/")
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

    def load_game(bootfile)
      require bootfile
    end
  end
end
