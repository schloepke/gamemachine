module GameMachine
  class Settings < Settingslogic
  source  File.expand_path(
    File.join(File.dirname(__FILE__), "../../config/config.yml")
  )
    namespace GameMachine.env
  end
end
