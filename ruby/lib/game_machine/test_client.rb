
module GameMachine


  class TestClientProxy

    include RSpec::Matchers
    attr_reader :config

    def initialize(name,client)
      @data = java.util.concurrent.ConcurrentHashMap.new
      @data[:name] = name
      @data[:client] = client
      @data[:actual] = []
    end

    def ctx=(ctx)
      @data[:ctx] = ctx
    end

    def ctx
      @data[:ctx]
    end

    def actual=(actual)
      @data[:actual] = actual
    end

    def actual
      @data[:actual]
    end

    def client
      @data[:client]
    end

    def name
      @data[:name]
    end

    def wait_for_ctx
      loop do
        @data[:ctx] = TestClient.find(name).ask('ctx',20)
        return if @data[:ctx]
      end
    end

    def ref
      TestClient.find(name)
    end

    def stop
      ref.tell('stop')
      client.shutdown
    end

    def send_to_server(client_message)
      client.send_to_server(client_message.to_byte_array,ctx)
    end

    def do_expect(found,not_found,component)
      unless found
        found = not_found
      end
      expect(found).to eq(component)
      actual.clear
    end

    def clear
      actual.clear
    end

    def entity_with_component(component,&blk)
      found = false
      10.times do
        actual.each do |entity|
          if entity.component_names.include?(component)
            puts "FOUND #{entity}"
            yield entity if block_given?
            found = entity
          end
        end
        return found if found
        sleep 1.100
      end
      found
    end

    def should_receive_component(component,wait=0.20, &blk)
      actual ||= []
      blk.call
      called = false
      found = nil
      not_found = []
      10.times do
        not_found = []
        actual.each do |entity|
          if entity.component_names.include?(component)
            called = true
            found = component
          else
            not_found += entity.component_names
          end
        end
        if called
          do_expect(found,not_found,component)
          return
        end
        sleep wait
      end
    end
  end


  class TestClient < Actor

    class << self
      def start(name,port)
        client = UdtClient.start(name,'localhost',port)
        proxy = TestClientProxy.new(name,client)
        ActorBuilder.new(self,name,port,proxy).with_name(name).start
        proxy.wait_for_ctx
        proxy
      end
    end


    def post_init(*args)
      @name = args[0]
      @port = args[1]
      @proxy = args[2]
      @ctx = nil
    end

    def preStart
      GameMachine.logger.info "#{@name} started"
    end

    def on_receive(message)
      if message.is_a?(JavaLib::DefaultChannelHandlerContext)
        @ctx = message
      elsif message == 'ctx'
        sender.tell(@ctx) if @ctx
      elsif message == 'stop'
        context.stop
      else
        @proxy.actual ||= []
        client_message = ClientMessage.parse_from(message.bytes)
        client_message.get_entity_list.each do |entity|
        GameMachine.logger.info "GOT #{entity.component_names.join(' | ')}"
          @proxy.actual << entity
        end
      end

    end
  end
end
