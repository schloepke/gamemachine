
if ['development','test'].include?(GameMachine.env)
  require_relative 'protobuf/game_messages'
  require_relative 'protobuf/generate'
end
