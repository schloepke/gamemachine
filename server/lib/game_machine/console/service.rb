require 'optparse'
require 'openssl'
require 'faraday'
require 'yaml'
require 'json'
require "highline/import"
require_relative '../ruby_extensions/string'
require_relative 'push'
require_relative 'deploy'

module Kernel
  def suppress_warnings
    original_verbosity = $VERBOSE
    $VERBOSE = nil
    result = yield
    $VERBOSE = original_verbosity
    return result
  end
end

suppress_warnings {OpenSSL::SSL::VERIFY_PEER = OpenSSL::SSL::VERIFY_NONE}

module GameMachine
  module Console
    class Service

      attr_reader :credentials_path, :config_path, :url, :command, :argv
      def initialize(argv)
        @credentials_path = File.join(ENV['APP_ROOT'],'credentials')
        @config_path = File.join(ENV['APP_ROOT'],'config','service.yml')
        ensure_config
        @url = config[:url]
        @argv = argv
        @command = argv.shift
      end

      def run!
        if command == 'push'
          GameMachine::Console::Push.new(self,argv).run!
        elsif command == 'deploy'
          GameMachine::Console::Deploy.new(self,argv).run!
        end
      end

      def ensure_config
      if config.nil?
        puts "#{config_path} is missing, or invalid"
        exit
      end
      end

      def login
        unless has_api_key?
          unless get_credentials
            puts "Invalid username/password"
            exit
          end
        end
      end

      def upload_conn
        conn = Faraday.new(url) do |f|
          f.request :multipart
          f.request :url_encoded
          #f.response :logger
          f.adapter Faraday.default_adapter
        end
      end

      def conn
        Faraday.new do |faraday|
          #faraday.response :logger
          faraday.adapter  Faraday.default_adapter
        end
      end

      def make_request(method, url, params)
        response = conn.send(method) do |req|
          req.options[:timeout] = 10
          req.options[:open_timeout] = 2 
          req.url url
          if [:put, :post].include?(method)
            json = JSON.generate(params)
            req.headers['Content-Type'] = 'application/json'
            req.body = json
          end
        end
      end

      def config
        if File.exists?(config_path)
          YAML.load(File.read(config_path))
        else
          nil
        end
      end

      def has_api_key?
        api_key.nil? ? false : true
      end

      def api_key
        if File.exists?(credentials_path)
          YAML.load(File.read(credentials_path))[:api_key]
        else
          nil
        end
      end

      def save_api_key(api_key)
        credentials = {:api_key => api_key}
        File.open(credentials_path,'w') {|f| f.write(YAML.dump(credentials))}
      end

      def get_api_key(username,password)
        api_url = "#{url}/api_key"
        params = {:username => username, :password => password}
        make_request(:post,api_url,params)
      end

      def get_credentials
        username = ask("Game Machine username:  ")
        pass = ask("password:  ") { |q| q.echo = false }
        response = get_api_key(username,pass)
        if response.status == 200
          data = JSON.parse(response.body)
          save_api_key(data['api_key'])
        else
          false
        end
      end

    end
  end
end