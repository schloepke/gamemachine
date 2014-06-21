require_relative 'region_settings'
module GameMachine
  module GameSystems
    class RegionManager < Actor::Base
      include GameMachine::Commands

      attr_reader :regions
      def post_init(*args)
        @regions = {}
        RegionSettings.regions.each do |name,servers|
          @regions[name] = servers.first
        end

      end

      def regions_string
        regions.map {|name,server| "#{name}=#{server}"}.join('|')
      end

      def on_receive(message)
        regions_msg = MessageLib::Regions.new.set_regions(regions_string)
        entity = MessageLib::Entity.set_regions(regions_msg)
        commands.player.send_message(entity,message.player.id)
      end

    end
  end
end
