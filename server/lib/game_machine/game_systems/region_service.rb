module GameMachine
  module GameSystems
    class RegionService < Actor::Base
      include GameMachine::Commands
      include GameMachine::Models

      attr_reader :regions


      def self.regions
        if @regions
          @regions
        else
          @regions = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def self.address
        @address
      end

      def self.address=(address)
        @address = address
      end

      def self.region
        @region
      end

      def self.region=(region)
        @region = region
      end

      def post_init(*args)
        cluster = JavaLib::Cluster.get(getContext.system)
        self.class.address = cluster.self_address.to_string
        load_from_config
        update_regions
        schedule_message('update_regions',10,:seconds)
      end

      def update_regions
        self.class.regions.keys.each do |name|
          if region = Region.find(name)
            self.class.regions[name] = region
            if region.server == self.class.address
              self.class.region = name
            end
          end
        end
      end

      def load_from_config
        RegionSettings.regions.each do |name,manager|

          # load even if nil, which can happen.  update will populate it
          region = Region.find(name)
          self.class.regions[name] = region
        end
      end

      def server_hostname(server)
        server.sub('akka.tcp://cluster@','').split(':').first
      end

      def regions_string
        #self.class.regions.select{|name,region| region && !region.server.nil?}.map do |name,region|
        #  "#{region.name}=#{server_hostname(region.server)}"
        #end.join('|')
        self.class.regions..map do |name,region|
          if region
            "#{name}="
          else
            "#{region.name}=#{server_hostname(region.server)}"
          end
        end.join('|')
      end

      def on_receive(message)
        if message.is_a?(String)
          if message == 'update_regions'
            update_regions
          end
        elsif message.is_a?(MessageLib::Entity)
          # TODO fix this, leaving for now until we figure out how complex this needs
          # to be
          if message.id == 'regions'
            regions_msg = MessageLib::Regions.new.set_regions(regions_string)
            entity = MessageLib::Entity.new.set_id(message.player.id).set_regions(regions_msg)
            commands.player.send_message(entity,message.player.id)
          end
        end

      end
    end
  end
end
