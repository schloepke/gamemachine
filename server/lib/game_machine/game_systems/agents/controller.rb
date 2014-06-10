module GameMachine
  module GameSystems
    module Agents

      class Controller < Actor::Base

        attr_reader :agent_config, :current_agents, :address, :children
        def post_init(*args)
          @agent_config = args.first
          @agent_config.load!
          @address = local_address
          @current_agents = local_agents
          @children = {}
          create_agents(current_agents)
          schedule_message('agent_tick',100)
          schedule_message('check_config',10000)
          ClusterMonitor.find.tell('register_observer',get_self)
        end

        def on_receive(message)

          if message.is_a?(String)
            if message == 'agent_tick'
              @children.each do |name,actor_ref|
                actor_ref.tell('update',get_self)
              end
            elsif message == 'check_config'
              if agent_config.reload?
                agent_config.load!
                update_agents
              end
            elsif message == 'cluster_update'
              update_agents
            end
          end
        end

        def schedule_message(message,update_interval)
          duration = GameMachine::JavaLib::Duration.create(
            update_interval, java.util.concurrent.TimeUnit::MILLISECONDS
          )
          scheduler = get_context.system.scheduler
          dispatcher = get_context.system.dispatcher
          scheduler.schedule(duration, duration, get_self, message, dispatcher, nil)
        end

        def update_agents
          agents_to_create = {}
          agents_to_destroy = {}
          updated_agents = local_agents

          # agents no longer under our control
          current_agents.each do |name,klass|
            unless updated_agents.has_key?(name)
              agents_to_destroy[name] = klass
            end
          end

          # new agents that need creating
          updated_agents.each do |name,klass|
            unless current_agents.has_key?(name)
              agents_to_create[name] = klass
            end
          end

          @current_agents = updated_agents
          destroy_agents(agents_to_destroy)
          create_agents(agents_to_create)
        end

        def destroy_agents(agents)
          agents.map {|agent_name,klass| destroy_child(agent_name)}
        end

        def create_agents(agents)
          agents.map {|agent_name,klass| create_child(agent_name,klass)}
        end

        def destroy_child(agent_name)
          if @children.has_key?(agent_name)
            tell_child(agent_name,JavaLib::PoisonPill.get_instance)
            @children.delete(agent_name)
            GameMachine.logger.info "Agent #{agent_name} killed"
          else
            GameMachine.logger.info "Agent #{agent_name} not found, unable to kill"
          end
        end

        def create_child(agent_name,klass_name)
          klass = klass_name.constantize
          name = child_name(agent_name)
          builder = Actor::Builder.new(klass,agent_name)
          child = builder.with_parent(context).with_name(name).start
          @children[agent_name] = Actor::Ref.new(child,klass.name)
          GameMachine.logger.info "Agent #{agent_name} created"
        end

        def tell_child(agent_name,message)
          name = child_name(agent_name)
          @children[agent_name].tell(message,nil)
        end

        def child_name(agent_name)
          "agent_#{agent_name}"
        end

        def local_address
          name = AppConfig.instance.config.name
          Akka.address_for(name)
        end

        def local_agents
          {}.tap do |agents|
            agent_config.agent_names.each do |name|
              bucket = Akka.instance.hashring.bucket_for(name)
              if bucket == address
                agents[name] = agent_config.klass_for_name(name)
              end
            end
          end
        end

      end

    end
  end
end
