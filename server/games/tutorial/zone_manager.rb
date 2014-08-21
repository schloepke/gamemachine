# This class is configured in regions.yml, which is a mapping of regions to classes
# that handle running the region.  In this example we use the same class for both
# of our regions.  Our example regions only vary in which npc's are spawned.
module Tutorial
  class ZoneManager < GameMachine::Actor::Base

    def post_init(*args)
      @region_up = false
    end

    def start_zone(zone)
    end

    def on_receive(message)

      # Region manager pings us every couple of seconds as long as we are alive,
      # it's up to us to determine if we have started the region or not
      if message.is_a?(GameMachine::Models::Region)
        return if @region_up
        @region_up = true

        zone = message.name
        GameMachine.logger.info "#{self.class.name} Starting region #{zone}"
        start_zone(zone)
      end
    end
  end
end
