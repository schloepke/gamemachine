module GameMachine
  module Navigation
    class Path

      attr_accessor :path, :current_location
      attr_reader :current_point
      def initialize(path,current_location)
        @path = path
        @current_point = path.first
        @current_location = current_location
      end

      def set_path(path)
        @path = path
        @current_point = path.first
      end

      def next_point
        if point_reached?
          @current_point = path.shift
        end
        @current_point
      end

      def point_reached?
        return true if current_point.nil?
        current_location.distance(current_point) < 1
      end
    end
  end
end
