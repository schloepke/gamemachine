# This class is configured in regions.yml, which is a mapping of regions to classes
# that handle running the region.  In this example we use the same class for both
# of our regions.  Our example regions only vary in which npc's are spawned.
module Example
  class ZoneManager < GameMachine::Actor::Base

    attr_reader :game_data
    def post_init(*args)
      @game_data = args.first
      @region_up = false
    end

    def start_zone(zone)

      # Start our combat controller
      GameMachine::Actor::Builder.new(CombatController).start

      if zone == 'zone1'
        data = game_data.fetch('zone1')
        spawn_npcs('male',data['male'],Npc)
        spawn_npcs('viking',data['viking'],Npc)
        spawn_npcs('golem',data['golem'],Npc)
        spawn_npcs('worm',data['worm'],AggressiveNpc)
      elsif zone == 'zone2'
        data = game_data.fetch('zone2')
        spawn_npcs('male',data['male'],Npc)
        spawn_npcs('viking',data['viking'],Npc)
        spawn_npcs('golem',data['golem'],Npc)
        spawn_npcs('worm',data['worm'],AggressiveNpc)
      end
    end

    def spawn_npcs(type,count,klass)
      count.times.map {|i| "#{type}_#{i}"}.each_slice(20).each do |group|
        name = Digest::MD5.hexdigest(group.join(''))
        group.each do |npc_name|
          Game.npcs[npc_name] = name
        end
        GameMachine::Actor::Builder.new(NpcGroup,group,klass).with_name(name).start
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
