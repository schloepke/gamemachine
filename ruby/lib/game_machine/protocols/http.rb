module GameMachine
  module Protocols
    class Http < JavaLib::UntypedConsumerActor
      class << self
        alias_method :apply, :new
        alias_method :create, :new
      end

      def getEndpointUri
        return "jetty:http://localhost:8877/"
      end

      def onReceive(message)
        response = {:server => 'localhost:8100', :authtoken => 'authorized'}
        getSender.tell(JSON.generate(response),get_self)
      end
    end
  end
end
