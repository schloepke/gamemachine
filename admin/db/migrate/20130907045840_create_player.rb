class CreatePlayer < ActiveRecord::Migration
  def change
    create_table :players do |t|
      t.string :name
      t.boolean :authenticated
      t.string :authtoken
      t.timestamps
    end
  end
end
