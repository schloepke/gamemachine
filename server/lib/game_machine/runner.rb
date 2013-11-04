require 'trollop'
module GameMachine
  class Runner

    def self.start(name,cluster=false)
      java_dir = File.join(File.expand_path(Dir.pwd),'java')
      unless File.directory?(java_dir)
        puts "Please run game_machine from your game directory"
        exit 0
      end

      GameMachine.logger.info "Using name #{name}"
      GameMachine::Application.initialize!(name,cluster)
      GameMachine::Application.start
    end

    def self.new_app(dir)
      unless dir
        Trollop::die :new, "new requires install path"
      end
      if File.directory?(dir)
        Trollop::die :new, "install path already exists"
      end

      FileUtils.mkdir dir
      java = File.join(File.dirname(__FILE__), '../java')
      demo = File.join(File.dirname(__FILE__), '../lib/demo')
      demo_rb = File.join(File.dirname(__FILE__), '../lib/demo.rb')
      boot = File.join(File.dirname(__FILE__), '../lib/demo/boot.rb')
      config = File.join(File.dirname(__FILE__), '../config')
      spec_helper = File.join(File.dirname(__FILE__), '../spec/spec_helper.rb')
      guardfile = File.join(File.dirname(__FILE__), '../Guardfile')
      rakefile = File.join(File.dirname(__FILE__), '../Rakefile')
      ruby_version = File.join(File.dirname(__FILE__), '../.ruby-version')
      git_ignore = File.join(File.dirname(__FILE__), '../lib/demo/git_ignore')
      
      lib_dir = File.join(dir,'lib')
      FileUtils.mkdir(File.join(dir,'spec'))
      FileUtils.mkdir(File.join(dir,'lib'))
      FileUtils.cp_r(java,dir)
      FileUtils.cp_r(demo,lib_dir)
      FileUtils.cp(demo_rb,lib_dir)
      FileUtils.cp(boot,dir)
      FileUtils.cp(git_ignore,File.join(dir,'.gitignore'))

      FileUtils.cp(guardfile,dir)
      FileUtils.cp(rakefile,dir)
      FileUtils.cp(ruby_version,dir)
      FileUtils.cp_r(config,dir)
      FileUtils.cp(spec_helper,File.join(dir,'spec','spec_helper.rb'))
    end

  end
end
