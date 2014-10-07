module GameMachine
  module GCache
    class CacheStats

      def initialize(delegate)
        @delegate = delegate
      end

      def average_load_penalty
        @delegate.average_load_penalty
      end

      def eviction_count
        @delegate.eviction_count
      end

      def hit_count
        @delegate.hit_count
      end

      def hit_rate
        @delegate.hit_rate
      end

      def load_count
        @delegate.load_count
      end

      def load_exception_count
        @delegate.load_exception_count
      end

      def load_exception_rate
        @delegate.load_exception_rate
      end

      def load_success_count
        @delegate.load_success_count
      end

      def miss_count
        @delegate.miss_count
      end

      def miss_rate
        @delegate.miss_rate
      end

      def request_count
        @delegate.request_count
      end

      def total_load_time
        @delegate.total_load_time
      end

      def to_s
        @delegate.to_string
      end

      def inspect
        to_s
      end
    end
  end
end