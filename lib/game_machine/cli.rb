require 'trollop'
require 'fileutils'
module GameMachine
  class Cli

    def self.start
      
      opts = Trollop::options do
        opt :noop, "just exit"
        opt :new, "Create new game"
        opt :server, "Run server"
        opt :boot, "Boot file.  Defaults to boot.rb", :type => :string
        opt :name, "Config name, defaults to 'default'", :type => :string
        version "GameMachine 0.0.1"
        banner <<-EOS
Usage:

* Create new game in /tmp/mygame
  --new /tmp/mygame

* Start server seed01 with boot file mygame.rb:
  --server --name=seed01 --boot=mygame.rb

EOS
      end

      if  opts[:noop]
        exit 0
      end
      
      if opts[:new]
        dir = ARGV.shift

        unless dir
          Trollop::die :new, "new requires install path"
        end
        if File.directory?(dir)
          Trollop::die :new, "install path already exists"
        end

        FileUtils.mkdir dir
        config = File.join(File.dirname(__FILE__), '../../config')
        boot = File.join(File.dirname(__FILE__), '../../boot.rb')
        FileUtils.cp_r(config,dir)
        FileUtils.cp(boot,dir)
        exit 0
      end

      if opts[:boot]
        ENV['boot'] = opts[:boot]
      end

      if opts[:server]
        GameMachine::Application.initialize!(opts[:name] || 'default',true)
        GameMachine::Application.start
      end

      if opts[:stop]
        GameMachine::Akka.instance.init!
        GameMachine::Akka.instance.kill_all
      end

    end
  end
end

