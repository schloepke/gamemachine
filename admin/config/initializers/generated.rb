
GeneratorBase.ensure_gen_paths
Dir["#{GeneratorBase.gen_models_path}/**/*.rb"].each {|file| require file}
Dir["#{GeneratorBase.gen_configs_path}/**/*.rb"].each {|file| require file}
