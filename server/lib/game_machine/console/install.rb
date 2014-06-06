module GameMachine
  module Console
    class Install

      attr_reader :install_path, :help, :install_source_path
      def initialize(argv,install_source_path)
        @install_source_path = install_source_path
        @install_path = argv.shift
        if argv.find {|v| /--help/ =~ v}
          @help = true
        end
        puts "Installing from #{install_source_path}"
      end

      def error
        puts "Invalid install path #{install_path}"
      end

      def run!
        if help
          show_help
          return
        end

        if install_path.nil? || install_path.empty?
          error
          return
        end

        if valid_install_path?
          install
        else
          error
        end
      end

      def show_help
        puts "new [relative path to install directory]"
      end

      def valid_install_path?
        if File.exists?(install_path)
          false
        else
          true
        end
      end

      def install
        puts "Installing into #{install_path}"
        path = File.expand_path(install_path)
        FileUtils.mkdir(path)
        FileUtils.cp_r(config_source,path)
        FileUtils.cp_r(games_source,path)
        FileUtils.cp_r(java_source,path)
      end

      def config_source
        File.join(install_source_path,'config')
      end

      def games_source
        File.join(install_source_path,'games')
      end

      def java_source
        File.join(install_source_path,'java')
      end

    end
  end
end
