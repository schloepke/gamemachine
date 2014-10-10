java_import 'javax.tools.DiagnosticCollector'
java_import 'javax.tools.JavaCompiler'
java_import 'javax.tools.JavaFileObject'
java_import 'javax.tools.StandardJavaFileManager'
java_import 'javax.tools.ToolProvider'

require 'digest/md5'

module GameMachine
  module Codeblocks
    class Compiler

      attr_reader :outdir, :package, :code, :username, :classname
      def initialize(username,code)
        @username = username
        @classname = extract_classname(code)
        if classname.nil?
          raise "classname is nil"
        end

        package_part = 'A' + Digest::MD5.hexdigest(username + code + Time.now.to_i.to_s)
        @outdir = File.join(GameMachine.app_root,'tmp',package_part)
        @package = "user.codeblocks.#{package_part}"
        @code = "package #{@package};\n\n#{code}"
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

      def self.testcode
        code = <<EOF
        
        import java.io.File;
        import java.io.IOException;
        import com.game_machine.codeblocks.api.*;

        public class Myclass implements Codeblock {

          public void run(Object message) throws Exception {
            System.out.println("run Test");
            Test.sendMessage("testing");
            System.setProperty("testing","true");
            File file = new File("/tmp/testfile");
            file.createNewFile();
          }
        }
EOF
        
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