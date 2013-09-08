class CreateProtobufMessage < ActiveRecord::Migration
  def change
    create_table :protobuf_messages do |t|
      t.string :name
      t.string :model_name
    end
  end
end
