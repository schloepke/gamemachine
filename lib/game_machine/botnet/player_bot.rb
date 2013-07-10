module GameMachine
  module Botnet
    class PlayerBot < Actor::Base

      CHAT_CHANNELS = ['region','global','help','group1']

      def post_init(*args)
        @client = args.first
        @ctx = args[1]
        @player_id = args[2]
        @scheduler = get_context.system.scheduler
        @dispatcher = get_context.system.dispatcher
        #login
        join_chat_groups
        #schedule_activities
        #echo
      end

      
      def schedule_activities
        sleep(rand(10))
        duration = JavaLib::Duration.create(rand(10) + 1, java.util.concurrent.TimeUnit::SECONDS)
        @scheduler.schedule(duration, duration, get_self, "send_chat_message", @dispatcher, nil)
      end

      def echo
        message = GameMachine::Helpers::GameMessage.new(@player_id)
        message.echo_test('testing')
        send_to_server(message)
      end

      def login
        message = GameMachine::Helpers::GameMessage.new(@player_id)
      end

      def send_to_server(message)
        @client.send_to_server(message.client_message.to_byte_array,@ctx)
      end

      def join_chat_groups
        message = GameMachine::Helpers::GameMessage.new(@player_id)
        CHAT_CHANNELS.each do |name|
          message.join_chat(name)
        end
        send_to_server(message)
      end


      def send_chat_message
        message = GameMachine::Helpers::GameMessage.new(@player_id)
        CHAT_CHANNELS.each do |name|
          message.chat_message('group','testing',name)
        end
        send_to_server(message)
      end

      def on_receive(message)
        if message == 'send_chat_message'
          send_chat_message
        else
        end
      end

    end
  end
end
