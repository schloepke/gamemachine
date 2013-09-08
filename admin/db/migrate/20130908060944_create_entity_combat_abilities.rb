class CreateEntityCombatAbilities < ActiveRecord::Migration
  def change
    create_table :entity_combat_abilities do |t|
      t.integer :entity_id
      t.integer :combat_ability_id
    end
  end
end
