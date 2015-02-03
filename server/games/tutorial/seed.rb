
def add_items(items,player)
  items.each do |item|
    player_item = GameMachine::MessageLib::PlayerItem.new
    player_item.set_player_id(player)
    player_item.set_id(item['id'])
    player_item.set_icon(item['icon'])
    player_item.set_weapon(item['weapon'])
    player_item.set_name(item['name'])
    player_item.set_quantity(item['quantity'])
    player_item.set_harvestable(item['harvestable'])
    player_item.set_craftable(item['craftable'])
    player_item.set_crafting_resource(item['crafting_resource'])

    ['consumable','cost','model_info'].each do |component|
      if item[component]
        player_item.send("set_#{component}".to_sym,item[component])
      end
    end

    where = 'player_item_id = ? AND player_item_player_id = ?'
    existing = GameMachine::MessageLib::PlayerItem.db.find_first(where,player_item.id,player_item.player_id)

    unless existing
      unless GameMachine::MessageLib::PlayerItem.db.save(player_item)
        puts player_item.db.dbErrors.inspect
      end
    end
  end
end


items = [
  {
    'id' => 'hp',
    'name' => 'Healing Potion',
    'quantity' => -1,
    'harvestable' => 0,
    'icon' => '',
    'consumable' => GameMachine::MessageLib::Consumable.new.set_type('health').set_size('small'),
     'cost' => GameMachine::MessageLib::Cost.new.set_amount(10).set_currency('silver')
  },

   {
    'id' => 'helm',
    'name' => 'Helm',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => 1000
  },

{
    'id' => 'ship',
    'name' => 'Ship',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'ship',
    'quantity' => 1000000
  },

{
    'id' => 'katana',
    'name' => 'Katana',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'katana',
    'quantity' => 1000000,
    'weapon' => true,
    'model_info' => GameMachine::MessageLib::ModelInfo.new.set_attach_x(-0.099).set_attach_y(0.189).
    set_attach_z(-0.024).set_rotate_x(0).set_rotate_y(0).set_rotate_z(-90).set_scale_x(1).set_scale_y(1).set_scale_z(1).
    set_resource("medieval_weapons").set_prefab("Katana").set_weapon_type("1hsword")
  },

  {
    'id' => 'shoes',
    'name' => 'Shoes',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1
  },

  {
    'id' => 'iron_ore',
    'name' => 'Iron Ore',
    'harvestable' => 1,
    'icon' => 'iron_ore',
    'quantity' => 1000000,
    'crafting_resource' => 1,
  },

  {
    'id' => 'good_rock',
    'name' => 'Good Rock',
    'harvestable' => 1,
    'icon' => 'good_rock',
    'quantity' => 1000000,
    'crafting_resource' => 1,
  },

{
    'id' => 'wood',
    'name' => 'Wood',
    'harvestable' => 1,
    'icon' => 'wood',
    'quantity' => 1000000,
    'crafting_resource' => 1,
  },

  {
    'id' => 'gold',
    'name' => 'Gold',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1
  },

  {
    'id' => 'copper',
    'name' => 'Copper',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1
  },

  {
    'id' => 'silver',
    'name' => 'silver',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1
  }
]

add_items(items,'global')

# Craftable items

items = [
  {
    'id' => 'ship',
    'item1' => 'wood',
    'item1_quantity' => 2,
    'item2' => 'iron_ore',
    'item2_quantity' => 2
  },
  {
    'id' => 'katana',
    'item1' => 'wood',
    'item1_quantity' => 2
  }
]

items.each do |item|
  citem = GameMachine::MessageLib::CraftableItem.new
  citem.set_id(item['id'])
  citem.set_item1(item['item1'])
  citem.set_item1_quantity(item['item1_quantity'])
  citem.set_item2(item['item2'])
  citem.set_item2_quantity(item['item2_quantity'])

  where = 'craftable_item_id = ?'
  existing = GameMachine::MessageLib::CraftableItem.db.find_first(where,citem.id)

  unless existing
    unless GameMachine::MessageLib::CraftableItem.db.save(citem)
      puts citem.db.dbErrors.inspect
    end
  end
end

skills = [
  {
    'id' => 'poison_blade',
    'name' => 'Poison blade',
    'category' => 'weapon',
    'weapon_type' => 'sword',
    'damage_type' => 'st',
    'icon' => 'Icon.1_13',
    'resource' => 'magic',
    'resource_cost' => 20,
    'radius' => 5,
  },
  {
    'id' => 'cleave',
    'name' => 'Cleave',
    'category' => 'weapon',
    'weapon_type' => 'sword',
    'damage_type' => 'pbaoe',
    'icon' => 'Icon.1_13',
    'resource' => 'stamina',
    'resource_cost' => 20,
    'radius' => 8,
  },
  {
    'id' => 'staff_heal',
    'name' => 'Staff heal',
    'category' => 'weapon',
    'weapon_type' => 'staff',
    'damage_type' => 'aoe_heal',
    'icon' => 'Icon.3_35',
    'resource' => 'magic',
    'resource_cost' => 80,
    'radius' => 14,
  },
  {
    'id' => 'lightning_bolt',
    'name' => 'Lightning bolt',
    'category' => 'weapon',
    'weapon_type' => 'staff',
    'damage_type' => 'aoe',
    'icon' => 'Icon.1_12',
    'resource' => 'magic',
    'resource_cost' => 40,
    'radius' => 14,
  },
  
]

characters = GameMachine::JavaLib::Commands.get_characters.map {|c| c.id}
characters << 'global'
characters.each do |char|
  skills.each do |item|
    citem = GameMachine::MessageLib::PlayerSkill.new
    citem.set_id(item['id'])
    citem.set_name(item['name'])
    citem.set_category(item['category'])
    citem.set_weapon_type(item['weapon_type'])
    citem.set_damage_type(item['damage_type'])
    citem.set_icon(item['icon'])
    citem.set_resource(item['resource'])
    citem.set_resource_cost(item['resource_cost'])
    citem.set_radius(item['radius'])
    citem.set_character_id(char)

    where = 'player_skill_id = ? AND player_skill_character_id = ?'
    existing = GameMachine::MessageLib::PlayerSkill.db.find_first(where,citem.id,char)

    unless existing
      unless GameMachine::MessageLib::PlayerSkill.db.save(citem)
        puts citem.db.dbErrors.inspect
      end
    end
  end
end
