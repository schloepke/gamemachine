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
            if line.match(/^\s*?persistent_message\s+?(\w+?)\s+?{/)
              message_names << $1
            end
          end
        end
      end

      def create_entity_fields
        [].tap do |fields|
          messages.each do |line|
            if line.match(/^\s*?message\s+?(\w+?)\s+?{/) || line.match(/^\s*?persistent_message\s+?(\w+?)\s+?{/)
              name = $1[0,1].downcase + $1[1..-1]
              fields << "optional #{$1} #{name} = #{starting_field_number};"
              @starting_field_number += 1
            end
          end
        end
      end
    end
  end
end
