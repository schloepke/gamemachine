module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      File.join(File.dirname(__FILE__), "../../config/config.yml")
    )
    namespace GameMachine.env

    def self.akka_config
      config = File.read(
        File.expand_path(
          File.join(File.dirname(__FILE__),
                    "../../config/#{GameMachine.env}_akka.conf")
        )
      )
      config.sub!('HOST',akka_host)
      config.sub!('PORT',akka_port.to_s)
      config
    end

    def self.akka_host
      Server.instance.config.akka.host
    end

    def self.akka_port
      Server.instance.config.akka.port
    end

  end 
end
