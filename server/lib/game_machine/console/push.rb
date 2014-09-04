
module GameMachine
  module Console
    class Push

      attr_reader :options, :service, :name
      def initialize(service,argv)
        @name = argv.shift
        @options = parse_arguments(argv)
        @service = service
      end

      
      def upload_bundle(name)
        url = "#{service.url}/bundle"
        file = Faraday::UploadIO.new('/tmp/bundle.zip', 'application/octet-stream')
        body = { :file => file, :api_key => service.api_key, :name => name }
        response = service.upload_conn.post do |req|
          req.options[:timeout] = 10
          req.options[:open_timeout] = 2 
          req.url url
          req.body = body
        end
      end

      def run!
        if name.nil? || name.blank?
          puts "Must specify bundle name"
          puts "Usage: game_machine service push [bundle name]"
          exit
        end

        service.login
        puts "Creating bundle"
        system("script/bundle.sh >> /dev/null")

        puts "Uploading bundle to server"
        response = upload_bundle(name)
        
        if response.status == 204
          puts "Push Successful"
        else
          puts "Push failed, server returned #{response.status}"
        end
      end


      def parse_arguments(arguments)
        options = {}

        OptionParser.new do |opt|
          opt.banner = "Usage: game_machine push [options]"
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
