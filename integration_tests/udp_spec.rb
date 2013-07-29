require 'integration_helper'

module GameMachine
  describe "udp" do 

    describe "sending messages via udp" do
      it "should do" do
        measure(10,10000) do
          Thread.current['bytes'] ||= client_message.to_byte_array
          Thread.current['c'] ||= Clients::Client.new(:seed01)
          Thread.current['c'].send_message(Thread.current['bytes'])
          message = Thread.current['c'].receive_message
          ClientMessage.parse_from(message.to_java_bytes)
        end
      end
    end

  end
end

