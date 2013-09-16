require 'json'

module GameMachine
  module Endpoints
    module Http
      class Auth < JavaLib::UntypedConsumerActor
        class << self
          alias_method :apply, :new
          alias_method :create, :new
        end

        def getEndpointUri
          return "jetty:http://#{Application.config.server.http_host}:#{Application.config.server.http_port}/auth?traceEnabled=false"
        end

        def onReceive(message)
          params = message_to_params(message)
          if auth = login(params['username'],params['password'])
            server = "#{Application.config.server.http_host}:#{Application.config.server.http_port}"
            response = {:server => server, :authtoken => auth}
          else
            response = {:error => 'bad login'}
          end
          getSender.tell(JSON.generate(response),get_self)
        end

        private

        def login(user,pass)
          Application.auth_handler.authorize(user,pass)
        end

        def message_to_params(message)
          body = message.getBodyAs(java.lang.String.java_class, getCamelContext)
          data = URI.decode_www_form(body)
          data.each_with_object({}) {|d,acc| acc[d[0]] = d[1]}
        end

      end
    end
  end
end
