class CreateProtobufMessages < ActiveRecord::Migration
  def change
    create_table :protobuf_messages do |t|
      t.string :name
    end
  end
end
