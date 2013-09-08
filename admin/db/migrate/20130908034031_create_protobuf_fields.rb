class CreateProtobufFields < ActiveRecord::Migration
  def change
    create_table :protobuf_fields do |t|
      t.string :name
      t.string :field_type
      t.string :value_type
      t.string :model_field
      t.integer :protobuf_message_id
    end
  end
end
