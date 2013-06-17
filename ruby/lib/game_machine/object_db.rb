module GameMachine
  class ObjectDb < GameSystem

    def post_init(*args)
      GameMachine.logger.info "ObjectDb started"
    end
    def on_receive(message)
      puts "ObjectDB GOT #{message}"
      message.blk.call message.entity
      self.sender.tell('ok',nil)
    end
  end
end
