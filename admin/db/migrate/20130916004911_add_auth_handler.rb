class AddAuthHandler < ActiveRecord::Migration
  def change
    add_column :settings, :auth_handler, :string
  end
end
