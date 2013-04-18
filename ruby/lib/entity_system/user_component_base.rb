module EntitySystem
  class UserComponentBase

    def self.update_user_components(user_components_data)
      user_components_data.each do |user_component_data|
        update_user_component(user_component_data)
      end
    end

    def self.update_user_component(user_component_data)
      component = load_component_by_name(delta[:name])

      # Ephemeral components are temporary, usually come from the client, and are not trusted or persisted
      # They are what we use to fit game request data into the component system.
      if component.ephemeral?
        
      end

      #  Not sure if such a thing will ever exist, but just for the sake of example we might have user components where we take
      #  what the user submits verbatim.
      if component.user_modifiable?

      end
    end

    def self.load_component_by_name(name)

    end

  end
end
