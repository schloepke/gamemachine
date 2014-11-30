require_relative 'spec_helper'
#java_import 'io.gamemachine.client.messages.GridData'
java_import 'io.gamemachine.pathfinding.grid.Graph'
java_import 'io.gamemachine.pathfinding.grid.Import'
java_import 'io.gamemachine.pathfinding.mesh.MeshImporter'
java_import 'io.gamemachine.pathfinding.Node'
java_import 'io.gamemachine.pathfinding.Util'

describe 'Misc' do

  it "navmesh" do
    MeshImporter.BuildNavMesh();
  end
  
  xit "test1" do
    Graph.useDiagonals = true
    Graph.heuristic = Graph::Heuristic::MANHATTAN2D
    source = java.io.File.new("/home/chris/game_machine/server/java/shared/grid_data.bin")
    graph = Import.new.import_from_file(source)
    
    
    puts Benchmark.realtime {
      #Util.stresstest(graph,10,10,60,20,false,false,5000)
    }
    #return

    result = graph.find_path(10,10,60,220,false,true)
    if result.metrics
      puts "visited nodes #{result.metrics.visitedNodes}"
    end
    
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
      puts "Smooth paths #{result.smoothPathCount}"
      if result.smoothPathCount > 0
        result.smoothPath.each do |node|
          puts node.toString
        end
      end
      pathdata = Util.fromPathResult(result)
      Util.write_path_data(pathdata)
    else
      puts "Path not found #{result.error}"
    end
  end
end