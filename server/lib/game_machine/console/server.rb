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


      def self.exit!
        clear_restart_file
        System.exit(0)
      end

      def self.ensure_tmp
        tmp_path = File.join(ENV['APP_ROOT'],'tmp')
        FileUtils.mkdir_p tmp_path
      end

      def self.restart?
        File.exists?(restart_file)
      end

      def self.restart!
        FileUtils.touch restart_file
      end

      def self.clear_restart_file
        if File.exists?(restart_file)
          FileUtils.rm(restart_file)
        end
      end

      def self.restart_file
        File.join(ENV['APP_ROOT'],'tmp','gm_restart.txt')
      end

      def self.run_in_loop
        ensure_tmp
        clear_restart_file

        build_cmd = "jruby bin/game_machine build"
        run_cmd = "jruby bin/game_machine s -r"
        loop do
          system(build_cmd)
          system(run_cmd)
        end
      end

      def run!
        check_start_dir

        GameMachine.logger.info "Starting with options = #{options.inspect}"
        GameMachine::Application.initialize!(options[:server],true)
        GameMachine::Application.start
      end

      def load_server_from_file
        path = File.join(File.expand_path(Dir.pwd),'servername.txt')
        if File.exists?(path)
          return File.read(path).chomp
        else
          nil
        end
      end

      def parse_arguments(arguments)
        options = {}

        OptionParser.new do |opt|
          opt.banner = "Usage: game_machine server [options]"
          opt.on('-r', '--restartable', 'If tmp/gm_restart.txt should trigger a restart') { |v| options[:restartable] = v }
          opt.on('-s', '--server=name', String, 'Server name') { |v| options[:server] = v.strip }
          opt.on('-c', '--config=name', String, 'Configuration file') { |v| options[:config] = v.strip }
          opt.on("-e", "--environment=name", String,
                  "Specifies the environment to run under (development/production).",
                  "Default: development") { |v| options[:environment] = v.strip }
          opt.parse!(arguments)
        end
        
        if options.has_key?(:restartable)
          ENV['RESTARTABLE'] = 'true'
        end

        unless options.has_key?(:environment)
          options[:environment] = 'development'
        end

        unless options.has_key?(:server)
          options[:server] = 'default'
        end

        if server = load_server_from_file
          options[:server] = server
          puts "server name #{server} set from servername.txt"
        end

        options
      end

    end
  end
end
