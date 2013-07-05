module GameMachine
  module Botnet
    class Master < Actor

      def self.start_udt_client
        UdtClient.start("GameMachine::Botnet::Master",'localhost',8101)
        UdtClient.udtClient
      end

      def self.start
        ActorBuilder.new(self).start
        client = start_udt_client
        master_ref = self.find
        master_ref.tell(client)
        ctx = nil
        loop do
         ctx = master_ref.ask('ctx',1000)
         break if ctx
         sleep 0.100
        end
        master_ref.tell('start_players')
      end

      def post_init(*args)
        @client = nil
        @ctx = nil
        @players = {}
      end

      def start_players
        100.times do |i|
          name = "player#{i}"
          child = ActorBuilder.new(PlayerBot,@client,@ctx,name).with_parent(context).with_name(name).start
          @players[name] = child
        end
      end

      def on_receive(message)
        if message.is_a?(JavaLib::DefaultChannelHandlerContext)
          @ctx = message
        elsif message.is_a?(UdtClient)
          @client = message
        elsif message == 'ctx'
          sender.tell(@ctx) if @ctx
        elsif message == 'start_players'
          start_players
        else
          client_message = ClientMessage.parse_from(message.bytes)
          player = client_message.player
          @players.fetch(player.id).tell(client_message,get_self)
        end
      end
    end
  end
end
