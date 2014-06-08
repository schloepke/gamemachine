require 'spec_helper'
require_relative 'mono_test'
require "base64"
require 'socket'
require "uri"
require 'net/http'
java_import 'java.util.concurrent.ThreadPoolExecutor'
java_import 'java.util.concurrent.TimeUnit'
java_import 'java.util.concurrent.LinkedBlockingQueue'
java_import 'java.util.concurrent.FutureTask'
java_import 'java.util.concurrent.Callable'

CHARS = [*('a'..'z'),*('0'..'9')].flatten
STR = Array.new(100) {|i| CHARS.sample}.join
STR2 = Array.new(1000) {|i| CHARS.sample}.join


module GameMachine
  describe 'mono' do

    it "mono gateway" do
      Actor::Builder.new(Endpoints::MonoGateway).start
      sleep 1
      str = Array.new(1000) {|i| CHARS.sample}.join
      message = MessageLib::Entity.new.set_id(STR)
      message.set_destination('GameMachine.TestActor')
      puts timed = Benchmark.realtime {
        10.times do
          Endpoints::MonoGateway.find.tell(message)
        end
      }
      sleep 2
    end

  end
end
