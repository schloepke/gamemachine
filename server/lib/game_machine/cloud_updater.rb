module GameMachine
  class CloudUpdater < Actor::Base

    attr_reader :node_status, :stats
    def post_init(*args)

      @stats = {}

      if ENV['CONTAINER_ID'] && ENV['CLUSTER_NAME']
        @node_status = MessageLib::NodeStatus.new.
          set_container_id(ENV['CONTAINER_ID'].to_i).
          set_hostname(AppConfig.instance.config.http.host).
          set_port(AppConfig.instance.config.http.port).
          set_cluster_name(ENV['CLUSTER_NAME']).
          set_client_count(ClientManager.local_players.size)

        schedule_message('update',5,:seconds)
      end

      cluster = JavaLib::Cluster.get(getContext.system)
      cluster.subscribe(getSelf, JavaLib::ClusterEvent::ClusterMetricsChanged.java_class)
      
    end

    def on_receive(message)
      if message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
        message.get_node_metrics.each do |node_metric|
          heap = JavaLib::StandardMetrics.extract_heap_memory(node_metric)
          stats[:heap] = heap.used / 1024 / 1024
          cpu = JavaLib::StandardMetrics.extract_cpu(node_metric)
          if cpu.respond_to?(:systemLoadAverage)
            stats[:load_average] = cpu.systemLoadAverage.get
            stats[:processor_count] = cpu.processors
          end
        end
      end
      if message.is_a?(String)
        if message == 'update'
          node_status.set_last_updated(Time.now.to_i)
          node_status.set_client_count(ClientManager.local_players.size)
          node_status.set_heap_used(stats[:heap])
          node_status.set_load_average(stats[:load_average])
          JavaLib::CloudClient.get_instance.update_node_status(node_status)
        end
      end
    end
  end
end