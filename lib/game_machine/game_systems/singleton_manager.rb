module GameMachine
  module GameSystems
    
    class SingletonManager < Actor::Base
      
      aspect %w(CreateSingleton)
      aspect %w(NotifySingleton)
      aspect %w(DestroySingleton)

      def post_init(*args)
        router_count = Settings.singleton_manager.router_count
        @actor_refs = create_singleton_routers(router_count)

        @slice_size = router_count / 10
        @slices = @actor_refs.each_slice(@slice_size).to_a
        update_interval = Settings.singleton_manager.update_interval
        schedule_update(update_interval)
      end

      def create_singleton_routers(router_count)
        GameMachine::Actor::Builder.new(SingletonRouter).distributed(
          router_count
        ).start
      end

      def send_in_slices
        @slices.each do |slice|
          slice.each do |actor_ref|
            actor_ref.tell('update',nil)
          end
          sleep 0.005
        end
      end

      def send_at_once
        @actor_refs.each do |actor_ref|
          actor_ref.tell('update',nil)
        end
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'update'
            send_at_once
          end
        elsif message.has_notify_singleton
          ref = SingletonRouter.find_distributed(message.notify_singleton.id)
          ref.tell(message)
        elsif message.has_create_singleton
          ref = SingletonRouter.find_distributed(message.create_singleton.id)
          ref.tell(message)
        elsif message.has_destroy_singleton
          ref = SingletonRouter.find_distributed(message.destroy_singleton.id)
          ref.tell(message)
        else
          unhandled(message)
        end
      end

      def schedule_update(update_interval)
        duration = GameMachine::JavaLib::Duration.create(update_interval, java.util.concurrent.TimeUnit::MILLISECONDS)
        scheduler = get_context.system.scheduler
        dispatcher = get_context.system.dispatcher
        scheduler.schedule(duration, duration, get_self, "update", dispatcher, nil)
      end

    end
  end
end
