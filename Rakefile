

task :default => :build_proto

task :build_proto do
  system("rprotoc java/game_machine/src/main/resources/ClientMessage.proto --out ./ruby/lib")
  mv 'ClientMessage.pb.rb', 'ruby/lib'
  system("protoc java/game_machine/src/main/resources/ClientMessage.proto --java_out=java/game_machine/src/main/java")
end
