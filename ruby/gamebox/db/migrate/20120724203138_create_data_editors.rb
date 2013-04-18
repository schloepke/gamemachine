class CreateDataEditors < ActiveRecord::Migration
  def change
    create_table :data_editors do |t|
      t.text      :data
      t.string    :name
      t.timestamps
    end
  end
end
