class AddTcpSettings < ActiveRecord::Migration
  def change
    add_column :servers, :tcp_enabled, :boolean, :default => false
    add_column :servers, :tcp_host, :string
    add_column :servers, :tcp_port, :integer
  end
end
