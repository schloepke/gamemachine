require 'optparse'
module GameMachine
  module Console
    class Server

      attr_reader :options
      def initialize(argv)
        @options = parse_arguments(argv)
      end

      def check_start_dir
        java_dir = File.join(File.expand_path(Dir.pwd),'java')
        unless File.directory?(java_dir)
          puts "Please run game_machine from your game directory"
          exit 0
        end
      end

      def set_environment
        ENV['GAME_ENV'] = options[:environment]
        if options[:config]
          ENV['CONFIG_FILE'] = options[:config]
        end
      end

      def run!
        check_start_dir

        GameMachine.logger.info "Starting with options = #{options.inspect}"
        GameMachine::Application.initialize!(options[:server],true)
        GameMachine::Application.start
      end

      def parse_arguments(arguments)
        options = {}

        OptionParser.new do |opt|
          opt.banner = "Usage: game_machine server [options]"
          opt.on('-s', '--server=name', String, 'Server name') { |v| options[:server] = v.strip }
          opt.on('-c', '--config=name', String, 'Configuration file') { |v| options[:config] = v.strip }
          opt.on("-e", "--environment=name", String,
                  "Specifies the environment to run under (development/production).",
                  "Default: development") { |v| options[:environment] = v.strip }
          opt.parse!(arguments)
        end
        
        unless options.has_key?(:environment)
          options[:environment] = 'development'
        end

        unless options.has_key?(:server)
          options[:server] = 'default'
        end

        options
      end

    end
  end
end
