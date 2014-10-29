module GameMachine
  class HttpHelper

    def client_auth_response(token)
      config = AppConfig.instance.config
      response = {
        :authtoken => token,
        :protocol => config.client.protocol
      }

      if config.client.protocol == 'TCP'
        response['tcp_host'] = config.tcp.host
        response['tcp_port'] = config.tcp.port
      elsif config.client.protocol == 'UDP'
        response['udp_host'] = config.udp.host
        response['udp_port'] = config.udp.port
      end
      JSON.generate(response)
    end

    def clusterinfo
      info = {}
      info[:members] = {}
      GameMachine::ClusterMonitor.cluster_members.keys.each do |key|
        member = GameMachine::ClusterMonitor.cluster_members[key]
        info[:members][key] = {:address => member.address, :status => member.status}
      end
      info[:self_address] = GameMachine::Akka.instance.address

      JSON.generate(info)
    end

  end
end