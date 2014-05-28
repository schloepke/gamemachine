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
          return "jetty:http://#{Application.config.http_host}:#{Application.config.http_port}/auth?traceEnabled=false"
        end

        def onReceive(message)
          params = message_to_params(message)
          if auth = login(params['username'],params['password'])
            response = auth
          else
            response = "error"
          end
          getSender.tell(response,get_self)
        rescue Exception => e
          GameMachine.logger.error "#{self.class.name} #{e.to_s}"
          getSender.tell('error',get_self)
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
