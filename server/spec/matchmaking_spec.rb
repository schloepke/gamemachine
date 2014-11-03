require 'spec_helper_minimal'
require 'dentaku'
require 'ostruct'
module GameMachine
  include Models
  

  describe "Matching teams" do

    let(:team1) do
      Team.new(:id => 't1', :owner => 'p1', :name => 't1', :min_for_match => 1, :members => ['p1','p2','p3'])
    end

    let(:team2) do
      Team.new(:id => 't2', :owner => 'p4', :name => 't2', :min_for_match => 1, :members => ['p4','p5','p6'])
    end

    let(:team3) do
      Team.new(:id => 't3', :owner => 'p7', :name => 't3', :min_for_match => 1, :members => ['p7','p8','p9'])
    end

    let(:team4) do
      Team.new(:id => 't4', :owner => 'p10', :name => 't4', :min_for_match => 1, :members => ['p10','p11','p12'])
    end

    let(:team5) do
      Team.new(:id => 't5', :owner => 'p13', :name => 't5', :min_for_match => 10, :members => ['p13','p14','p15'])
    end

    let(:teams1) do
      Teams.new(:teams => [team2,team3,team4,team5],:id => '1')
    end

    let(:teams2) do
      Teams.new(:teams => [team5],:id => '1')
    end

    subject {DefaultHandlers::TeamHandler.new}

    it "should return nil if no match" do
      expect(subject).to receive(:team_rating).with(team1).and_return(5000)
      start_match = subject.match!(teams2,team1)
      expect(start_match).to be_nil
    end

    it "should find closest match 1" do
      expect(subject).to receive(:team_rating).with(team1).and_return(5000)
      expect(subject).to receive(:team_rating).with(team2).and_return(5001)
      expect(subject).to receive(:team_rating).with(team3).and_return(4998)
      expect(subject).to receive(:team_rating).with(team4).and_return(3000)
      expect(subject).to_not receive(:team_rating).with(team5)
       
      start_match = subject.match!(teams1,team1)
      expect(start_match.team_names.last).to eql('t2')
    end

    it "should find closest match 2" do
      expect(subject).to receive(:team_rating).with(team1).and_return(5000)
      expect(subject).to receive(:team_rating).with(team2).and_return(2000)
      expect(subject).to receive(:team_rating).with(team3).and_return(4000)
      expect(subject).to receive(:team_rating).with(team4).and_return(3000)
      expect(subject).to_not receive(:team_rating).with(team5)
       
      start_match = subject.match!(teams1,team1)
      expect(start_match.team_names.last).to eql('t3')
    end

    it "ties pick first match" do
      expect(subject).to receive(:team_rating).with(team1).and_return(5000)
      expect(subject).to receive(:team_rating).with(team2).and_return(1000)
      expect(subject).to receive(:team_rating).with(team3).and_return(1000)
      expect(subject).to receive(:team_rating).with(team4).and_return(1000)
      expect(subject).to_not receive(:team_rating).with(team5)
       
      start_match = subject.match!(teams1,team1)
      expect(start_match.team_names.last).to eql('t2')
    end

  end
end