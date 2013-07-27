require 'integration_helper'

module GameMachine
  describe "basic" do 
    let(:client) {Client.new(:seed01)}

    context "test" do
      describe "udt" do

        it "stress with small payload" do
          measure(10,10000) do
            Thread.current['bytes'] ||= client_message.to_byte_array
            Thread.current['s'] ||= Clients::Client.connect_udt
            result = Clients::Client.send_udt(Thread.current['s'],Thread.current['bytes'])
            ClientMessage.parse_from(result)
          end
        end

        it "stress with large payload" do
          measure(10,10000) do
            Thread.current['bytes'] ||= client_message.to_byte_array
            Thread.current['s'] ||= Clients::Client.connect_udt
            result = Clients::Client.send_udt(Thread.current['s'],Thread.current['bytes'])
            ClientMessage.parse_from(result)
          end
        end

      end
    end

  end
end

