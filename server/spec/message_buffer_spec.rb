require 'spec_helper'
module GameMachine

  describe MessageBuffer do

    let(:message) do
      MessageLib::ClientMessage.new.add_entity(
        MessageLib::Entity.new.set_id('0')
      )
    end

    let(:message_size) {bytes.length - 1}
    let(:bytes) {message.to_prefixed_byte_array }
    let(:bytes_without_prefix) {message.to_byte_array }

    subject {MessageBuffer.new}

    describe "#add_bytes" do
      it "adds bytes to buffer" do
        puts bytes.length
        subject.add_bytes(bytes)
        expect(subject.bytes.length).to eql(bytes.length)
      end

      it "appends to the buffer when called multiple times" do
        subject.add_bytes(bytes)
        subject.add_bytes(bytes)
        subject.add_bytes(bytes)
        expect(subject.bytes.length).to eql(3 * bytes.length)
      end
    end

    describe "#messages" do
      it "returns a message" do
        subject.add_bytes(bytes)
        expect(subject.messages.size).to eql(1)
      end

      it "returns multiple messages" do
        subject.add_bytes(bytes)
        subject.add_bytes(bytes)
        subject.add_bytes(bytes)
        expect(subject.messages.size).to eql(3)
      end

      it "correctly handles partial messages" do
        subject.add_bytes(bytes)
        part = Java::byte[2].new
        part[0] = bytes[0]
        part[1] = bytes[1]
        subject.add_bytes(part)
        expect(subject.messages.size).to eql(1)
        expect(subject.bytes.length).to eq(1)
        expect(subject.next_message_length).to eql(5)
        part = Java::byte[4].new
        part[0] = bytes[2]
        part[1] = bytes[3]
        part[2] = bytes[4]
        part[3] = bytes[5]
        subject.add_bytes(part)
        expect(subject.messages.size).to eql(1)
        expect(subject.bytes).to be_nil
      end
    end

  end
end
