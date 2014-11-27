require_relative 'spec_helper'
#java_import 'io.gamemachine.client.messages.GridData'
java_import 'io.gamemachine.pathfinding.GridGraph'
describe 'Misc' do

  it "test1" do
    GridGraph.useDiagonals = true
    GridGraph.heuristic = GridGraph::Heuristic::MANHATTAN2D
    graph = MeshImporter.importGrid
    puts Benchmark.realtime {
      MeshImporter.stresstest(graph,10,10,120,120,false,false,5000)
    }
    return

    result = graph.find_path(10,10,260,220,false,true)
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
      pathdata = MeshImporter.fromPathResult(result)
      MeshImporter.write_path_data(pathdata)
    else
      puts "Path not found #{result.error}"
    end
  end
end