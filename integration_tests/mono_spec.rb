require 'integration_helper'
require_relative '../spec/boot.rb'

module GameMachine

  describe MonoTest do

    it "runs ok" do
      100000.times do
      MonoTest.find_remote('seed01').tell(Helpers::GameMessage.new('test').to_byte_array)
      end
    end
  end

end

