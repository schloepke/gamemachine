require_relative 'spec_helper'
#java_import 'io.gamemachine.client.messages.GridData'
java_import 'io.gamemachine.pathfinding.grid.Graph'
java_import 'io.gamemachine.pathfinding.grid.GridImporter'
java_import 'io.gamemachine.pathfinding.grid.Verticle'
java_import 'io.gamemachine.pathfinding.mesh.MeshImporter'
java_import 'io.gamemachine.pathfinding.Node'
java_import 'io.gamemachine.pathfinding.Util'
java_import 'io.gamemachine.util.Vector3'
java_import 'io.gamemachine.physics.CollisionTest'

describe 'Misc' do

  it "test collisions" do
    puts Benchmark.realtime {
      CollisionTest.run
    }
  end
  
  xit "navmesh" do
    MeshImporter.BuildNavMesh();
  end

  # 81.2,0.9, 158.9
  xit "test1" do
    Graph.useDiagonals = true
    Graph.heuristic = Graph::Heuristic::MANHATTAN2D
    source = java.io.File.new("/home/chris/game_machine/server/java/shared/grid_data.bin")
    graph = GridImporter.new.import_from_file(source)

    startp = Vector3.new(1,1,0.2)
    startp = Vector3.new(76,127,1.03)
    endp = Vector3.new(80.4,166.24,0.279)

    puts Benchmark.realtime {
      Util.stresstest(graph,startp,endp,false,false,5000)
    }
    return

    result = graph.find_path(startp,endp,false,true)
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

    show_full_path = true
    if result.result
      puts result.resultPath.getCount
      if result.resultPath.getCount > 0 && show_full_path
        result.resultPath.nodes.each do |node|
          puts node.position.toString
        end
      end
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