module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      File.join(File.dirname(__FILE__), "../../config/config.yml")
    )
    namespace GameMachine.env

    def self.akka_cluster_config
      config = load_akka_config('cluster')
      config.sub!('HOST',akka_host)
      if Server.instance.seed
        config.sub!('PORT',akka_port.to_s)
      else
        config.sub!('PORT','0')
      end
      config
    end

    def self.akka_config
      config = load_akka_config('akka')
      config.sub!('HOST',akka_host)
      config.sub!('PORT',akka_port.to_s)
      config
    end

    def self.load_akka_config(name)
      File.read(File.expand_path(File.join(
          File.dirname(__FILE__),"../../config/#{name}.conf")
        )
      )
    end

    def self.akka_host
      Server.instance.config.akka.host
    end

    def self.akka_port
      Server.instance.config.akka.port
    end

  end 
end
