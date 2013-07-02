module GameMachine
  module Systems
    class Chat < Actor

      def preStart
        subscribe = Subscribe.new
        subscribe.topic = 'global_chat'
        MessageQueue.find.tell(subscribe,self)
      end

      def post_init(*args)

      end

      def on_receive(message)

      end
    end
  end
end
