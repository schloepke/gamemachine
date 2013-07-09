guard :shell do
  watch('../core/src/main/resources/messages.proto') do |m|
    n m[0], 'Changed'
    `say -v cello #{m[0]}`
  end
end

# Add files and commands to this file, like the example:
#   watch(%r{file/path}) { `command(s)` }
#
guard 'shell' do
  watch(/(.*).txt/) {|m| `tail #{m[0]}` }
end
