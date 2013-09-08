class CreateCombatAbilities < ActiveRecord::Migration
  def change
    create_table :combat_abilities do |t|
      t.string :name
      t.integer :damage
      t.integer :hit_chance
      t.integer :range
      t.string :ability_type
    end
  end
end
