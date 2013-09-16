require 'spec_helper'

module GameMachine
  describe AuthHandlers::Basic do

    subject do
      Application.auth_handler
    end

    before(:each) do
      subject.load_users
    end

    describe "#authorize" do
      it "test user should return true" do
        expect(subject.authorize('test_user','test_pass')).to be_true
      end
      it "bad user should return false" do
        expect(subject.authorize('bad_user','test_pass')).to be_false
      end
    end

    describe "#authtoken_for" do
      it "should return the user authtoken" do
        authtoken = subject.authorize('test_user','test_pass')
        expect(subject.authtoken_for('test_user')).to eq(authtoken)
      end

      it "should return nil for unauthenticated user" do
        expect(subject.authtoken_for('blah')).to be_nil
      end

      it "should return token if preset in game data" do
        expect(subject.authtoken_for('player')).to eq('authorized')
      end

      it "authorization should replace preset authtoken" do
        authtoken = subject.authorize('player','pass')
        expect(subject.authtoken_for('player')).to eq(authtoken)
      end
    end
  end
end
