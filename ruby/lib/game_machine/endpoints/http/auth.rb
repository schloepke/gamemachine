module GameMachine
  module Endpoints
    module Http
      class Auth < JavaLib::UntypedConsumerActor
        class << self
          alias_method :apply, :new
          alias_method :create, :new
        end

        def getEndpointUri
          return "jetty:http://#{Settings.http_host}:#{Settings.http_port}/auth"
        end

        def onReceive(message)
          params = message_to_params(message)
          client_message = params_to_login_message(params)
          auth = Systems::LoginHandler.find.ask(
            client_message,1000
          )
          server = "#{Settings.http_host}:#{Settings.http_port}"
          response = {:server => server, :authtoken => auth}
          getSender.tell(JSON.generate(response),get_self)
        end

        private

        def message_to_params(message)
          body = message.getBodyAs(java.lang.String.java_class, getCamelContext)
          data = URI.decode_www_form(body)
          data.each_with_object({}) {|d,acc| acc[d[0]] = d[1]}
        end

        def params_to_login_message(params)
          client_message = ClientMessage.new
          player_login = PlayerLogin.new.set_username(
            params['username']
          ).set_password(
            params['password']
          )
          client_message.set_player_login(player_login)
        end

      end
    end
  end
end
