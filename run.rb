Thread.abort_on_exception = true
$LOAD_PATH.unshift('bin',(__FILE__))
$LOAD_PATH.unshift('lib',(__FILE__))
require 'game_machine'

Thread.new do
  #GameMachine::Server::SocketServer.run
end

rackup = <<EOF
require 'rubygems'
require 'app'

set :server, 'cgi'
set :port, 8090
set :run, false
set :environment, :production

#run Sinatra::Application
EOF

rackup2 = <<EOF
app = Rack::Builder.app do
      use Rack::CommonLogger
      run lambda { |env| [200, {'Content-Type' => 'text/plain'}, ['OK']] }
end
run app
EOF


GameMachine::Server::Jetty.run(rackup2,{:Port => 8090})
