class CreateDataEditors < ActiveRecord::Migration
  def change
    create_table :data_editors do |t|

      t.timestamps
    end
  end
end
