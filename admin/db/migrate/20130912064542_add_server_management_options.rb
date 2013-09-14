class AddServerManagementOptions < ActiveRecord::Migration
  def change
    add_column :servers, :status, :integer, :default => 0
    add_column :servers, :enabled, :boolean, :default => false
  end
end
