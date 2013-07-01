require 'integration_helper'

module GameMachine
  describe "basic" do 
    let(:client) {Client.new(:seed01)}

    context "test" do
      describe "udt" do

        it "stress with small payload" do
          bytes = client_message.to_byte_array
          measure(10,10000) do
            Thread.current['s'] ||= Client.connect_udt
            result = Client.send_udt(Thread.current['s'],bytes)
            ClientMessage.parse_from(result)
          end
        end

        it "stress with large payload" do
          bytes = large_client_message.to_byte_array
          measure(10,10000) do
            Thread.current['s'] ||= Client.connect_udt
            result = Client.send_udt(Thread.current['s'],bytes)
            ClientMessage.parse_from(result)
          end
        end

      end
    end

  end
end

