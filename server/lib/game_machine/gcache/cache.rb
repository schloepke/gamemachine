require_relative 'cache_stats'

module GameMachine
  module GCache
    class Cache

      def self.create(opts = Hash.new)
        cache_builder = JavaLib::CacheBuilder.new_builder
        cache_builder = cache_builder.maximum_size(opts[:maximum_size]) if opts.has_key?(:maximum_size)
        cache_builder = cache_builder.initial_capacity(opts[:initial_capacity]) if opts.has_key?(:initial_capacity)
        cache_builder = cache_builder.concurrency_level(opts[:concurrency_level]) if opts.has_key?(:concurrency_level)
        cache_builder = cache_builder.expire_after_write(opts[:expire_after_write_sec], java.util.concurrent.TimeUnit::SECONDS) if opts.has_key?(:expire_after_write_sec)
        cache_builder = cache_builder.expire_after_access(opts[:expire_after_access_sec], java.util.concurrent.TimeUnit::SECONDS) if opts.has_key?(:expire_after_access_sec)
        cache_builder = cache_builder.record_stats if opts.has_key?(:record_stats) && opts[:record_stats] == true
        new(cache_builder.build)
      end

      private_class_method :new

      def initialize(delegate)
        @delegate = delegate
      end

      def get(key, &value_loader)
        value = @delegate.get(ObjectWrapper.new(key), CallableImpl.new(value_loader))
        value == nil ? nil : value.wrapped_object
      end

      def get_if_present(key)
        value = @delegate.get_if_present(ObjectWrapper.new(key))
        value == nil ? nil : value.wrapped_object
      end

      def invalidate(key)
        @delegate.invalidate(ObjectWrapper.new(key))
      end

      def invalidate_all
        @delegate.invalidate_all
      end

      def put(key, value)
        @delegate.put(ObjectWrapper.new(key), ObjectWrapper.new(value))
      end

      def size
        @delegate.size
      end

      def stats
        CacheStats.new(@delegate.stats)
      end

      private

      class CallableImpl
        include java.util.concurrent.Callable

        def initialize(b)
          @b = b
        end

        def call
          ObjectWrapper.new(@b.call)
        end
      end

      # Need this so JRuby doesn't convert certain objects to Java object,
      # i.e. Strings with Encoding::BINARY
      class ObjectWrapper
       
        attr_reader :wrapped_object

        def initialize(wrapped_object)
          @wrapped_object = wrapped_object
        end

        def eql?(other)
          other.is_a?(ObjectWrapper) && wrapped_object.eql?(other.wrapped_object)
        end

        def ==(other)
          other.is_a?(ObjectWrapper) && wrapped_object == other.wrapped_object
        end

        def hash
          wrapped_object.hash
        end
      end
    end
  end
end