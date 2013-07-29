module GameMachine
  module Bot
    class Client < Actor::Base

      def self.start_udt_client(name,port)
        UdtClient.start(name,'localhost',port)
      end

      def self.start(name,port)
        Actor::Builder.new(self).with_name(name).start
        client = start_udt_client(name,port)
        master_ref = self.find(name)
        master_ref.tell(client)
        ctx = nil
        loop do
         ctx = master_ref.ask('ctx',10)
         break if ctx
        end
        master_ref.tell('start_players')
      end

      def post_init(*args)
        @client = nil
        @ctx = nil
      end

      def start_player
        name = "player#{@player_id}"
        child = Actor::Builder.new(Bot::Chat,@client,@ctx,name,@player_id).with_parent(context).with_name(name).start
        @player = child
      end

      def on_receive(message)
        if message.is_a?(JavaLib::DefaultChannelHandlerContext)
          @ctx = message
        elsif message.is_a?(UdtClient)
          @client = message
        elsif message == 'ctx'
          sender.tell(@ctx) if @ctx
        elsif message == 'start_players'
          GameMachine.logger.info "#{@player_id} started"
          #start_player
          player_id = rand(10000).to_s
          m = GameMachine::Helpers::GameMessage.new(player_id)
          m.player_logout
          @client.send_to_server(m.to_byte_array,@ctx)
        else
          client_message = ClientMessage.parse_from(message.bytes)
          #@player.tell(client_message,get_self)
        end
      end
    end
  end
end
