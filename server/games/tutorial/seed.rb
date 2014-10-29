
items = [
  {
    'id' => 'hp',
    'name' => 'Healing Potion',
    'quantity' => -1,
    'consumable' => GameMachine::MessageLib::Consumable.new.set_type('health').set_size('small')
  },

  {
    'id' => 'sw',
    'name' => 'Sword',
    'quantity' => 100,
    'weapon' => GameMachine::MessageLib::Weapon.new.set_attack(5).set_delay(3),
    'cost' => GameMachine::MessageLib::Cost.new.set_amount(5).set_currency('gold')
  },

  {
    'id' => 'helm',
    'name' => 'Helm',
    'quantity' => 1000
  },

  {
    'id' => 'shoes',
    'name' => 'Shoes',
    'quantity' => -1
  },

  {
    'id' => 'gold',
    'name' => 'Gold',
    'quantity' => -1
  }
]

items.each do |item|
  player_item = GameMachine::MessageLib::PlayerItem.new
  player_item.set_player_id('global')
  player_item.set_id(item['id'])
  player_item.set_name(item['name'])
  player_item.set_quantity(item['quantity'])

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
