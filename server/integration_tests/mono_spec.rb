require 'integration_helper'

module GameMachine

  describe MonoTest do

    it "runs ok" do
      100000.times do
      MonoTest.find.tell(Helpers::GameMessage.new('test').to_byte_array)
      #MonoTest.find_remote('seed01').tell(Helpers::GameMessage.new('test').to_byte_array)
      end
    end
  end

end

