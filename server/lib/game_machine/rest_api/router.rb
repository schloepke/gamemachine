module GameMachine
  module RestApi
    class Router < Actor::Base

      def on_receive(message)
        reply_to = message[:reply_to]
        uri = message.fetch(:uri)
        if uri.match(/\/auth/)
          Auth.find.tell(message,reply_to)
        elsif uri.match(/\/protobuf/)
          ProtobufCompiler.find.tell(message,reply_to)
        else
          reply_to.tell('error',get_self)
          GameMachine.logger.info "Invalid http route #{uri}"
        end
      end
    end
  end
end
