require_relative 'region_settings'
module GameMachine
  module GameSystems
    class RegionManager < Actor::Base
      include GameMachine::Commands
      include GameMachine::Models

      attr_reader :regions, :servers
      def post_init(*args)
        @regions = {}
        @servers = {}
        load_from_config
        schedule_message('check_regions',2,:seconds)
      end

      def check_regions
        unassign_down_servers
        assign_servers
        notify_managers
      end

      def notify_managers
        @regions.each do |name,region|
          if region.manager
            region.manager.constantize.find.tell(name)
          end
        end
      end

      def load_from_config
        RegionSettings.regions.each do |name,manager|
          unless region = Region.find(name,5000)
            region = Region.new(
              :name => name,
              :manager => manager
            )
            region.save
          end
          regions[name] = region
        end
      end

      def unassign_down_servers
        regions.each do |name,region|
          if region.server
            unless ClusterMonitor.cluster_members.has_key?(region.server)
              servers.delete(region.server)
              region.server = nil
              region.save
            end
          end
        end
      end

      def assign_servers
        regions.each do |name,region|
          if region.server.nil?
            ClusterMonitor.cluster_members.keys.each do |address|
              unless servers.has_key?(address)
                region.server = address
                servers[address] = name
                region.save
                break
              end
            end
          end
        end
      end

      def server_hostname(server)
        server.sub('akka.tcp://cluster@','').split(':').first
      end

      def regions_string
        regions.select{|name,region| !region.server.nil?}.map do |name,region|
          "#{region.name}=#{server_hostname(region.server)}"
        end.join('|')
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'check_regions'
            check_regions
          else
            regions_msg = MessageLib::Regions.new.set_regions(regions_string)
            entity = MessageLib::Entity.new.set_id(message.player.id).set_regions(regions_msg)
            commands.player.send_message(entity,message.player.id)
            #GameMachine.logger.info "#{self.class.name} sent regions to #{message.player.id}"
          end
        end
      end

    end
  end
end
