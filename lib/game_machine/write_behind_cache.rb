module GameMachine
  class WriteBehindCache < Actor::Base

    attr_accessor :write_interval, :max_writes_per_second

    def post_init(*args)
      @write_interval = Settings.write_behind_cache.write_interval
      @max_writes_per_second = Settings.write_behind_cache.max_writes_per_second
      @store = DataStore.instance
      @cache = {}
      @updates = {}
      @queued = []
      @last_write = current_time - (120 * 1000)
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher
      schedule_queue_run
      schedule_queue_stats
    end

    def on_receive(message)
      if message.is_a?(String)
        handle_scheduled_message(message)
      else
        if new_message?(message)
          write(message)
        elsif eligible_for_write?(message)
          write(message)
        end
        set_message(message)
      end
    end

    private

    def last_updated(message)
      @updates.fetch(message.id,nil)
    end

    def new_message?(message)
      @updates.has_key?(message.id) ? false : true
    end

    def eligible_for_write?(message)
      (current_time - last_updated(message)) > write_interval
    end

    def min_time_between_writes
      1000.to_f / max_writes_per_second.to_f
    end

    def current_time
      java.lang.System.currentTimeMillis
    end

    def queue_stats
      if @queued.size > 10
        GameMachine.logger.warn "Queued messages size = #{@queued.size}"
      end
    end

    def check_queue
      return if @queued.empty?
      if message = get_message(@queued.shift)
        write(message)
      end
    end

    def set_message(message)
      @cache[message.id] = message
    end

    def get_message(message_id)
      @cache.fetch(message_id,nil)
    end

    def busy?(message)
      (current_time - @last_write) < min_time_between_writes
    end

    def set_updated_at(message)
      @updates[message.id] = @last_write
    end

    def enqueue(message_id)
      @queued << message_id
    end

    #  If there are items in queue, write one of those and put
    # the current message at the end of the queue
    def swap_if_queued_exists(message)
      if queued_message = get_message(@queued.shift)
        enqueue(message)
        queued_message
      else
        message
      end
    end

    def write(message)
      if busy?(message)
        enqueue(message.id)
      else
        message = swap_if_queued_exists(message)
        @store.set(message.id, message.to_byte_array)
        @last_write = current_time
        set_updated_at(message)
      end
    end

    def handle_scheduled_message(message)
      if message == 'check_queue'
        check_queue
      elsif message == 'queue_stats'
        queue_stats
      end
    end

    def schedule_queue_run
      duration = JavaLib::Duration.create(100, java.util.concurrent.TimeUnit::MILLISECONDS)
      @scheduler.schedule(duration, duration, get_self, "check_queue", @dispatcher, nil)
    end

    def schedule_queue_stats
      duration = JavaLib::Duration.create(10, java.util.concurrent.TimeUnit::SECONDS)
      @scheduler.schedule(duration, duration, get_self, "queue_stats", @dispatcher, nil)
    end
  end
end
