module GameMachine
  class SystemMonitor < GameActor

    def post_init(*args)
    end

    def preStart
      @cluster_members = {}
    end

    def on_receive(message)
      if message.is_a?(JavaLib::DeadLetter)
        GameMachine.logger.warn(message.message)
      elsif message.is_a?(JavaLib::ClusterEvent::SeenChanged)
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        puts message.member.address
        puts message.member.status
        @cluster_members[message.member.address] = message.member
      elsif message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
      else
        GameMachine.logger.warn("Unrecognized message #{message}")
      end
    end
  end
end
