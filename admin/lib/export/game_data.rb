module Export
  class GameData

    def self.entity_components
      ::GameData::Entity.reflect_on_all_associations(:has_many).select do |as|
        !as.name.match(/^entity/)
      end.map(&:name)
    end

    def publish
      write_game_data(to_yaml) 
    end

    def to_yaml
      to_hash.to_yaml
    end

    def to_hash
      {:entities => entities_to_hash, :components => components_to_hash}
    end

    private

    def filtered_attributes(attributes)
      attributes.reject do |attribute|
        attribute == 'created_at' ||
          attribute == 'updated_at'
      end
    end

    def entities_to_hash
      [].tap do |entities|
        ::GameData::Entity.all.each do |entity|
          attributes = filtered_attributes(entity.attributes)
          self.class.entity_components.each do |rel|
            attributes[rel] = entity.send(rel).map do |e|
              filtered_attributes(e.attributes)
            end
          end
          entities << attributes
        end
      end
    end

    def components_to_hash
      {}.tap do |components|
        self.class.entity_components.each do |name|
          klass_name = name.to_s.classify
          component_klass = "GameData::#{klass_name}".constantize
          components[name] = component_klass.all.map do |c|
            filtered_attributes(c.attributes)
          end
        end
      end
    end

    def game_data_file
      File.join(Rails.root,'../server','config','game_data.yml')
    end

    def write_game_data(data)
      File.open(game_data_file,'w') {|f| f.write(data)}
    end

  end
end
