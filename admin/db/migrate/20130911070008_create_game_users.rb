class CreateGameUsers < ActiveRecord::Migration
  def change
    create_table :game_users do |t|
      t.string :username
      t.string :password
      t.string :roles
      t.timestamps
    end
  end
end
