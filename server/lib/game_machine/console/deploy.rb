module GameMachine
  module Console
    class Deploy

      attr_reader :options, :service
      def initialize(service,argv)
        @options = parse_arguments(argv)
        @service = service
      end

      def parse_arguments(arguments)
        options = {}

        OptionParser.new do |opt|
          opt.banner = "Usage: game_machine deploy [options]"
          opt.on('-n', '--name=name', String, 'Give a name to the bundle') { |v| options[:server] = v.strip }
          
          opt.parse!(arguments)
        end
        
        unless options.has_key?(:name)
          options[:name] = 'default'
        end

        options
      end

    end
  end
end