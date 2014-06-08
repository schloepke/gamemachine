module GameMachine
  class MonoServer

    attr_reader :mono_path
    def initialize
      @mono_path = File.join(GameMachine.app_root,'mono','server')
    end

    def run!
      # mono server blocks.  We run in a loop so if it dies or we restart it,
      # it will just get restarted
      Thread.new do
        loop do
          system("cd #{mono_path} && mono server.exe #{Application.config.mono_gateway_port}")
          sleep 10
        end
      end
    end
  end
end
