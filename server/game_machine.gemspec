# -*- encoding: utf-8 -*-
lib = File.expand_path('../lib', __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require 'game_machine/version'

Gem::Specification.new do |gem|
  gem.name        = "game_machine"
  gem.version     = GameMachine::VERSION
  gem.authors     = ["Chris Ochs"]
  gem.email       = ["chris@ochsnet.com"]
  gem.homepage    = "https://github.com/gamemachine/gamemachine"
  gem.summary     = %q{Game Machine}
  gem.description = %q{game server}

  gem.files         = `git ls-files`.split($/).reject { |f| f =~ /^(games\/physics_experiments)/ }
  gem.executables   = gem.files.grep(%r{^bin/}).map{ |f| File.basename(f) }
  gem.test_files    = gem.files.grep(%r{^(test|spec|features|integration_tests)/})
  gem.require_paths = ["lib"]

  gem.add_dependency 'i18n'
  gem.add_dependency 'rjack-logback'
  gem.add_dependency 'settingslogic'
  gem.add_dependency 'consistent-hashing'
  gem.add_dependency  'json'
  gem.add_dependency 'state_machine'
  gem.add_dependency 'ffi'
  gem.add_dependency 'activesupport', '~> 3.0.0'
  gem.add_dependency 'hashie'
  gem.add_dependency 'sinatra'
  gem.add_dependency 'sinatra-contrib'
  gem.add_dependency 'haml'

  gem.add_development_dependency 'rake'
  gem.add_development_dependency 'rspec'
  gem.add_development_dependency 'rspec-mocks'
  gem.add_development_dependency 'rspec-expectations'
  gem.add_development_dependency 'descriptive_statistics'

  
end
