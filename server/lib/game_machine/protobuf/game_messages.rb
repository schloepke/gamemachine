module GameMachine
  module Protobuf
    class GameMessages

      attr_reader :messages, :starting_field_number
      def initialize(message_file)
        @starting_field_number = 1000
        @messages = IO.readlines(message_file)
      end

      def persistent_messages
        [].tap do |message_names|
          messages.each do |line|
            if line.match(/^\s*?(local_persistent|persistent)_message\s+?(\w+?)\s+?{/)
              message_names << $2
            end
          end
        end
      end

      def create_entity_fields
        [].tap do |fields|
          messages.each do |line|
            if line.match(/^\s*?(persistent)?_?message\s+?(\w+?)\s+?{/)
              name = $2[0,1].downcase + $2[1..-1]
              fields << "optional #{$2} #{name} = #{starting_field_number};"
              @starting_field_number += 1
            end
          end
        end
      end
    end
  end
end
