module Demo
  class ObjectDbCallbacks
    include GameMachine::ObjectDbProc

      dbproc(:update) do |entity|
        entity
      end

      dbproc(:update2) do |entity|
        entity
      end
  end
end
