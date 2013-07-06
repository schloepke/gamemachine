module GameMachine
  module Botnet
    class Master < Actor

      def self.start_udt_client(name,port)
        UdtClient.start(name,'localhost',port)
      end

      def self.start(name,port,all_players,player_list)
        ActorBuilder.new(self,all_players,player_list).with_name(name).start
        client = start_udt_client(name,port)
        master_ref = self.find(name)
        master_ref.tell(client)
        ctx = nil
        loop do
         ctx = master_ref.ask('ctx',1000)
         break if ctx
         sleep 0.100
        end
        master_ref.tell('start_players')
      end

      def send_message_loop
        sleep 5
        Thread.new do
          loop do
            begin
              @all_players.each do |player_id|
                name = "player#{player_id}"
                if player = @players.fetch(name,nil)
                  player.tell('send_chat_message',get_self)
                  sleep 0.010
                end
              end
            rescue Exception => e
              puts e
            end
          end
        end
      end

      def post_init(*args)
        @all_players = args.first
        @player_list = args.last
        @client = nil
        @ctx = nil
        @players = {}
      end

      def start_players
        @player_list.each do |i|
          name = "player#{i}"
          child = ActorBuilder.new(PlayerBot,@client,@ctx,name,@all_players).with_parent(context).with_name(name).start
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
          send_message_loop
        else
          client_message = ClientMessage.parse_from(message.bytes)
          player = client_message.player
          @players.fetch(player.id).tell(client_message,get_self)
        end
      end
    end
  end
end
