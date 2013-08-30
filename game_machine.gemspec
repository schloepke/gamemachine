# -*- encoding: utf-8 -*-
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'game_machine/version'

Gem::Specification.new do |gem|
  gem.name        = "game_machine"
  gem.version     = GameMachine::VERSION
  gem.authors     = ["Chris Ochs"]
  gem.email       = ["chris@ochsnet.com"]
  gem.homepage    = "https://github.com/chrisochs/game_machine"
  gem.summary     = %q{Game Machine}
  gem.description = %q{game server}

  gem.files         = `git ls-files`.split($/).reject { |f| f =~ /^(games\/physics_experiments)/ }
  gem.executables   = gem.files.grep(%r{^bin/}).map{ |f| File.basename(f) }
  gem.test_files    = gem.files.grep(%r{^(test|spec|features|integration_tests)/})
  gem.require_paths = ["lib"]

  gem.add_dependency 'rjack-logback'
  gem.add_dependency 'settingslogic'
  gem.add_dependency 'slop'
  gem.add_dependency 'consistent-hashing'
  gem.add_dependency  'json'
  gem.add_dependency 'guard-shell'
  gem.add_dependency 'guard-rake'
  gem.add_dependency 'guard-process'
  gem.add_dependency 'state_machine'
  gem.add_dependency 'ffi'
  gem.add_dependency 'trollop'
  gem.add_dependency 'active_support'

  gem.add_development_dependency 'rake'
  gem.add_development_dependency 'rspec'
  gem.add_development_dependency 'rspec-mocks'
  gem.add_development_dependency 'rspec-expectations'
  gem.add_development_dependency 'pry'
  gem.add_development_dependency 'guard-jruby-rspec'
  gem.add_development_dependency 'descriptive_statistics'
  
end
