module GameMachine
  class Config

    def self.filename
      File.expand_path(File.join(File.dirname(__FILE__), '../../config.yml'))
    end

    def self.load
      @@config = YAML.load_file(filename)
    end

    def self.config
      @@config
    end

    def self.akka_config
      File.read(File.expand_path(File.join(File.dirname(__FILE__), '../../application.conf')))
    end

    def self.configure_logging

      log = RJack::SLF4J[ 'game_machine' ]
      log.info "About to reconfigure..."

      RJack::Logback.configure do
        console = RJack::Logback::ConsoleAppender.new do |a|

        end
        RJack::Logback.root.add_appender( console )
        RJack::Logback.root.level = RJack::Logback::INFO
      end
    end

  end
end
