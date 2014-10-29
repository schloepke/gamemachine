require_relative '../clients/test_client'

module GameMachine
  module Console
    class TestCluster

      def initialize
      end

      def start_node(name)
        node = Clients::TestClient.node_for(name)
        seed_node = Clients::TestClient.node_for(:seed)
        run_node(node,seed_node,name == 'seed')
      end

      def run_node(node,seed_node,is_seed)
        cmd = 'CLUSTER_TEST=1 '
        cmd << "NODE_HOST=#{node[:host]} "
        cmd << "AKKA_PORT=#{node[:akka_port]} "
        cmd << "UDP_PORT=#{node[:udp_port]} "
        cmd << "TCP_PORT=#{node[:tcp_port]} "
        cmd << "WWW_PORT=#{node[:www_port]} "
        unless is_seed
          cmd << "AKKA_SEED_HOST=#{seed_node[:host]} AKKA_SEED_PORT=#{seed_node[:akka_port]} "
        end
        classpath = File.join(ENV['JAVA_ROOT'],'lib/*')
        cmd << "jruby -J-cp \"#{classpath}\" bin/game_machine s &"
        puts cmd
        system(cmd)
      end

      def stop_node(name)
        client = GameMachine::Clients::TestClient.new('test',name)
        client.kill_node
      end

      def ping_node(name)
        client = GameMachine::Clients::TestClient.new('test',name)
        if client.login
          puts "Logged in successfully"
          client.logout
        else
          puts "Unable to login"
        end
      end

      

    end
  end
end