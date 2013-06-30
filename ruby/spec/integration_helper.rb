require 'rubygems'
require 'game_machine'

RSpec.configure do |config|
  config.before(:suite) do
    GameMachine::Server.instance.init!('member01', :cluster => true)
    GameMachine::Server.instance.start_actor_system
    GameMachine::Server.instance.start_game_systems
    puts "before suite"
  end

  config.before(:each) do
  end

  config.after(:each) do
  end

  config.after(:suite) do
    puts "after suite"
    GameMachine::Server.instance.stop_actor_system
  end
end

def measure(num_threads,loops, &blk)
  threads = []
  num_threads.times do |thread_id|
    threads << Thread.new do
      results = []
      loops.times do |i|
        results << Benchmark.realtime do
          yield
        end
      end
      puts "Number = #{results.number} Average #{results.mean} Standard deviation #{results.standard_deviation}"
    end
  end
  threads.map(&:join)
end


