
guard 'process', :name => 'GameMachine', :command => 'jruby bin/game_machine --server --name=seed01' do
    watch('restart.txt')
    watch(%r{^games/(.+)\.rb$})
end

guard 'rake', :task => 'java:build' do
  watch('java/src/main/resources/messages.proto')
end
