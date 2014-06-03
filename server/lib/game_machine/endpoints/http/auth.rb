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
          return "jetty:http://#{Application.config.http_host}:#{Application.config.http_port}/?matchOnUriPrefix=true&traceEnabled=false"
        end

        def onReceive(message)
          headers = message.get_headers
          headers.each do |key,value|
            GameMachine.logger.info "Header: #{key}=#{value}"
          end
          http_message = {:reply_to => getSender, :camel_message => message}
          headers = message.get_headers
          http_message[:query] = headers.fetch('CamelHttpQuery',nil)
          http_message[:uri] = headers.fetch('CamelHttpUri',nil)
          http_message[:method] = headers.fetch('CamelHttpMethod',nil)
          if http_message[:method] == 'POST'
            http_message[:body] = message.getBodyAs(java.lang.String.java_class, getCamelContext)
          end
          http_message[:camel_message] = message
          RestApi::Router.find.tell(http_message,get_self)
        rescue Exception => e
          GameMachine.logger.error "#{self.class.name} #{e.to_s}"
          getSender.tell('error',get_self)
        end


      end
    end
  end
end
