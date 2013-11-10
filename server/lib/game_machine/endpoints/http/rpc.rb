require 'json'
require 'base64'
module GameMachine
  module Endpoints
    module Http
      class Rpc < JavaLib::UntypedConsumerActor
        class << self
          alias_method :apply, :new
          alias_method :create, :new
        end

        def getEndpointUri
          return "jetty:http://#{Application.config.ttp_host}:#{Application.config.http_port}/rpc?traceEnabled=false"
        end

        def onReceive(message)
          getSender.tell("OK",get_self)
          return
          params = message_to_params(message)
          if params['data']
            encoded_message = Base64.decode64(params['data'])
            MessageLib::Entity.parse_from(encoded_message)
          end
          getSender.tell("OK",get_self)
        end

        private

        def message_to_params(message)
          body = message.getBodyAs(java.lang.String.java_class, getCamelContext)
          data = URI.decode_www_form(body)
          data.each_with_object({}) {|d,acc| acc[d[0]] = d[1]}
        end

      end
    end
  end
end
