class CreateSystemStatus < ActiveRecord::Migration
  def change
    create_table :system_statuses do |t|
      t.integer :status, :default => 0
      t.string :resolve_with
      t.string :reason
    end
  end
end
