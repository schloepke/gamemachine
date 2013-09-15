class CreateAssets < ActiveRecord::Migration
  def change
    create_table :assets do |t|
      t.string :asset
      t.string :name
      t.integer :version, :default => 0
    end
  end
end
