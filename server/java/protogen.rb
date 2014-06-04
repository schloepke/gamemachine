
require 'local_lib/protostuff-compiler-1.0.7-jarjar.jar'
require '../lib/game_machine/protobuf/game_messages.rb'
require '../lib/game_machine/protobuf/generate.rb'
user_dir = File.join(Dir.pwd,'../')
protogen = GameMachine::Protobuf::Generate.new(user_dir)
protogen.generate
