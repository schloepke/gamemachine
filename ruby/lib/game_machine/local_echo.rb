module GameMachine
  class LocalEcho < ActorBase
    
    def on_receive(message)
      puts "LocalEcho got #{message}"
      get_sender.tell(message,get_self)
    end
  end
end
