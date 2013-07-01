module GameMachine
  class WriteBehindCache < Actor

    def post_init(*args)
      @write_interval = 10
      @client = DataStores::Couchbase.instance.client
      @entities = {}
      @updates = {}
      @queued = []
      @last_write = current_time
      @max_writes_per_second = 100
      @min_time_between_writes = 1000.to_f / @max_writes_per_second.to_f
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      schedule_queue_run
      schedule_queue_stats
    end

    def current_time
      java.lang.System.currentTimeMillis
    end

    def schedule_queue_run
      duration = JavaLib::Duration.create(50, java.util.concurrent.TimeUnit::MILLISECONDS)
      @scheduler.schedule(duration, duration, get_self, "check_queue", @dispatcher, nil)
    end

    def schedule_queue_stats
      duration = JavaLib::Duration.create(10, java.util.concurrent.TimeUnit::SECONDS)
      @scheduler.schedule(duration, duration, get_self, "queue_stats", @dispatcher, nil)
    end

    def queue_stats
      if @queued.size > 10
        GameMachine.logger.warn "Queued messages size = #{@queued.size}"
      end
    end

    def check_queue
      return if @queued.empty?
      if entity = get_entity(@queued.shift)
        write(entity)
      end
    end

    def set_entity(entity)
      @entities[entity.id] = entity
    end

    def get_entity(entity_id)
      @entities.fetch(entity_id,nil)
    end

    def busy?
      elapsed = current_time - @last_write
      if elapsed < @min_time_between_writes
        #puts "busy #{elapsed} < #{@min_time_between_writes} (#{current_time} - #{@last_write})"
        true
      else
        false
      end
    end

    def write(message)
      if busy?
        @queued << message.id
        return
      end
      @client.set(message.id, message.to_byte_array)
      @last_write = current_time
      @updates[message.id] = @last_write
      #puts "Write #{message.id}"
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'check_queue'
          check_queue
        elsif message == 'queue_stats'
          queue_stats
        end
        return
      end
      if last_updated = @updates.fetch(message.id,nil)
        time_passed = current_time - last_updated
        if time_passed > @write_interval
          write(message)
        end
      else
        write(message)
      end
      set_entity(message)
    end
  end
end
