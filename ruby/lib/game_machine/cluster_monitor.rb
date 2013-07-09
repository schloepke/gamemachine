module GameMachine
  class ClusterMonitor < Actor

    def self.cluster_members
      @@cluster_members ||= java.util.concurrent.ConcurrentHashMap.new
    end

    def preStart
      if getContext.system.name == 'cluster'
        cluster = JavaLib::Cluster.get(getContext.system)
        Server.instance.init_cluster!(cluster.self_address.to_string)
        cluster.subscribe(getSelf, JavaLib::ClusterEvent::ClusterDomainEvent.java_class)
      end
    end

    def on_receive(message)
      if message.is_a?(JavaLib::ClusterEvent::SeenChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberRemoved)
        Server.instance.hashring.remove_bucket(message.member.address.to_string)
        self.class.cluster_members.delete(message.member.address.to_string)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        self.class.cluster_members[message.member.address.to_string] = message.member
        Server.instance.hashring.add_bucket(message.member.address.to_string)
      elsif message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::CurrentClusterState)
        message.get_members.each do |member|
          self.class.cluster_members[member.address.to_string] = member
          Server.instance.hashring.add_bucket(member.address.to_string)
        end
      else
        #GameMachine.logger.info("Unrecognized message #{message}")
      end
    end
  end
end

