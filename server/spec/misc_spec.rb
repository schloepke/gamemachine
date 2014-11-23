require 'spec_helper_minimal'
require 'benchmark'
require "net/http"
require "uri"
require 'jruby/core_ext'
require 'digest'
module GameMachine

  describe "misc" do 
    let(:entity) do 
      entity = MessageLib::Entity.new
      entity.id = '1'
      player = MessageLib::Player.new
      player.authtoken = 'authorized'
      player.id = '2'
      entity.player = player
      entity
    end

    let(:player_id) {'player'}
    let(:id) {'one'}

    let(:test_object) do
      message = MessageLib::TestObject.new
      message.set_id(id)
      message.set_required_string('testing')
      message.set_fvalue(1.9)
      message.set_bvalue(true)
      message.set_dvalue(3.4)
      message.set_numbers64(555)
    end

    it "angle test" do
      puts PathLib::MeshImporter.angle(0.5,2)
      
    end

    xit "test2" do
      graph = PathLib::MeshImporter.importGrid
      result = graph.find_path(10,10,120,120)
      puts result.result
      
      if result.startNode
        pos = result.startNode.position
        puts "startNode #{pos.toString}"
      end

      if result.endNode
        puts "endNode #{result.endNode.position.toString}"
      end

      if result.result
        puts result.resultPath.getCount
        result.resultPath.nodes.each do |node|
          puts node.position.toString
        end
        pathdata = PathLib::MeshImporter.fromPathResult(result)
        PathLib::MeshImporter.write_path_data(pathdata)
      else
        puts "Path not found"
      end
    end

    xit "test1" do
      graph = PathLib::MeshImporter.import_mesh
      result = graph.find_path(10,10,120,120)
      puts result.result
      
      if result.startNode
        pos = result.startNode.position
        puts "startNode #{pos.toString}"
      end

      if result.endNode
        puts "endNode #{result.endNode.position.toString}"
      end

      if result.result
        puts result.resultPath.getCount
        result.resultPath.nodes.each do |node|
          puts node.position.toString
        end
        pathdata = PathLib::MeshImporter.fromPathResult(result)
        PathLib::MeshImporter.write_path_data(pathdata)
      else
        puts "Path not found"
      end
      puts Benchmark.realtime {
        #result = PathLib::MeshImporter.stresstest(graph,1)
        #puts result.result
      }
      
    end

    xit "byte test" do
      java_import 'io.protostuff.ProtobufOutput'
      tdata = MessageLib::TrackData.new
      tdata.set_x(123.45)
      tdata.set_y(188.43)
      tdata.set_id('player2')
      bytes = tdata.to_byte_array
      puts "trackdata #{bytes.length}"

      entity = MessageLib::Entity.new.set_id('p')
      puts "entity = #{entity.to_byte_array.length}"

      tdata = MessageLib::TrackData.new
      tdata.set_ix(-123)
      bytes = tdata.to_byte_array
      puts "trackdata3 ints #{bytes.length}"
    end

    
    
  end
end
