class CreateServers < ActiveRecord::Migration
  def change
    create_table :servers do |t|
      t.string  :environment
      t.string  :name
      t.boolean :http_enabled
      t.string  :http_host
      t.integer :http_port
      t.boolean :udp_enabled
      t.string  :udp_host
      t.integer :udp_port
      t.boolean :udt_enabled
      t.string  :udt_host
      t.integer :udt_port
      t.string  :akka_host
      t.integer :akka_port
    end
  end
end
