class CreateClusters < ActiveRecord::Migration
  def change
    create_table :clusters do |t|
      t.string :name
      t.string :seeds
      t.integer :user_id
    end
  end
end
