require_relative 'npc_actor'

module Demo
  class NpcManager < GameMachine::Actor::Base
    
    aspect %w(CreateNpc)

    def post_init(*args)
      @scheduler = get_context.system.scheduler
      @dispatcher = get_context.system.dispatcher

      update_interval = 100
      @actor_refs = GameMachine::Actor::Builder.new(Demo::NpcActor).distributed(200).start
      GameMachine.logger.info("#{@actor_refs.size} actors")
      @npc_actors = {}
      1500.times do |i|
        create_npc("#{GameMachine::Application.config.akka_port}_#{i}")
      end
      GameMachine.logger.info("Npc's created")

      @duration = GameMachine::JavaLib::Duration.create(update_interval, java.util.concurrent.TimeUnit::MILLISECONDS)
      schedule_update
    end

    def on_receive(message)
      if message.is_a?(String)
        if message == 'update'
          @actor_refs.each {|actor_ref| actor_ref.tell('update',nil)}
        end
      elsif message.has_create_npc
        ref = Demo::NpcActor.find_distributed(message.create_npc.npc.id)
        ref.tell(message)
      end
      GameMachine.logger.debug("NpcManager got #{message}")
    end

    def create_npc(id)
      max = GameMachine::Settings.world_grid_size - 10

      x = rand(max) + 1
      y = rand(max) + 1
      z = 1.10
      entity = Entity.new.set_id('0').set_create_npc(
        CreateNpc.new.set_npc(
          Npc.new.set_id(id).set_transform(
            Transform.new.set_vector3(
              Vector3.new.set_x(x.to_f).set_y(y.to_f).set_z(z.to_f)
            )
          )
        )
      )
      self.class.find.tell(entity)
    end

    def schedule_update
      @scheduler.schedule(@duration, @duration, get_self, "update", @dispatcher, nil)
    end

  end
end
