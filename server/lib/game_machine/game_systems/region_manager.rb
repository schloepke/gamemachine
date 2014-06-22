require_relative 'region_settings'
module GameMachine
  module GameSystems
    class RegionManager < Actor::Base
      include GameMachine::Commands


      def self.servers
        if @servers
          @servers
        else
          @servers = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def self.regions
        if @regions
          @regions
        else
          @regions = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      # Return region that this server is in, if any
      def self.region_for(server)
        servers[server]
      end

      def post_init(*args)
        RegionSettings.regions.each do |name,servers|

          # Placeholder
          server = servers.first
          self.class.regions[name] = server
          self.class.servers[server] = name
        end
      end

      def regions_string
        self.class.regions.map {|name,server| "#{name}=#{server}"}.join('|')
      end

      def on_receive(message)
        regions_msg = MessageLib::Regions.new.set_regions(regions_string)
        entity = MessageLib::Entity.new.set_id(message.player.id).set_regions(regions_msg)
        commands.player.send_message(entity,message.player.id)
        GameMachine.logger.info "#{self.class.name} sent regions to #{message.player.id}"
      end

    end
  end
end
