module GameMachine
  
  class Server
    
    def self.start
      Root.start('localhost', "1234")
    end
  end
end