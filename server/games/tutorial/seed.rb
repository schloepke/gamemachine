
if GameMachine::Application.config.orm
  items = [
    {
      'id' => 'hp',
      'name' => 'Healing Potion',
      'quantity' => 100,
      'consumable' => GameMachine::MessageLib::Consumable.new.set_type('health').set_size('small')
    },

    {
      'id' => 'sw',
      'name' => 'Sword',
      'quantity' => 100,
      'weapon' => GameMachine::MessageLib::Weapon.new.set_attack(5).set_delay(3)
    },

    {
      'id' => 'helm',
      'name' => 'Helm',
      'quantity' => 100
    },

    {
      'id' => 'shoes',
      'name' => 'Shoes',
      'quantity' => 100
    }
  ]

  items.each do |item|
    player_item = GameMachine::MessageLib::PlayerItem.new
    player_item.set_id(item['id'])
    player_item.set_name(item['name'])
    player_item.set_quantity(item['quantity'])
    if item['consumable']
      player_item.set_consumable(item['consumable'])
    end
    if item['weapon']
      player_item.set_weapon(item['weapon'])
    end
    unless player_item.orm_save('global')
      puts player_item.ormErrors.inspect
    end
  end
end