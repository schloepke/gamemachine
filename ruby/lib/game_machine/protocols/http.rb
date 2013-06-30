module GameMachine
  module Protocols
    class Http < JavaLib::UntypedConsumerActor
      class << self
        alias_method :apply, :new
        alias_method :create, :new
      end

      def getEndpointUri
        return "jetty:http://#{Settings.http_host}:#{Settings.http_port}/"
      end

      def onReceive(message)
        entity = Entity.parse_from(message)
        player_login = entity.player_login
        auth = LoginHandler.find.send_message(player_login,
                                                :sender => get_self,
                                                :blocking => true
                                             )
        server = "#{Settings.http_host}:#{Settings.http_port}"
        response = {:server => server, :authtoken => auth}
        getSender.tell(JSON.generate(response),get_self)
      end
    end
  end
end
