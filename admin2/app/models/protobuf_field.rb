class ProtobufField < ActiveRecord::Base
  belongs_to :protobuf_message

  validates_presence_of :name
  validates_presence_of :value_type
  validates_presence_of :field_type

  def proto_types
    [
      'double',
      'float',
      'int32',
      'int64',
      'sint32',
      'sint64',
      'fixed32',
      'fixed64',
      'sfixed32',
      'sfixed64',
      'uint32',
      'uint64',
      'bool',
      'string',
      'bytes'
    ]
  end

  def field_type_enum
    ['optional','required','repeated']
  end

  def value_type_enum
    (proto_types + ProtobufMessage.game_messages).uniq
  end
end

