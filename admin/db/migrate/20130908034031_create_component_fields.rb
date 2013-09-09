class CreateComponentFields < ActiveRecord::Migration
  def change
    create_table :component_fields do |t|
      t.string :name
      t.string :value_type
      t.integer :component_id
    end
  end
end
