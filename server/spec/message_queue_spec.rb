require 'spec_helper'

module GameMachine
  describe MessageQueue do

    let(:topic) {'test topic'}
    let(:message) {'test message'}

    let(:subscribe) do
      MessageLib::Subscribe.new.set_topic(topic)
    end

    let(:unsubscribe) do
      MessageLib::Unsubscribe.new.set_topic(topic)
    end

    let(:publish) do
      publish = MessageLib::Publish.new
      publish.set_topic(topic).set_message(message)
    end

  end
end
