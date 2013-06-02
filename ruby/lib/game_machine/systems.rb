module GameMachine
  class Systems

    def self.systems
      @@systems ||= ConcurrentHashMap.new
    end

    def self.systems_with_component(component)
      systems[component] || []
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
