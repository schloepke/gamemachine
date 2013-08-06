module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      ENV['CONFIG_FILE'] || File.join(GameMachine.app_root, "config/config.yml")
    )
    namespace GameMachine.env

  end 
end
