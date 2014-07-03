require 'spec_helper'
require_relative 'test_agent_config'
require_relative 'test_agent'

module GameMachine
  module GameSystems
    module Agents

      describe Controller do

        let(:agent_config) {TestAgentConfig.new}

        let(:agents) do
          TestAgentConfig.new.data
        end

        subject do
          ref = Actor::Builder.new(Controller,agent_config).with_name('controller_test').test_ref
          ref.underlying_actor
        end

        context "initialization" do

          it "should correctly set current_agents on start " do
            expect(subject.current_agents).to eq agents
          end

          it "should create child agents on start" do
            expect(subject.children.size).to eq 5
          end
        end

        context "receiving messages" do

          describe "receives a check_config message" do
            it "should reload config if reload! is true" do
              subject
              expect(subject.agent_config).to receive(:reload?).and_return(true)
              expect(subject.agent_config).to receive(:load!)
              expect(subject).to receive(:update_agents)
              subject.on_receive('check_config')
            end

            it "should not reload config if reload! is false" do
              subject
              expect(subject.agent_config).to receive(:reload?).and_return(false)
              expect(subject.agent_config).to_not receive(:load!)
              expect(subject).to_not receive(:update_agents)
              subject.on_receive('check_config')
            end
          end

        end

        context "cluster node changes" do

          it "should destroy children that it is no longer responsible for" do
            my_agents = agents.clone
            my_agents.delete(:agent1)
            my_agents.delete(:agent2)
            allow(subject).to receive(:local_agents).and_return(my_agents)
            subject.update_agents
            expect(subject.children.size).to eq 3
            expect(subject.children.has_key?(:agent1)).to be_falsey
            expect(subject.children.has_key?(:agent2)).to be_falsey
          end

          it "should create new children it becomes responsible for" do
            my_agents = agents.clone
            my_agents[:agent6] = 'TestAgent'
            allow(subject).to receive(:agent_config).and_return(my_agents)
            allow(subject).to receive(:local_agents).and_return(my_agents)
            subject.update_agents
            expect(subject.children.size).to eq 6
            expect(subject.children.has_key?(:agent6)).to be_truthy
          end
        end


      end

    end
  end
end
