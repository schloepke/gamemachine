class ConfigGenerator

  class << self
    def environments
      Setting.all.map(&:environment)
    end

    def string_to_array(str)
      str.split(',')
    end

    def setting_to_hash(setting)
      attributes = setting.attributes
      attributes['seeds'] = string_to_array(attributes['seeds'])
      attributes
    end

    def servers_to_hash(servers)
      {}.tap do |server_hash|
        servers.each do |server|
          attributes = server.attributes
          server_hash[server.name] = attributes
        end
      end
    end

    def write_config(yaml)
      file = File.join(GAME_MACHINE_ROOT,'config','config.yml')
      File.open(file,'w') {|f| f.write(yaml)}
    end

    def generate
      config_hash = {}
      environments.each do |env|
        config_hash[env] = setting_to_hash(
          Setting.find_by_environment(env)
        )
        config_hash[env]['servers'] = servers_to_hash(
          Server.where(:environment => env)
        )
      end
      write_config(config_hash.to_yaml)
    end

  end
end
