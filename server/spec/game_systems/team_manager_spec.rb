require 'spec_helper'

module GameMachine
  module GameSystems
    include Models

    describe TeamManager do

      let(:actor_ref) {double('Actor::Ref', :tell => true)}

      let(:player_id) {'player'}
      let(:player2) {'player2'}
      let(:team_name) {'team1'}
      let(:team_name2) {'team2'}
      let(:team_members) {[player_id]}
      let(:invite_id) {'invite'}

      let(:team) do
        Team.new(:owner => player_id, :name => team_name, :access => 'public', :members => team_members)
      end

      let(:team_two_members) do
        Team.new(:owner => player_id, :name => team_name2, :access => 'public', :members => [player_id,player2])
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

      let(:start_match) do
        StartMatch.new(:game_handler => nil, :team_names => [team_name,team_name2])
      end

      let(:end_match) do
        EndMatch.new(:match_id => 'team1_team2')
      end

      let(:match) {double('Match')}
      let(:actor_ref) {double('ActorRef', :tell => true)}

      subject do
        ref = Actor::Builder.new(TeamManager).with_name('team_manager_test').test_ref
        ref.underlying_actor
      end

      before(:each) do
        allow_any_instance_of(Commands::PlayerCommands).to receive(:send_message).and_return(true)
        allow_any_instance_of(Commands::MiscCommands).to receive(:client_manager_register).and_return(true)
      end

      describe "#create_team" do

        before(:each) do
          allow(subject).to receive(:send_team_joined).and_return(true)
        end

        it "should create a team" do
          allow(Team).to receive(:find!).and_return(nil)
          expect_any_instance_of(Team).to receive(:save!)
          subject.on_receive(create_team)
        end

        it "should send chat join request" do
          allow(Team).to receive(:find!).and_return(nil)
          expect(subject).to receive(:join_chat).with(team_name,player_id)
          subject.on_receive(create_team)
        end

        it "should not create a team if one exists" do
          allow(Team).to receive(:find!).and_return(team)
          expect(subject.create_team(create_team)).to be_falsey
        end

        it "should create invite id if private team" do
          allow(Team).to receive(:find!).and_return(nil)
          allow(subject).to receive(:team_id).and_return 'team_id'
          expect(Uniqueid).to receive(:generate_token).with(team_name).and_return invite_id
          subject.on_receive(create_private_team)
        end
      end

      describe "#destroy_team" do
        before(:each) do
          allow(Team).to receive(:find!).and_return(team)
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

      describe "#leave_team" do

        before(:each) do
          allow(Team).to receive(:find!).and_return(team)
        end

        it "should destroy team if last member leaves" do
          leave_team.player_id = player_id
          expect(subject).to receive(:destroy_team)
          expect(subject).to receive(:destroy_on_owner_leave?).and_return(false)
          subject.on_receive(leave_team)
        end

        it "should reassign owner if owner leaves" do
          allow(Team).to receive(:find!).and_return(team_two_members)
          leave_team.player_id = player_id
          allow(subject).to receive(:destroy_on_owner_leave?).and_return(false)
          subject.on_receive(leave_team)
          expect(team_two_members.owner).to eql(player2)
        end

        it "should remove member from team" do
          leave_team.player_id = player2
          subject.on_receive(leave_team)
          expect(team.members.include?(player2)).to be_falsey
        end

        it "should destroy team if player is owner" do
          leave_team.player_id = player_id
          expect(subject).to receive(:destroy_team)
          subject.on_receive(leave_team)
        end

        it "should call destroy_player_team with player id" do
          leave_team.player_id = player_id
          expect(subject).to receive(:destroy_player_team).with(player_id)
          subject.on_receive(leave_team)
        end
      end

      describe "#join_team" do

        before(:each) do
          allow(Team).to receive(:find!).and_return(team)
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
          allow(Team).to receive(:find!).and_return(private_team)
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

      describe "#team_invite" do

        it "should forward the invite message to the invitee" do
          allow(Team).to receive(:find!).and_return(team)
          team_invite.player_id = player_id
          expect_any_instance_of(Commands::PlayerCommands).to receive(:send_message)
          subject.on_receive(team_invite)
        end

        it "should set the invite id to the team invite id" do
          allow(Team).to receive(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = 'blah'
          expect(team_invite).to receive("invite_id=").with('blah')
          subject.on_receive(team_invite)
        end
      end

      describe "team_accept_invite" do

        it "should add member to team with correct invite id" do
          allow(Team).to receive(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = invite_id
          expect(subject).to receive(:join_team)
          subject.on_receive(team_accept_invite)
        end

        it "should not add member to team with incorrect invite id" do
          allow(Team).to receive(:find!).and_return(team)
          team_invite.player_id = player_id
          team.invite_id = 'bad invite'
          expect(subject).to_not receive(:join_team)
          subject.on_receive(team_accept_invite)
        end
      end

      describe "#start_match" do
        it "should create the match" do
          allow(Team).to receive(:find!).with('team1').and_return(team)
          allow(Team).to receive(:find!).with('team2').and_return(team_two_members)
          expect(match).to receive(:save)
          expect(Match).to receive(:new).
            with(
              :id => 'team1_team2',
              :teams => [team,team_two_members],
              :server => 'localhost',
              :game_handler => nil
          ).and_return(match)
          subject.on_receive(start_match)
        end

        it "should save teams with match id" do
          allow(Team).to receive(:find!).with('team1').and_return(team)
          allow(Team).to receive(:find!).with('team2').and_return(team_two_members)
          expect(team).to receive(:match_id=).with('team1_team2')
          expect(team_two_members).to receive(:match_id=).with('team1_team2')
          subject.on_receive(start_match)
        end
      end

      describe "#end_match" do
        it "should destroy the match" do
          allow(match).to receive(:teams).and_return([team,team_two_members])
          expect(Match).to receive(:find!).with('team1_team2').and_return(match)
          expect(match).to receive(:destroy)
          subject.on_receive(end_match)
        end
      end

    end
  end
end
