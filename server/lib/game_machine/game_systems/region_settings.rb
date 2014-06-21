require 'settingslogic'

module GameMachine
  module GameSystems
    class RegionSettings < Settingslogic
      source  File.expand_path(
        ENV['CONFIG_FILE'] || File.join(ENV['APP_ROOT'], "config/regions.yml")
      )
      namespace ENV['GAME_ENV']

    end 
  end
end
