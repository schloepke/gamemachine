require 'spec_helper'

module GameMachine
  describe WriteBehindCache do
 
    let(:entity) do
      MessageLib::Entity.new.set_id('1')
    end

    let(:data_store) do
      double("DataStore", :set => true, :get => nil, :shutdown => true)
    end

    subject do
      props = JavaLib::Props.new(WriteBehindCache);
      ref = JavaLib::TestActorRef.create(Akka.instance.actor_system, props, WriteBehindCache.name);
      ref.underlying_actor.stub(:schedule_queue_run)
      ref.underlying_actor.stub(:schedule_queue_stats)
      ref.underlying_actor.post_init
      ref.underlying_actor
    end

    describe "#on_receive" do

      before(:each) do
        DataStore.stub(:instance).and_return(data_store)
        subject.write_interval = 10
        subject.max_writes_per_second = 100
      end

      context "receives a string message" do
        it "check_queue message should call check_queue" do
          subject.should_receive(:check_queue)
          subject.on_receive('check_queue')
        end

        it "takes one message id from queue and writes it" do
          data_store.should_receive(:set).exactly(2).times
          3.times do |i|
            subject.on_receive(entity.clone.set_id(i.to_s))
          end
          sleep 0.200
          subject.send(:check_queue)
        end

        it "queue_stats message should call queue_stats" do
          subject.should_receive(:queue_stats)
          subject.on_receive('queue_stats')
        end
      end

      context "receives a protobuf message" do

        it "new message should be written to cache" do
          subject.on_receive(entity)
          subject.cache.fetch(entity.id).should == entity
        end

        it "message not eligible for write should be enqueued" do
          subject.on_receive(entity)
          subject.on_receive(entity)
          subject.queue.size.should == 1
        end

        it "saves to store with entity id and entity" do
          data_store.should_receive(:set).with(entity.id,kind_of(java.lang.Object))
          subject.on_receive(entity)
        end

        it "does not save if write on same id happens before write_interval has passed" do
          data_store.should_receive(:set).with(entity.id,kind_of(java.lang.Object)).once
          subject.on_receive(entity)
          subject.on_receive(entity)
        end

        it "sequential writes of different id's should get saved" do
          data_store.should_receive(:set).exactly(4).times
          4.times do |i|
            subject.on_receive(entity.clone.set_id(i.to_s))
            sleep 0.150
          end
        end

        it "sequential writes that exceed max writes per second should be enqueued" do
          subject.write_interval = -1
          subject.max_writes_per_second = 1
          data_store.should_receive(:set).exactly(1).times
          subject.should_receive(:enqueue).exactly(2).times
          3.times do |i|
            subject.on_receive(entity.clone.set_id(i.to_s))
          end
        end

        it "sequential writes of existing messages that exceed write_interval should be enqueued" do
          subject.write_interval = 100000
          subject.max_writes_per_second = -1
          data_store.should_receive(:set).exactly(1).times
          subject.should_receive(:enqueue).exactly(3).times
          4.times do |i|
            subject.on_receive(entity.set_id('11'))
          end
        end
      end

    end
  end
end

