require 'slop'
module GameMachine
  class Cli

    def self.start
      opts = Slop.parse(:help => true) do
        banner 'Usage: datacube [options]'

        on '--cluster',  'start in cluster mode'
        on 'name=',  'Akka name'
        on '--server',  'start in standalone server mode'
        on '--stop',  'stop all nodes'

        help
      end


      if opts.stop?
        GameMachine::Akka.instance.init!
        GameMachine::Akka.instance.kill_all
      end


      if opts.server? || opts.cluster?
        GameMachine::Application.initialize!(opts[:name],opts.cluster?)
        GameMachine::Application.start
      end
    end
  end
end

