module GameMachine
  module Console
    class Install

      attr_reader :install_path, :help, :install_source_path, :command
      def initialize(command,argv,install_source_path)
        @command = command
        @install_path = argv.shift
        @install_source_path = install_source_path
        
        puts "Installing from #{install_source_path}"
      end

      def error(reason='')
        puts "Invalid install path #{install_path} (#{reason})"
      end

      def run!
        if install_path.nil? || install_path.empty?
          error("Invalid input")
          return
        end

        if command == 'install' && File.exists?(install_path)
          error("Directory exists")
          return
        end

        if command == 'upgrade' && !File.exists?(install_path)
          error("Installation not found")
          return
        end

        @install_path = File.expand_path(install_path)
        send(command.to_sym)
      end

      def install
        puts "Installing into #{install_path}"
        path = File.expand_path(install_path)
        FileUtils.mkdir(path)
        FileUtils.cp_r(config_source,path)
        FileUtils.cp_r(games_source,path)
        FileUtils.cp_r(java_source,path)
        FileUtils.cp_r(mono_source,path)

        FileUtils.cp_r(web_source,path)
        FileUtils.cp_r(lib_source,path)
        FileUtils.cp_r(bin_source,path)

        FileUtils.cp(gemfile_source,path)
        FileUtils.cp(gemlockfile_source,path)
      end

      def upgrade
        puts "Upgrading installation at #{install_path}"
        path = File.expand_path(install_path)

        puts "Upgrading mono"
        FileUtils.rm_rf(File.join(path,'mono'))
        FileUtils.cp_r(mono_source,path)

        puts 'Upgrading java'
        FileUtils.rm_rf(File.join(path,'java','server'))
        FileUtils.cp_r(java_project_source, File.join(path,'java','server'))

        puts 'Upgrading Gemfile'
        FileUtils.rm_rf(File.join(path,'Gemfile'))
        FileUtils.cp(gemfile_source,path)

        FileUtils.rm_rf(File.join(path,'Gemfile.lock'))
        FileUtils.cp(gemlockfile_source,path)

        puts 'Upgrading lib'
        FileUtils.rm_rf(File.join(path,'lib'))
        FileUtils.cp_r(lib_source,path)

        puts 'Upgrading bin'
        FileUtils.rm_rf(File.join(path,'bin'))
        FileUtils.cp_r(bin_source,path)
        
      end

      def gemfile_source
        File.join(install_source_path,'Gemfile')
      end

      def gemlockfile_source
        File.join(install_source_path,'Gemfile.lock')
      end

      def web_source
        File.join(install_source_path,'web')
      end

      def lib_source
        File.join(install_source_path,'lib')
      end

      def bin_source
        File.join(install_source_path,'bin')
      end

      def mono_source
        File.join(install_source_path,'mono')
      end

      def config_source
        File.join(install_source_path,'config')
      end

      def games_source
        File.join(install_source_path,'games')
      end

      def java_project_source
        File.join(install_source_path,'java','project')
      end

      def java_source
        File.join(install_source_path,'java')
      end

    end
  end
end
