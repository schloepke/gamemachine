require 'settingslogic'

module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      ENV['CONFIG_FILE'] || File.join(ENV['APP_ROOT'], "config/config.yml")
    )
    namespace ENV['GAME_ENV']

  end 
end
