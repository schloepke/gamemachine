
module GameMachine
  module Server
    ::Protobuf::OPTIONS[:"optimize_for"] = :SPEED
    class Msg < ::Protobuf::Message
      defined_in __FILE__
      required :bytes, :name, 1
    end
  end
end
