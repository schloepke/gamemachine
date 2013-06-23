module GameMachine
  class SystemMonitor < GameActor

    def post_init(*args)
    end

    def preStart
      @cluster_members = {}
      getContext.system.eventStream.subscribe(getSelf, JavaLib::DeadLetter.java_class)
      if getContext.system.name == 'cluster'
        cluster = JavaLib::Cluster.get(getContext.system)
        cluster.subscribe(getSelf, JavaLib::ClusterEvent::ClusterDomainEvent.class)
      end
    end

    def on_receive(message)
      if message.is_a?(JavaLib::DeadLetter)
        GameMachine.logger.warn("DeadLetter #{message.message}")
      elsif message.is_a?(JavaLib::ClusterEvent::SeenChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        #puts message.member.address
        #puts message.member.status
        @cluster_members[message.member.address] = message.member
      elsif message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::CurrentClusterState)
        GameActor.find('blah').send_message('blah')
      else
        GameMachine.logger.warn("Unrecognized message #{message}")
      end
    end
  end
end
