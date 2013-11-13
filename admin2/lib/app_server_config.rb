class AppServerConfig

  attr_reader :app_servers
  def initialize(user)
    @app_servers = user.app_servers
  end

  def export
    config = Hash.new.tap do |config|
      app_servers.map(&:environment).each do |env|
        config[env] = {'servers' => {}}
        app_servers.where(:environment => env).each do |app_server|
          attributes = app_server.attributes
          attributes.delete('name')
          attributes['seeds'] = attributes['seeds'].to_s.split(',')
          config[env]['servers'][app_server.name] = attributes
        end
      end
    end.to_yaml
    write_config(config)
  end

  def write_config(yaml)
    file = File.join(GAME_MACHINE_ROOT,'config','config.yml')
    File.open(file,'w') {|f| f.write(yaml)}
  end


end
