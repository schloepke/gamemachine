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

  end
end
