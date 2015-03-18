
def add_items(items,player)
  items.each do |item|
    player_item = GameMachine::MessageLib::PlayerItem.new
    player_item.set_player_id(player)
    player_item.set_id(item['id'])
    player_item.set_health(item['health'])
    player_item.set_max_health(item['max_health'])
    player_item.set_type(item['type'])
    player_item.set_icon(item['icon'])
    player_item.set_weapon(item['weapon'])
    player_item.set_name(item['name'])
    player_item.set_quantity(item['quantity'])
    player_item.set_harvestable(item['harvestable'])
    player_item.set_craftable(item['craftable'])
    player_item.set_crafting_resource(item['crafting_resource'])
    player_item.set_is_consumable(item['is_consumable'])

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


# Item types
# consumable = 50
# wearable = 7
# ships = 2
# general = 10
# houses = 3
# siege weapons = 1
# weapon = 4
# crafting resource = 15
# currency = 20
# internal = 30

items = [
  {
    'id' => 'hp',
    'type' => 50,
    'name' => 'Healing Potion',
    'quantity' => -1,
    'harvestable' => 0,
    'icon' => '',
    'consumable' => GameMachine::MessageLib::Consumable.new.set_type('health').set_size('small'),
     'cost' => GameMachine::MessageLib::Cost.new.set_amount(10).set_currency('silver'),
     'health' => 100,
     'max_health' => 100
  },

   {
    'id' => 'helm',
    'type' => 7,
    'name' => 'Helm',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => 1000,
     'health' => 100,
     'max_health' => 100
  },

  {
      'id' => 'keep_wall',
      'type' => 30,
      'name' => 'keep_wall',
      'harvestable' => 0,
      'icon' => '',
      'quantity' => 1000000,
    'health' => 1000,
    'max_health' => 1000
    },
{
    'id' => 'ship',
    'type' => 2,
    'name' => 'Ship',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'ship',
    'quantity' => 1000000,
  'health' => 1000,
  'max_health' => 1000
  },
  {
    'id' => 'pier',
    'type' => 10,
    'name' => 'Pier',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'pier',
    'quantity' => 1000000,
    'health' => 100,
    'max_health' => 100
  },
  {
    'id' => 'house_large1',
    'type' => 3,
    'name' => 'Large house',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'small_house',
    'quantity' => 1000000,
    'health' => 1000,
    'max_health' => 1000
  },
  {
    'id' => 'wood_shack1',
    'type' => 3,
    'name' => 'Wood shack',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'small_house',
    'quantity' => 1000000,
    'health' => 1000,
    'max_health' => 1000
  },
  {
    'id' => 'catapult',
    'type' => 1,
    'name' => 'Catapult',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'catapult',
    'quantity' => 1000000,
    'is_consumable' => true,
    'health' => 1000,
    'max_health' => 1000
  },

{
    'id' => 'katana',
    'type' => 4,
    'name' => 'Katana',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'katana',
    'quantity' => 1000000,
    'weapon' => true,
    'model_info' => GameMachine::MessageLib::ModelInfo.new.set_attach_x(-0.099).set_attach_y(0.189).
    set_attach_z(-0.024).set_rotate_x(0).set_rotate_y(0).set_rotate_z(-90).set_scale_x(1).set_scale_y(1).set_scale_z(1).
    set_resource("medieval_weapons").set_prefab("Katana").set_weapon_type("1hsword"),
  'health' => 100,
  'max_health' => 100
  },
    
  {
      'id' => 'longbow',
      'type' => 4,
      'name' => 'Longbow',
      'harvestable' => 0,
      'craftable' => 1,
      'icon' => 'longbow',
      'quantity' => 1000000,
      'weapon' => true,
      'model_info' => GameMachine::MessageLib::ModelInfo.new.set_attach_x(-0.096).set_attach_y(-0.004).
    set_attach_z(-0.017).set_rotate_x(30).set_rotate_y(-180).set_rotate_z(0).set_scale_x(1).set_scale_y(1).set_scale_z(1).
      set_resource("medieval_weapons").set_prefab("Long_bow_A").set_weapon_type("bow"),
    'health' => 100,
    'max_health' => 100
    },

  {
    'id' => '2hsword',
    'type' => 4,
    'name' => '2h Sword',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => '2hsword',
    'quantity' => 1000000,
    'weapon' => true,
    'model_info' => GameMachine::MessageLib::ModelInfo.new.set_attach_x(-0.099).set_attach_y(0.189).
    set_attach_z(-0.024).set_rotate_x(0).set_rotate_y(0).set_rotate_z(-90).set_scale_x(1).set_scale_y(1).set_scale_z(1).
    set_resource("medieval_weapons").set_prefab("2H_Sword_A").set_weapon_type("2hsword"),
    'health' => 100,
    'max_health' => 100
  },
  {
  'id' => 'staff',
  'type' => 4,
    'name' => 'Staff',
    'harvestable' => 0,
    'craftable' => 1,
    'icon' => 'staff',
    'quantity' => 1000000,
    'weapon' => true,
    'model_info' => GameMachine::MessageLib::ModelInfo.new.set_attach_x(-0.099).set_attach_y(0.189).
    set_attach_z(-0.024).set_rotate_x(0).set_rotate_y(0).set_rotate_z(-90).set_scale_x(1).set_scale_y(1).set_scale_z(1).
    set_resource("medieval_weapons").set_prefab("Mage_Staff").set_weapon_type("staff"),
    'health' => 100,
    'max_health' => 100
  },

  {
    'id' => 'shoes',
    'type' => 7,
    'name' => 'Shoes',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1,
    'health' => 100,
    'max_health' => 100
  },

  {
    'id' => 'iron_ore',
    'type' => 15,
    'name' => 'Iron Ore',
    'harvestable' => 1,
    'icon' => 'iron_ore',
    'quantity' => 1000000,
    'crafting_resource' => 1,
    'health' => 100,
    'max_health' => 100
  },

  {
    'id' => 'good_rock',
    'type' => 15,
    'name' => 'Good Rock',
    'harvestable' => 1,
    'icon' => 'good_rock',
    'quantity' => 1000000,
    'crafting_resource' => 1,
    'health' => 100,
    'max_health' => 100
  },

{
    'id' => 'wood',
    'type' => 15,
    'name' => 'Wood',
    'harvestable' => 1,
    'icon' => 'wood',
    'quantity' => 1000000,
    'crafting_resource' => 1,
  'health' => 100,
  'max_health' => 100
  },

  {
    'id' => 'gold',
    'type' => 20,
    'name' => 'Gold',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1,
    'health' => 100,
    'max_health' => 100
  },

  {
    'id' => 'copper',
    'type' => 20,
    'name' => 'Copper',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1,
    'health' => 100,
    'max_health' => 100
  },

  {
    'id' => 'silver',
    'type' => 20,
    'name' => 'silver',
    'harvestable' => 0,
    'icon' => '',
    'quantity' => -1,
    'health' => 100,
    'max_health' => 100
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
    'id' => 'pier',
    'item1' => 'wood',
    'item1_quantity' => 30,
    'item2' => 'iron_ore',
    'item2_quantity' => 10
  },
  {
    'id' => 'house_large1',
    'item1' => 'wood',
    'item1_quantity' => 20,
    'item2' => 'iron_ore',
    'item2_quantity' => 5
  },
  {
    'id' => 'wood_shack1',
    'item1' => 'wood',
    'item1_quantity' => 18,
    'item2' => 'iron_ore',
    'item2_quantity' => 5
  },
  {
    'id' => 'catapult',
    'item1' => 'wood',
    'item1_quantity' => 3,
    'item2' => 'iron_ore',
    'item2_quantity' => 1
  },
  {
    'id' => 'katana',
    'item1' => 'iron_ore',
    'item1_quantity' => 2
  },
  {
    'id' => '2hsword',
    'item1' => 'iron_ore',
    'item1_quantity' => 4
  },
  {
    'id' => 'staff',
    'item1' => 'wood',
    'item1_quantity' => 2
  },
  {
      'id' => 'longbow',
      'item1' => 'wood',
      'item1_quantity' => 3
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
          'id' => 'light_attack',
          'name' => 'Light Attack',
          'category' => 'weapon',
          'weapon_type' => '1hsword',
          'damage_type' => 'st',
          'icon' => 'Icon.1_46',
          'resource' => 'stamina',
          'resource_cost' => 20,
          'range' => 5,
        },
  {
        'id' => 'deadly_attack',
        'name' => 'Deadly Attack',
        'category' => 'weapon',
        'weapon_type' => 'bow',
        'damage_type' => 'st',
        'icon' => 'Icon.1_46',
        'resource' => 'stamina',
        'resource_cost' => 1,
        'range' => 20,
      },
  {
      'id' => 'poison_arrow',
      'name' => 'Poison arrow',
      'category' => 'weapon',
      'weapon_type' => 'bow',
      'damage_type' => 'st',
      'icon' => 'Icon.1_46',
      'resource' => 'stamina',
      'resource_cost' => 200,
      'range' => 20,
    },
  {
    'id' => 'poison_blade',
    'name' => 'Poison blade',
    'category' => 'weapon',
    'weapon_type' => '1hsword',
    'damage_type' => 'st',
    'icon' => 'Icon.1_13',
    'resource' => 'magic',
    'resource_cost' => 200,
    'range' => 3,
  },
  {
    'id' => 'cleave',
    'name' => 'Cleave',
    'category' => 'weapon',
    'weapon_type' => '2hsword',
    'damage_type' => 'pbaoe',
    'icon' => 'Icon.1_13',
    'resource' => 'stamina',
    'resource_cost' => 100,
    'range' => 5,
  },

  {
    'id' => 'charge',
    'name' => 'Charge',
    'category' => 'weapon',
    'weapon_type' => '2hsword',
    'damage_type' => 'pbaoe',
    'icon' => 'Icon.1_13',
    'resource' => 'stamina',
    'resource_cost' => 200,
    'range' => 10,
  },

  {
    'id' => 'staff_heal',
    'name' => 'Staff heal',
    'category' => 'weapon',
    'weapon_type' => 'staff',
    'damage_type' => 'aoe',
    'icon' => 'Icon.3_35',
    'resource' => 'magic',
    'resource_cost' => 300,
    'range' => 10,
  },
  {
    'id' => 'fire_field',
    'name' => 'Fire Field',
    'category' => 'weapon',
    'weapon_type' => 'staff',
    'damage_type' => 'aoe',
    'icon' => 'Icon.3_35',
    'resource' => 'magic',
    'resource_cost' => 300,
    'range' => 10,
  },
  {
    'id' => 'lightning_bolt',
    'name' => 'Lightning bolt',
    'category' => 'weapon',
    'weapon_type' => 'staff',
    'damage_type' => 'aoe',
    'icon' => 'Icon.1_12',
    'resource' => 'magic',
    'resource_cost' => 200,
    'range' => 5,
  },
  {
    'id' => 'catapult_explosive',
    'name' => 'Catapult explosive shot',
    'category' => 'weapon',
    'weapon_type' => 'siege',
    'damage_type' => 'aoe',
    'icon' => 'Icon.1_12',
    'resource' => 'stamina',
    'resource_cost' => 200,
    'range' => 15,
  }
  
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
    citem.set_range(item['range'])
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

playerskills = GameMachine::MessageLib::PlayerSkills.new 
playerskills.playerSkill = GameMachine::MessageLib::PlayerSkill.db.where("player_skill_character_id = ?", 'global')
File.open(File.join(ENV['APP_ROOT'],'skills.proto'),'w') {|f| f.write playerskills.to_byte_array}
  
