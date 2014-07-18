
if ['development','test'].include?(GameMachine.env)
  require_relative '../../java/local_lib/protostuff-compiler-1.0.8-jarjar.jar'
  require_relative 'protobuf/game_messages'
  require_relative 'protobuf/generate'
end
