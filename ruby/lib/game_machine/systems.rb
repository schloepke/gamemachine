module GameMachine
  class Systems

    def self.systems
      @@systems ||= ConcurrentHashMap
    end

    def self.systems_with_component(component)
      systems.fetch(component,[])
    end

    def self.register(system,components)
      components.each do |component|
        systems[component] ||= []
        unless systems[component].include?(system)
          systems[component] << system
        end
      end
    end

  end
end
