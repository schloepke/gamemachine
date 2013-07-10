guard :shell do
  watch('java/src/main/resources/messages.proto') do |m|
    n m[0], 'Changed'
    `say -v cello #{m[0]}`
  end
end

