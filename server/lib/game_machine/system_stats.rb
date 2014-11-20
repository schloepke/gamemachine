module GameMachine
  class SystemStats < Actor::Base

    def self.log_statistic(name,value,type='static')
      statistic = MessageLib::Statistic.new.set_name(name).set_value(value.to_f).set_type(type)
      SystemStats.find.tell(statistic)
    end

    def post_init(*args)
      if getContext.system.name == 'cluster'
        @cluster = JavaLib::Cluster.get(getContext.system)
      end
      @stats = {}
      @interval = 5
      schedule_message('message_count',@interval,:seconds)
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
        cache_hits = DbLib::DbActor.cacheHits.get
        queue_size = DbLib::WriteBehindCache.queueSize.get
        
        set_count = DbLib::Store.setCount.get
        get_count = DbLib::Store.getCount.get
        delete_count = DbLib::Store.deleteCount.get

        self.class.log_statistic('queue_size',queue_size)
        self.class.log_statistic('dbset',set_count)
        self.class.log_statistic('dbget',get_count)
        self.class.log_statistic('dbdelete', delete_count)
        self.class.log_statistic('db_cachehit',cache_hits)
        
        mps = JavaLib::GameLimits.get_mps_out + JavaLib::GameLimits.get_mps_in
        self.class.log_statistic('mps',mps)

        self.class.log_statistic('mps_in',JavaLib::GameLimits.get_mps_in)

        self.class.log_statistic('mps_out',JavaLib::GameLimits.get_mps_out)

        self.class.log_statistic('bps_out',JavaLib::GameLimits.get_bps_out)

        bps_out = JavaLib::GameLimits.get_bps_out
        mps_out = JavaLib::GameLimits.get_mps_out
        if mps_out > 0 && bps_out > 0
          self.class.log_statistic('bpm', bps_out/mps_out )
        end

        update_statistics
        JavaLib::GameGrid.get_grid_counts
        #puts @stats.inspect
      end
      
    end
  end
end
