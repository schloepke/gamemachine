require 'fileutils'
module GameMachine
  module Console
    class Build

      attr_reader :command
      def initialize(argv)
        @command = argv.shift || ''
      end

      def java_root
        ENV['JAVA_ROOT']
      end

      def java_lib
        File.join(java_root,'lib')
      end

      def gradlew
        File.join(java_root,'gradlew')
      end

      def build_code
        system("cd #{java_root} && #{gradlew} build")
      end

      def generate_code
        protogen = GameMachine::Protobuf::Generate.new(ENV['APP_ROOT'])
        protogen.generate
      end

      def remove_libs
        FileUtils.rm Dir.glob(File.join(java_lib,'*.jar'))
      end

      def install_libs
        system("cd #{java_root} && #{gradlew} install_libs")
      end

      def run!
        if command == 'code'
          build_code
          remove_libs
          install_libs
        elsif command == 'messages'
          generate_code
        else
          generate_code
          build_code
          remove_libs
          install_libs
        end
      end

    end
  end
end
