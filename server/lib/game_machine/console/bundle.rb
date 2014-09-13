
require 'digest'
require 'openssl'
require 'faraday'
require 'yaml'
require 'json'
require 'fileutils'
require_relative '../ruby_extensions/string'

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
    class Bundle

      attr_reader :command, :name, :bundle_dir, :bundle_file
      def initialize(argv)
        @command = argv.shift
        @name = argv.shift
        @bundle_dir = File.join(ENV['APP_ROOT'],'.game_machine')
        @bundle_file = File.join(bundle_dir,'bundle.zip')
      end

      
      def upload_bundle(name)
        username = AppConfig.instance.config.gamecloud.user
        api_key = AppConfig.instance.config.gamecloud.api_key
        host = AppConfig.instance.config.gamecloud.host

        url = "http://#{host}/bundle/#{username}/upload"
        token = Digest::SHA256.hexdigest("#{username}#{name}#{api_key}")
        file = Faraday::UploadIO.new(bundle_file, 'application/octet-stream')
        body = { :file => file, :name => name }
       

        response = conn.post do |req|
          req.options[:timeout] = 10
          req.options[:open_timeout] = 2 
          req.url url
          req.headers['X-Auth'] = token
          req.body = body
        end
      end


      def conn
        Faraday.new do |f|
          f.request :multipart
          f.request :url_encoded
          #f.response :logger
          f.adapter Faraday.default_adapter
        end
      end

      def create_bundle
        FileUtils.mkdir_p(bundle_dir)
        FileUtils.rm_f(bundle_file)
        system("cd #{ENV['APP_ROOT']} && bundle install --path=.game_machine/vendor/bundle >> /dev/null")
        system("cd #{ENV['APP_ROOT']} && zip -r .game_machine/bundle.zip games config db lib java bin script web .game_machine/vendor mono >> /dev/null")
      end

      def run!
        if name.nil? || name.blank?
          puts "Must specify bundle name"
          puts "Usage: game_machine service push [bundle name]"
          exit
        end

        puts "Creating bundle"
        create_bundle

        puts "Uploading bundle to server"
        response = upload_bundle(name)
        
        if response.status == 204
          puts "Push Successful"
        else
          puts "Push failed, server returned #{response.status}"
        end
      end

    end
  end
end
