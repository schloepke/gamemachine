class AddAuthtokenToGameUser < ActiveRecord::Migration
  def change
    add_column :game_users, :auth_token, :string
  end
end
