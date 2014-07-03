require 'spec_helper'

module GameMachine
  module Handlers
    
    describe Authentication do

      let(:handler) {double('Handler', :tell => true)}
      let(:actor_ref) {double('ActorRef', :ask => true)}
      let(:player) {MessageLib::Player.new.set_id('player').set_authtoken('authorized')}

      subject do
        Authentication.new
      end

      describe "authenticating a player" do

        context "player is authenticated" do
          it "authenticates player with valid authtoken" do
            expect(subject.authenticate!(player)).to be_truthy
          end
        end
      end

    end
  end
end
