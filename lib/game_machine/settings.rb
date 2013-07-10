module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      File.join(GameMachine.app_root, "config/config.yml")
    )
    namespace GameMachine.env

  end 
end
