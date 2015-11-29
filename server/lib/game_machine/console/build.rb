require 'fileutils'
module GameMachine
  module Console
    class Build

      attr_reader :command, :bundle
      def initialize(argv,bundle=false)
        @bundle = bundle
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

      def copy_process_manager
        source = File.join(java_root,'build','install','server')
        destination = File.join(ENV['APP_ROOT'],'process_manager')
        FileUtils.rm_rf destination
        FileUtils.mv(source,destination)
        FileUtils.cp File.join(ENV['APP_ROOT'],'config','process_manager.conf'), File.join(ENV['APP_ROOT'],'process_manager')
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

      def build(clean=false)
        if clean
          cmd = "cd #{java_root} && #{gradlew} clean assemble install_libs installDist"
        else
          cmd = "cd #{java_root} && #{gradlew} assemble install_libs"
        end
      end

      def bundle_commands
        gm_path = File.join(ENV['APP_ROOT'],'.game_machine')
        FileUtils.mkdir_p(gm_path)
        vendor_path = File.join(gm_path,'vendor','bundle')
        cmd = "cd #{ENV['APP_ROOT']} && bundle install --path=#{vendor_path}"
      end

      def run_commands(commands)
        commands.each do |command|
          system(command)
        end
      end

      def run!
        commands = []

        if bundle
          commands << generate_code
          remove_libs
          commands << build(true)
          commands << bundle_commands
          run_commands(commands)
          bundler_path = File.join(ENV['APP_ROOT'],'.bundle')
          FileUtils.rm_rf(bundler_path)
        else
          if command == 'messages'
            commands << generate_code
            commands << build(false)
            run_commands(commands)
          elsif command == 'compile'
            commands << build(false)
            run_commands(commands)
          elsif command == 'clean'
            commands << generate_code
            remove_libs
            commands << build(true)
            run_commands(commands)
            copy_process_manager
          else
            commands << generate_code
            commands << build(false)
            run_commands(commands)
            copy_process_manager
          end
        end
      end

    end
  end
end
