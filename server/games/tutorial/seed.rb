
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
    'id' => 'sw',
    'name' => 'Sword',
    'quantity' => 100,
    'harvestable' => 0,
    'icon' => '',
    'weapon' => GameMachine::MessageLib::Weapon.new.set_attack(5).set_delay(3),
    'cost' => GameMachine::MessageLib::Cost.new.set_amount(5).set_currency('gold')
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

items.each do |item|
  player_item = GameMachine::MessageLib::PlayerItem.new
  player_item.set_player_id('global')
  player_item.set_id(item['id'])
  player_item.set_icon(item['icon'])
  player_item.set_name(item['name'])
  player_item.set_quantity(item['quantity'])
  player_item.set_harvestable(item['harvestable'])
  player_item.set_craftable(item['craftable'])
  player_item.set_crafting_resource(item['crafting_resource'])

  ['consumable','weapon','cost'].each do |component|
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


# Craftable items

items = [
  {
    'id' => 'ship',
    'item1' => 'wood',
    'item1_quantity' => 2,
    'item2' => 'iron_ore',
    'item2_quantity' => 2
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
