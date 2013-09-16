module GameMachine
  class GameData

    def self.load_from(filename)
      @data = YAML.load(File.read(filename))
    end

    def self.data
      @data
    end
  end
end
