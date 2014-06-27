# This class is configured in regions.yml, which is a mapping of regions to classes
# that handle running the region.  In this example we use the same class for both
# of our regions.  Our example regions only vary in which npc's are spawned.
module Example
  class ZoneManager < GameMachine::Actor::Base

    def post_init(*args)
      @region_up = false
    end

    def start_zone(zone)

      # Start our combat controller
      GameMachine::Actor::Builder.new(CombatController).start

      if zone == 'zone1'
        spawn_npcs('male',1000,Npc)
        spawn_npcs('viking',1000,Npc)
        spawn_npcs('golem',1000,Npc)
        spawn_npcs('worm',1000,AggressiveNpc)
      elsif zone == 'zone2'
        spawn_npcs('male',500,Npc)
        spawn_npcs('viking',500,Npc)
        spawn_npcs('golem',500,Npc)
        spawn_npcs('worm',1000,AggressiveNpc)
      end
    end

    def spawn_npcs(type,count,klass)
      count.times.map {|i| "#{type}_#{i}"}.each_slice(20).each do |group|
        name = Digest::MD5.hexdigest(group.join(''))
        group.each do |npc_name|
          Game.npcs[npc_name] = name
        end
        GameMachine::Actor::Builder.new(NpcGroup,group,klass).with_name(name).start
        #name = "#{name}_runner"
        #GameMachine::Actor::Builder.new(NpcGroupRunner,group,klass,name).with_name(name).start
      end
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
