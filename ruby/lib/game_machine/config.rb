module GameMachine
  class Config

    def self.akka_config
      File.read(
        File.expand_path(
          File.join(File.dirname(__FILE__),
                    "../../config/#{GameMachine.env}_akka.conf")
        )
      )
    end

  end
end
