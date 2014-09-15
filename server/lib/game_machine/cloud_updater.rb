module GameMachine
  class CloudUpdater < Actor::Base

    attr_reader :id, :hostname, :port, :node_status
    def post_init(*args)

      @port = AppConfig.instance.config.http.port
      @hostname = AppConfig.instance.config.http.host
      if ENV['CONTAINER_ID']
        @id = ENV['CONTAINER_ID']
      else
        @id = Akka.instance.address
      end
      @node_status = MessageLib::NodeStatus.new.set_id(id).set_hostname(hostname).set_port(port)
      schedule_message('update',5,:seconds)
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'update'
          node_status.set_last_updated(Time.now.to_i)
          node_status.store_set('node_status')
        end
      end
    end
  end
end