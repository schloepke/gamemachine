require 'fileutils'

module GameMachine
  module Console
    class Install

      attr_reader :install_path, :help, :install_source_path, :command
      def initialize(argv)
        puts argv
        @plugin = argv.shift
        
      end

      def java_dir
        java_dir = File.join(File.expand_path(Dir.pwd),'java','src','main','java')
      end

      def plugins_dir
        File.join(java_dir,'plugins')
      end

      def plugin_package_dir(name)
        File.join(plugins_dir,name)
        FileUtils.mkdir_p(model_src)
      end

      def templates
        File.join(java_dir,'templates')
      end

      def loader_content
        File.read(File.join(templates,'PluginLoaderTemplate.template'))
      end

      def class_content
        File.read(File.join(templates,'PluginTemplate.template'))
      end

      def loader_out
        File.join(plugins_dir,"#{@plugin}Loader.java")
      end

      def plugin_dir
        File.join(plugins_dir,@plugin.downcase)
      end

      def class_out
        File.join(plugin_dir,"#{@plugin}.java")
      end

      def run
        puts "Creating plugin #{@plugin}"
        if File.exists?(loader_out) || File.exists?(class_out)
          puts "#{@plugin} exists"
          return
        end

        loader = loader_content
        klass = class_content
        loader.gsub!("CLASS",@plugin)
        loader.gsub!("PACKAGE",@plugin.downcase)

        klass.gsub!("PACKAGE",@plugin.downcase)
        klass.gsub!("CLASS",@plugin)

        FileUtils.mkdir_p(plugin_dir)

        File.open(loader_out,'w') {|f| f.write loader}
        File.open(class_out,'w') {|f| f.write klass}
        
      end


    end
  end
end
