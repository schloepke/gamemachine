module GameMachine
  module Botnet
    class Master < Actor

      def self.start_udt_client(name,port)
        UdtClient.start(name,'localhost',port)
      end

      def self.start(name,port,player_id)
        ActorBuilder.new(self,player_id).with_name(name).start
        client = start_udt_client(name,port)
        master_ref = self.find(name)
        master_ref.tell(client)
        ctx = nil
        loop do
         ctx = master_ref.ask('ctx',1000)
         break if ctx
         sleep 0.100
        end
        master_ref.tell('start_player')
      end

      def send_message_loop
        sleep 3
        Thread.new do
          loop do
            begin
              @player.tell('send_chat_message',get_self)
              sleep 0.050
            rescue Exception => e
              puts e
            end
          end
        end
      end

      def post_init(*args)
        @player_id = args.first
        @client = nil
        @ctx = nil
        @player = nil
      end

      def start_player
        name = "player#{@player_id}"
        child = ActorBuilder.new(PlayerBot,@client,@ctx,name,@player_id).with_parent(context).with_name(name).start
        @player = child
      end

      def on_receive(message)
        if message.is_a?(JavaLib::DefaultChannelHandlerContext)
          @ctx = message
        elsif message.is_a?(UdtClient)
          @client = message
        elsif message == 'ctx'
          sender.tell(@ctx) if @ctx
        elsif message == 'start_player'
          start_player
          send_message_loop
        else
          client_message = ClientMessage.parse_from(message.bytes)
          @player.tell(client_message,get_self)
        end
      end
    end
  end
end
