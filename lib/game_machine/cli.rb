require 'trollop'
require 'fileutils'
module GameMachine
  class Cli

    def self.start
      
      opts = Trollop::options do
        opt :new
        opt :cluster
        version "GameMachine 0.0.1"
        banner <<-EOS
      Usage:
            new install path
            cluster [name]
EOS
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
      end

      if opts[:stop]
        GameMachine::Akka.instance.init!
        GameMachine::Akka.instance.kill_all
      end


      if opts[:cluster]
        name = ARGV.shift || 'default'
        GameMachine::Application.initialize!(name,true)
        GameMachine::Application.start
      end

    end
  end
end

