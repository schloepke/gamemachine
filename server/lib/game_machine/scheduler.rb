module GameMachine
  class Scheduler < Actor::Base

    def preStart
      scheduler = get_context.system.scheduler
      dispatcher = get_context.system.dispatcher

      every_second = JavaLib::Duration.create(1, java.util.concurrent.TimeUnit::SECONDS)
      #scheduler.schedule(every_second, every_second, get_self, "every_second", dispatcher, nil)

      every_10_seconds = JavaLib::Duration.create(10, java.util.concurrent.TimeUnit::SECONDS)
      #scheduler.schedule(every_10_seconds, every_10_seconds, get_self, "every_10_seconds", dispatcher, nil)
    end

    def on_receive(message)
      if message == 'every_10_seconds'
        #GameMachine.logger.debug "Cluster members #{ClusterMonitor.cluster_members.keys.to_a.inspect}"
        #GameMachine.logger.debug "Hashrings #{Akka.instance.hashring.buckets.inspect}"
      end
    end
  end
end

