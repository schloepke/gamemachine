module GameMachine
  class ClusterMonitor < Actor::Base

    def self.cluster_members
      if @cluster_members
        @cluster_members
      else
        @cluster_members = java.util.concurrent.ConcurrentHashMap.new
      end
    end

    def self.remote_members
      if @remote_members
        @remote_members
      else
        @remote_members = java.util.concurrent.ConcurrentHashMap.new
      end
    end
    
    def self.add_cluster_member(address,member)
      cluster_members[address] = member
    end

    def self.remove_cluster_member(address)
      cluster_members.delete(address)
    end

    def self.remove_remote_member(address)
      remote_members.delete(address)
    end

    def self.add_remote_member(address,member)
      remote_members[address] = member
    end

    def preStart
      if getContext.system.name == 'cluster'
        @cluster = JavaLib::Cluster.get(getContext.system)
        Akka.instance.init_cluster!(@cluster.self_address.to_string)
        @cluster.subscribe(getSelf,
                           JavaLib::ClusterEvent::MemberEvent.java_class,
                           JavaLib::ClusterEvent::UnreachableMember.java_class)
      end
      @observers = []
      @notify_on_up_observers = []
      @notify_on_down_observers = []
    end

    def notify_member_up
      @notify_on_up_observers.each {|i| i[1].tell(i[0],get_self)}
    end

    def notify_member_down
      @notify_on_down_observers.each {|i| i[1].tell(i[0],get_self)}
    end

    def notify_observers
      @observers.each {|observer| observer.tell('cluster_update',get_self)}
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'register_observer'
          @observers << sender
        elsif message == 'notify_on_up'
          @notify_on_up_observers << [message,sender]
        elsif message == 'notify_on_down'
          @notify_on_down_observers << [message,sender]
        end
      elsif message.is_a?(JavaLib::ClusterEvent::SeenChanged)

      elsif message.is_a?(JavaLib::ClusterEvent::MemberRemoved)
        address = message.member.address.to_string
        Akka.instance.hashring.remove_bucket(address)
        self.class.remove_cluster_member(address)
        self.class.remove_remote_member(address)

        notify_observers
        notify_member_down

        GameMachine.logger.info "MemberRemoved #{address}"
      elsif message.is_a?(JavaLib::ClusterEvent::MemberUp)
        address = message.member.address.to_string
        self.class.add_cluster_member(address,message.member)
        Akka.instance.hashring.add_bucket(address)

        unless address == @cluster.self_address.to_string
          self.class.add_remote_member(address,message.member)
        end

        notify_member_up
        notify_observers
        GameMachine.logger.info "MemberUp #{address}"
      elsif message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)

      elsif message.is_a?(JavaLib::ClusterEvent::CurrentClusterState)
        message.get_members.each do |member|
          address = member.address.to_string
          self.class.add_cluster_member(address,member)
          Akka.instance.hashring.add_bucket(address)
          unless address == @cluster.self_address.to_string
            self.class.add_remote_member(address,member)
          end
        end

        notify_observers

      else
        #GameMachine.logger.info("Unrecognized message #{message}")
      end
    end
  end
end

