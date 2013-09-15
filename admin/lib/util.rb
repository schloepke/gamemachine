class Util
  def self.component_models
    # must eager load all the classes...
    Dir.glob("#{Rails.root}/app/models/**/*.rb") do |model_path|
      begin
        require model_path
      rescue
        # ignore
      end
    end
    # simply return them
    #Module.constants.select { |c| (eval c).is_a? Class }
    ActiveRecord::Base.send(:subclasses).select do |klass|
      klass.name.match(/GameData::/) &&
      !klass.name.match(/GameData::Entity/)
    end
  end

  def self.component_names_for_view
    component_models.map do |model|
      model.name.sub('GameData::','')
    end
  end

end
