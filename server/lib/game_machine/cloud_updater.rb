module GameMachine
  class CloudUpdater < Actor::Base

    attr_reader :node_status, :stats, :windows
    def post_init(*args)

      @stats = {}
      @windows = Config::CONFIG['target_os'].match(/mswin/)

      if ENV['CONTAINER_ID'] && ENV['CLUSTER_NAME']
        @node_status = MessageLib::NodeStatus.new.
          set_container_id(ENV['CONTAINER_ID'].to_i).
          set_hostname(AppConfig.instance.config.http.host).
          set_port(AppConfig.instance.config.http.port).
          set_cluster_name(ENV['CLUSTER_NAME']).
          set_client_count(ClientManager.local_players.size)
      elsif
        @node_status = MessageLib::NodeStatus.new.
          set_container_id(0).
          set_cluster_name("none").
          set_hostname(AppConfig.instance.config.http.host).
          set_port(AppConfig.instance.config.http.port).
          set_client_count(ClientManager.local_players.size)
      end

      schedule_message_once('update',5,:seconds)
      cluster = JavaLib::Cluster.get(getContext.system)
      cluster.subscribe(getSelf, JavaLib::ClusterEvent::ClusterMetricsChanged.java_class)
      
    end

    def on_receive(message)
      if message.is_a?(JavaLib::ClusterEvent::ClusterMetricsChanged)
        message.get_node_metrics.each do |node_metric|
          heap = JavaLib::StandardMetrics.extract_heap_memory(node_metric)
          stats[:heap] = heap.used / 1024 / 1024
          unless windows
            cpu = JavaLib::StandardMetrics.extract_cpu(node_metric)
            if cpu.respond_to?(:systemLoadAverage)
              stats[:load_average] = cpu.systemLoadAverage.get
              stats[:processor_count] = cpu.processors
            end
          end
        end
      elsif message.is_a?(String)
        if message == 'update'
          node_status.set_last_updated(Time.now.to_i)
          node_status.set_client_count(ClientManager.local_players.size)
          node_status.set_heap_used(stats[:heap])
          node_status.set_load_average(stats[:load_average])
          cloud_response = JavaLib::CloudClient.get_instance.update_node_status(node_status)
          if cloud_response.status != 200
            self.class.logger.warn "Update node status returned #{cloud_response.status}"
          end
          schedule_message_once('update',5,:seconds)
        end
      end
    end
  end
end