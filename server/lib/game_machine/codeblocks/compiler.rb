java_import 'javax.tools.DiagnosticCollector'
java_import 'javax.tools.JavaCompiler'
java_import 'javax.tools.JavaFileObject'
java_import 'javax.tools.StandardJavaFileManager'
java_import 'javax.tools.ToolProvider'

require 'digest'

module GameMachine
  module Codeblocks
    class Compiler

      attr_reader :outdir, :package, :code, :username, :classname

      class << self
        def fixture_path
          File.join(ENV['APP_ROOT'],'spec','fixtures')
        end

        def testcode_fail
          File.read(File.join(fixture_path,'codeblocks_fail.java'))
        end

        def testcode_pass
          File.read(File.join(fixture_path,'codeblocks_pass.java'))
        end
      end

      def initialize(username,code)
        @username = username
        @classname = extract_classname(code)
        if classname.nil?
          raise "classname is nil"
        end

        package_part = 'A' + Digest::SHA256.hexdigest(username + code + Time.now.to_i.to_s)
        @outdir = File.join(GameMachine.app_root,'tmp',package_part)
        @package = "user.codeblocks.#{package_part}"
        @code = remove_package(code)
        @code = "package #{@package};\n\n#{@code}"
      end

      def package_path
        package.split('.').join('/')
      end

      def classfile_for(classname)
        File.join(bin_dir,"#{classname}.class")
      end

      def bin_dir
        File.join(outdir,package_path)
      end

      def remove_package(code)
        code.lines.select {|line| !line.match(/^\s*?package/)}.join('')
      end

      def extract_classname(code)
        klass = nil
        io = StringIO.new(code)
        io.each_line do |line|
          if line.match(/^\s*?public\s+?class\s+?(\w+?)\s+?implements\s+?Codeblock/)
            klass = $1
          end
        end
        klass
      end

      def classpath
        File.join(GameMachine.java_root,'lib',"game_machine-#{GameMachine::VERSION}.jar")
      end

      def compile
        result = JavaLib::CodeblockCompiler.memory_compile(classpath,code,"#{package}.#{classname}")
        if result.is_compiled
          JavaLib::CodeblockCompiler.load_from_memory(result)
        else
          result.get_errors.each {|error| puts error}
          nil
        end
      end

      def compile_file
        java.lang.System.setProperty("java.class.path",classpath)
        FileUtils.mkdir_p outdir
        filename = File.join(outdir,"#{classname}.java")
        File.open(filename,'w') {|f| f.write(code)}
        if JavaLib::CodeblockCompiler.compile(outdir,filename)
          classfile = classfile_for(classname)
          if File.exists?(classfile)
            puts "#{classfile} generated"
            JavaLib::CodeblockCompiler.tryload(outdir,"#{package}.#{classname}")
          else
            puts "#{classfile} not found"
            nil
          end
        end
      end

    end
  end
end