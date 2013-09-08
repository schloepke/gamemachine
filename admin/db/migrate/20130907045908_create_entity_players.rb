class CreateEntityPlayers < ActiveRecord::Migration
  def change
    create_table :entity_players do |t|
      t.integer :player_id
      t.integer :entity_id
      t.boolean :locked
    end
  end
end
