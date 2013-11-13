class CreateGameUsers < ActiveRecord::Migration
  def change
    create_table :game_users do |t|
      t.string :user
      t.string :pass
      t.string :roles
      t.timestamps
    end
  end
end
