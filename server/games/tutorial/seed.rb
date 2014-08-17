
if GameMachine::Application.config.orm
  items = [
    {
      'id' => 'hp',
      'name' => 'Healing Potion',
      'max_quantity' => 1,
      'consumable' => GameMachine::MessageLib::Consumable.new.set_type('health').set_size('small')
    },
    {
      'id' => 'sw',
      'name' => 'Sword',
      'max_quantity' => 1,
      'offensive_stats' => GameMachine::MessageLib::WeaponAttributes.new.set_attack(5).set_delay(3)
    },
    {
      'id' => 'helm',
      'name' => 'Helm',
      'max_quantity' => 1
    },
    {
      'id' => 'shoes',
      'name' => 'Shoes',
      'max_quantity' => 1
    }
  ]

  items.each do |item|
    player_item_definition = GameMachine::MessageLib::PlayerItemDefinition.new
    player_item_definition.set_id(item['id'])
    player_item_definition.set_name(item['name'])
    player_item_definition.set_max_quantity(item['max_quantity'])
  end
end