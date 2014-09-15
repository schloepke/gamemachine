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

      def generate_csharp_code
        protogen_path = File.join(ENV['APP_ROOT'],'mono','bin','csharp','protogen_csharp.exe')
        proto_file = File.join(ENV['APP_ROOT'],'config','combined_messages.proto')
        proto_path = File.join(ENV['APP_ROOT'],'config')
        messages_out_file =  File.join(ENV['APP_ROOT'],'messages.cs')
        
        # Yet another .NET tool that assumes the whole world is windows.  In this case it embeds a 
        # windows protoc.exe, so we have to compile differently on non windows platforms
        if Config::CONFIG['target_os'].match(/mswin/)
          cmd = "#{protogen_path} -i:#{proto_file} -o:#{messages_out_file}"
        else
          bin_out_file =  File.join(ENV['APP_ROOT'],'messages.bin')
          cmd = "protoc -I#{proto_path} #{proto_file} -o#{bin_out_file}; mono #{protogen_path} -i:#{bin_out_file} -o:#{messages_out_file};rm -f #{bin_out_file}"
        end
        return cmd
      end

      def generate_java_code
        protogen = GameMachine::Protobuf::Generate.new(ENV['APP_ROOT'])
        protogen.generate
      end

      def generate_code
        generate_java_code
        generate_csharp_code
      end

      def remove_libs
        FileUtils.rm Dir.glob(File.join(java_lib,'*.jar'))
      end

      def install_libs
        #system("cd #{java_root} && #{gradlew} shadowJar")
        FileUtils.cp_r(File.join(java_root,'build','libs','.'), File.join(java_root,'lib'))
      end

      def build(clean=false)
        if clean
          cmd = "cd #{java_root} && #{gradlew} clean build install_libs"
        else
          cmd = "cd #{java_root} && #{gradlew} build install_libs"
        end
      end

      def run_commands(commands)
        command_str = commands.join(';')
        system(command_str)
      end

      def run!
        commands = []
        if command == 'messages'
          commands << generate_code
          commands << build(false)
          run_commands(commands)
        elsif command == 'clean'
          commands << generate_code
          remove_libs
          commands << build(true)
          run_commands(commands)
        else
          commands << generate_code
          commands << build(false)
          run_commands(commands)
        end
      end

    end
  end
end
