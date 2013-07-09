
ENV['GAME_ENV'] = 'test'
require 'rubygems'
require 'game_machine'

RSpec.configure do |config|
  config.before(:suite) do
    GameMachine::Server.instance.init!('default', :cluster => true)
    puts "before suite"
  end

  config.before(:each) do
    GameMachine::Server.instance.start_actor_system
    GameMachine::Server.instance.start_game_systems
  end

  config.after(:each) do
    GameMachine::Server.instance.stop_actor_system

  end

  config.after(:suite) do
    puts "after suite"
  end
end

module ClassMethods

  require "rspec/mocks/standalone"
  require "rspec/expectations"
  require "test/unit"

    include RSpec::Matchers

  def expected
    @expected ||= {}
  end

  def actual
    @actual ||= {}
  end

  def should_receive_message(message,name=self.name, &blk)
    blk.call
    called = false
    10.times do
      if actual.has_key?(name)
        called = true
        break
      end
      sleep 0.020
    end
    expect(actual[name]).to eq(message)
    actual.delete(name)
  end

end

module InstanceMethods

    def onReceive(message)
      on_receive(message)
      self.class.actual[get_self.path.name] = message
    end

end
result = []
ObjectSpace.each_object(::Class) {|klass| result << klass if klass < GameMachine::Actor }
result.each do |klass|
  klass.send(:include, InstanceMethods)
  klass.send(:extend, ClassMethods)
end

