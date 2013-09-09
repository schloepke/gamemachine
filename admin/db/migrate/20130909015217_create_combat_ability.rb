class CreateCombatAbility < ActiveRecord::Migration
  def change
    create_table :entity_combat_abilities do |t|
      t.integer :combat_ability_id
      t.integer :entity_id
    end

    create_table :combat_abilities do |t|

      t.string   :name

      t.integer   :damage

      t.integer   :hit_chance

      t.timestamps
    end
  end
end
