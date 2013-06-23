module GameMachine
  class Scheduler < GameActor

    def preStart
      scheduler = get_context.system.scheduler
      dispatcher = get_context.system.dispatcher
      duration = JavaLib::Duration.create(1, java.util.concurrent.TimeUnit::SECONDS)
      cancellable = scheduler.schedule(duration, duration, get_self, "Tick", dispatcher, nil)
    end

    def on_receive(message)
      puts Server.instance.cluster_members.keys.map(&:to_string).inspect
      puts Server.instance.hashring.buckets.inspect
    end
  end
end

