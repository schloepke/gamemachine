# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name        = "game_machine"
  s.version     = '0.0.1'
  s.authors     = ["Chris Ochs"]
  s.email       = ["chris@ochsnet.com"]
  s.homepage    = ""
  s.summary     = %q{Game Machine}
  s.description = %q{game server}

  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.files         = `git ls-files`.split("\n")
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  
  s.require_paths = ["lib"]

  s.add_development_dependency 'rake'
  s.add_development_dependency 'rspec'
  
end
