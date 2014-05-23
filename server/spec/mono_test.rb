class MonoTest < GameMachine::Actor::Base

  def post_init(*args)
    @vm = Mono::Vm.instance
  end

  def on_receive(message)
    namespace = 'GameMachine'
    klass = 'TestActor'
    response = @vm.send_message(namespace,klass,message)
    unless response && response.id == message.id
      #thread_id = JRuby.reference(Thread.current).native_thread.id
      puts "Response #{response}"
    end
    sender.tell(message,self)
  end

end
