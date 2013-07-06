module GameMachine
  class Metric < Actor

    class << self

      def measurements
        @@measurements ||= java.util.concurrent.ConcurrentHashMap.new
      end

      def increment(klass_name,name)
        measurements[klass_name] ||= {
          :on_receive => JavaLib::AtomicInteger.new,
          :ask => JavaLib::AtomicInteger.new,
          :tell => JavaLib::AtomicInteger.new
        }
        measurements.fetch(klass_name).fetch(name).get_and_increment
      end

    end
    
    def preStart
      scheduler = get_context.system.scheduler
      dispatcher = get_context.system.dispatcher

      #every_second = JavaLib::Duration.create(1, java.util.concurrent.TimeUnit::SECONDS)
      #scheduler.schedule(every_second, every_second, get_self, "every_second", dispatcher, nil)

      every_10_seconds = JavaLib::Duration.create(10, java.util.concurrent.TimeUnit::SECONDS)
      scheduler.schedule(every_10_seconds, every_10_seconds, get_self, "every_10_seconds", dispatcher, nil)
    end

    def on_receive(message)
      if message == 'every_10_seconds'
        lines = []
        self.class.measurements.keys.each do |key|
          metrics = self.class.measurements[key]
          lines << "#{key}                 : ask=#{metrics[:ask].get} tell=#{metrics[:tell].get} received=#{metrics[:on_receive].get}"
        end
        GameMachine.logger.info "\n\n#{lines.join("\n")}"
      end
    end

  end
end
