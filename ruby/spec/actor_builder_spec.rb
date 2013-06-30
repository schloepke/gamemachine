require 'spec_helper'

module GameMachine
  describe ActorBuilder do

    let(:actor_class) {Systems::LocalEcho}
    subject do
     ActorBuilder.new(actor_class)
    end

    describe "#with_name" do
      it "should set name" do
      end
    end

  end
end


