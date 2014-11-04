module GameMachine
  class SystemStats < Actor::Base

    def self.log_statistic(name,value,type='static')
      statistic = MessageLib::Statistic.new.set_name(name).set_value(value.to_f).set_type(type)
      SystemStats.find.tell(statistic)
    end

    def post_init(*args)
      @last_count = 0
      @last_in_count = 0
      @last_out_count = 0
      if getContext.system.name == 'cluster'
        @cluster = JavaLib::Cluster.get(getContext.system)
      end
      @stats = {}
      @interval = 5
      schedule_message('message_count',@interval,:seconds)
      #schedule_message('update',60,:seconds)
    end

    def update_statistics
      statistics = MessageLib::Statistics.new
      @stats.each do |stat|
        s = stat.flatten
        statistics.add_statistic(MessageLib::Statistic.new.set_type('static').set_name(s[0]).set_value(s[1]))
      end
      CloudUpdater.find.tell(statistics)
    end

    def on_receive(message)
      if message.is_a?(MessageLib::Statistic)
        @stats[message.get_name] = message.get_value
        return
      end

      if message.is_a?(String)
        cache_hits = DbLib::DbActor.cacheHits.incrementAndGet
        cache_hits = DbLib::DbActor.cacheHits.decrementAndGet
        queue_size = DbLib::WriteBehindCache.queueSize.incrementAndGet
        queue_size = DbLib::WriteBehindCache.queueSize.decrementAndGet
        #GameMachine.logger.info "Queue Size: #{queue_size}"
        
        set_count = DbLib::Store.setCount.incrementAndGet
        get_count = DbLib::Store.getCount.incrementAndGet
        delete_count = DbLib::Store.deleteCount.incrementAndGet

        set_count = DbLib::Store.setCount.decrementAndGet
        get_count = DbLib::Store.getCount.decrementAndGet
        delete_count = DbLib::Store.deleteCount.decrementAndGet
        self.class.log_statistic('queue_size',queue_size)
        self.class.log_statistic('dbset',set_count)
        self.class.log_statistic('dbget',get_count)
        self.class.log_statistic('dbdelete', delete_count)
        self.class.log_statistic('db_cachehit',cache_hits)
        #GameMachine.logger.info "Store: set=#{set_count} get=#{get_count} delete=#{delete_count} cache_hits=#{cache_hits}"
        
        current_count = JavaLib::Incoming.messageCount.incrementAndGet
        JavaLib::Incoming.messageCount.decrementAndGet
        diff = current_count - @last_count
        #GameMachine.logger.info "GatewayMessagesPerSecond: #{diff}"
        self.class.log_statistic('gateway_mps',diff / @interval)
        @last_count = current_count

        current_count = NetLib::UdpServerHandler.countIn.incrementAndGet
        NetLib::UdpServerHandler.countIn.decrementAndGet
        diff = current_count - @last_in_count
        #GameMachine.logger.info "MessagesInPerSecond: #{diff}"
        self.class.log_statistic('messages_in_mps',diff / @interval)
        @last_in_count = current_count

        current_count = NetLib::UdpServerHandler.countOut.incrementAndGet
        NetLib::UdpServerHandler.countOut.decrementAndGet
        diff = current_count - @last_out_count
        #GameMachine.logger.info "MessagesOutPerSecond: #{diff}"
        self.class.log_statistic('messages_out_mps',diff / @interval)
        @last_out_count = current_count
        update_statistics
        JavaLib::GameGrid.get_grid_counts
        puts @stats.inspect
      end
      
    end
  end
end
