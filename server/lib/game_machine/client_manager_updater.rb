
# This actor sends other nodes updates about local players.  All local players are sent when a node joins
# or becomes reachable again.  We also periodically send out a random sample of players to all nodes.  This helps ensure
# all nodes have all players over time even in the case where some updates might get lost due to network issues.
module GameMachine
  class ClientManagerUpdater < Actor::Base

    attr_reader :players, :local_players
    def post_init(*args)
      @local_players = ClientManager.local_players
      @players = ClientManager.get_map('players')
      @cluster = JavaLib::Cluster.get(getContext.system)
      @cluster.subscribe(getSelf, JavaLib::ClusterEvent::MemberEvent.java_class)
      schedule_message('update_remote_members',10 * 1000)
    end

    def cluster_event?(message)
      message.is_a?(JavaLib::ClusterEvent::MemberUp) ||
       message.is_a?(JavaLib::ClusterEvent::ReachableMember)
    end

    def show_stats
      local_player_count = local_players.size
      player_count = players.size
      SystemStats.log_statistic('local_players',local_player_count)
      SystemStats.log_statistic('players',player_count)
      #self.class.logger.info "#{Akka.instance.address} stats: local_players=#{local_player_count} players=#{player_count}"
    end

    def create_client_event(client_id,player_id,event)
      client_event = MessageLib::ClientEvent.new.set_client_id(client_id).
        set_event(event).set_player_id(player_id)
      client_event.set_sender_id(Akka.instance.address + '|' + self.class.name)
    end

    # Ensures player info gets spread around the cluster even
    # in the event of network issues/lost messages
    def update_remote_members(event_type,sample)
      begin
        members = ClusterMonitor.remote_members.values.to_a
        if sample
          members = members.sample(50)
        end
        members.each {|member| update_remote_member(member,event_type) }
      rescue Exception => e
        self.class.logger.error "Error updating remote members #{e.message}"
      end
    end

    def update_remote_member(member,event_type)
      self.class.logger.debug "Updating remote #{member.address.to_string}"
      remote_ref = Actor::Base.find_remote(member.address.to_string,self.class.name)
      
      players.each do |player_id,client_id|
        #send_client_event(client_id,player_id,event_type)
        client_event = create_client_event(client_id,player_id,event_type)
        entity = MessageLib::Entity.new.set_id('0').set_client_event(client_event)
        remote_ref.tell(entity,get_self)
        self.class.logger.debug "Sent #{event_type} for #{player_id}"
      end
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'update_remote_members'
          update_remote_members('update',true)

          show_stats
        end
      elsif cluster_event?(message)
        update_remote_member(message.member,'update')
      else
        unhandled(message)
      end
    end

  end
end