module GameMachine
  class SystemMonitor < GameActor

    def post_init(*args)
    end

    def preStart
      getContext.system.eventStream.subscribe(getSelf, JavaLib::DeadLetter.java_class)
      if getContext.system.name == 'cluster'
        cluster = JavaLib::Cluster.get(getContext.system)
        cluster.subscribe(getSelf, JavaLib::ClusterEvent::ClusterDomainEvent.java_class)
      end
    end

    def on_receive(message)
      if message.is_a?(JavaLib::DeadLetter)
        GameMachine.logger.warn("DeadLetter #{message.message}")
      elsif message.is_a?(JavaLib::ClusterEvent::SeenChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberRemoved)
        puts "member removed"
        Server.instance.hashring.remove_bucket(message.member.address.to_string)
        Server.instance.cluster_members.delete(message.member.address)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        puts "member up"
        Server.instance.cluster_members[message.member.address] = message.member
        Server.instance.hashring.add_bucket(message.member.address.to_string)
      elsif message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::CurrentClusterState)
        puts "currentclusterstate"
        message.get_members.each do |member|
          Server.instance.cluster_members[member.address] = member
          Server.instance.hashring.add_bucket(member.address.to_string)
        end
      else
        GameMachine.logger.warn("Unrecognized message #{message}")
      end
    end
  end
end
