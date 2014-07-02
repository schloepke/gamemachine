require 'spec_helper'

module GameMachine
  module GameSystems
    include Models

    describe TeamManager do

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      let(:player_id) {'player'}
      let(:player2) {'player2'}
      let(:team_name) {'team1'}
      let(:team_members) {[player_id]}
      let(:invite_id) {'invite'}

      let(:team) do
        Team.new(:owner => player_id, :name => team_name, :access => 'public', :members => team_members)
      end

      let(:team_invite) do
        TeamInvite.new(:name => team_name, :invitee => player_id)
      end

      let(:team_accept_invite) do
        TeamAcceptInvite.new(:name => team_name, :invitee => player2, :invite_id => invite_id)
      end

      let(:private_team) do
        Team.new(:owner => player_id, :name => team_name, :access => 'private', :members => team_members)
      end

      let(:destroy_team) do
        DestroyTeam.new(:name => team_name)
      end

      let(:leave_team) do
        LeaveTeam.new(:name => team_name)
      end

      let(:join_team) do
        JoinTeam.new(:name => team_name)
      end

      let(:create_team) do
        CreateTeam.new(:owner => player_id, :name => team_name, :access => 'public')
      end

      let(:create_private_team) do
        CreateTeam.new(:owner => player_id, :name => team_name, :access => 'private')
      end

        let(:actor_ref) {double('ActorRef', :tell => true)}

      subject do
        ref = Actor::Builder.new(TeamManager).with_name('team_manager_test').test_ref
        ref.underlying_actor
      end

      before(:each) do
        Commands::PlayerCommands.any_instance.stub(:send_message).and_return(true)
      end

      describe "create_team" do

        before(:each) do
          subject.stub(:send_team_joined).and_return(true)
        end

        it "should create a team" do
          Team.stub(:find!).and_return(nil)
          expect_any_instance_of(Team).to receive(:save!)
          subject.on_receive(create_team)
        end

        it "should send chat join request" do
          Team.stub(:find!).and_return(nil)
          expect(subject).to receive(:join_chat).with(team_name,player_id)
          subject.on_receive(create_team)
        end

        it "should not create a team if one exists" do
          Team.stub(:find!).and_return(team)
          expect(subject.create_team(create_team)).to be_false
        end

        it "should create invite id if private team" do
          Team.stub(:find!).and_return(nil)
          subject.stub(:team_id).and_return 'team_id'
          expect(Uniqueid).to receive(:generate_token).with(team_name).and_return invite_id
          subject.on_receive(create_private_team)
        end
      end

      describe "destroy_team" do
        before(:each) do
          Team.stub(:find!).and_return(team)
        end

        it "should delete the team" do
          destroy_team.player_id = player_id
          expect(team).to receive(:destroy!)
          subject.on_receive(destroy_team)
        end

        it "should send a leave_chat for each member" do
          destroy_team.player_id = player_id
          expect(subject).to receive(:leave_chat).with(team.name,player_id)
          subject.on_receive(destroy_team)
        end

        it "should destroy player_team" do
          destroy_team.player_id = player_id
          expect(subject).to receive(:destroy_player_team).with(player_id)
          subject.on_receive(destroy_team)
        end

        it "should not delete the team if request is from non owner" do
          destroy_team.player_id = player2
          expect(team).to_not receive(:destroy!)
          subject.on_receive(destroy_team)
        end
      end

      describe "leave_team" do
        before(:each) do
          Team.stub(:find!).and_return(team)
        end

        it "should remove member from team" do
          leave_team.player_id = player2
          subject.on_receive(leave_team)
          expect(team.members.include?(player2)).to be_false
        end

        it "should destroy team if player is owner" do
          leave_team.player_id = player_id
          expect(team).to receive(:destroy!)
          subject.on_receive(leave_team)
        end

        it "should call destroy_player_team with player id" do
          leave_team.player_id = player_id
          expect(subject).to receive(:destroy_player_team).with(player_id)
          subject.on_receive(leave_team)
        end
      end

      describe "join_team" do

        before(:each) do
          Team.stub(:find!).and_return(team)
        end

        it "should add member to the team" do
          join_team.player_id = player2
          expect(team.members).to receive(:<<).with(player2)
          subject.on_receive(join_team)
        end

        it "should send join_chat request for member" do
          join_team.player_id = player2
          expect(subject).to receive(:join_chat).with(team.name,player2)
          subject.on_receive(join_team)
        end

        it "should not add member to team if access is private" do
          Team.stub(:find!).and_return(private_team)
          expect(subject).to_not receive(:create_player_team)
          subject.on_receive(join_team)
        end

        it "should not join the same player twice" do
          join_team.player_id = player2
          subject.on_receive(join_team)
          expect(team.members).to_not receive(:<<).with(player2)
          subject.on_receive(join_team)
        end

        it "should resend team_joined message" do
          join_team.player_id = player2
          subject.on_receive(join_team)
          expect(subject).to receive(:send_team_joined)
          subject.on_receive(join_team)
        end
      end

      describe "team_invite" do

        it "should forward the invite message to the invitee" do
          Team.stub(:find!).and_return(team)
          team_invite.player_id = player_id
          expect_any_instance_of(Commands::PlayerCommands).to receive(:send_message)
          subject.on_receive(team_invite)
        end

        it "should set the invite id to the team invite id" do
          Team.stub(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = 'blah'
          expect(team_invite).to receive("invite_id=").with('blah')
          subject.on_receive(team_invite)
        end
      end

      describe "team_accept_invite" do

        it "should add member to team with correct invite id" do
          Team.stub(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = invite_id
          expect(subject).to receive(:join_team)
          subject.on_receive(team_accept_invite)
        end

        it "should not add member to team with incorrect invite id" do
          Team.stub(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = 'bad invite'
          expect(subject).to_not receive(:join_team)
          subject.on_receive(team_accept_invite)
        end
      end
    end
  end
end
