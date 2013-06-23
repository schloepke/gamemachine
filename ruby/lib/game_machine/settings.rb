module GameMachine
  class Settings < Settingslogic
    source  File.expand_path(
      File.join(File.dirname(__FILE__), "../../config/config.yml")
    )
    namespace GameMachine.env

    class << self

      def set_address(config)
        config.sub!('HOST',akka_host)
        config.sub!('PORT',akka_port.to_s)
        config
      end

      def set_seeds(config)
        seeds = Settings.seeds.map do |seed| 
          seed_host = Settings.servers.send(seed).akka.host
          seed_port = Settings.servers.send(seed).akka.port
          "\"akka.tcp://cluster@#{seed_host}:#{seed_port}\""
        end
        config.sub!('SEEDS',seeds.join(','))
        config
      end

      def akka_cluster_config
        config = load_akka_config('cluster')
        config = set_address(config)
        config = set_seeds(config)
      end

      def akka_config
        config = load_akka_config('akka')
        config = set_address(config)
      end

      def load_akka_config(name)
        File.read(File.expand_path(File.join(
            File.dirname(__FILE__),"../../config/#{name}.conf")
          )
        )
      end

      def akka_host
        Server.instance.config.akka.host
      end

      def akka_port
        Server.instance.config.akka.port
      end

    end

  end 
end
