class CreateIsNpc < ActiveRecord::Migration
  def change
    create_table :is_npcs do |t|
      t.boolean :enabled
    end
  end
end
