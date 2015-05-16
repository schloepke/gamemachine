# -*- encoding: utf-8 -*-
# stub: dentaku 1.2.1 ruby lib

Gem::Specification.new do |s|
  s.name = "dentaku"
  s.version = "1.2.1"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Solomon White"]
  s.date = "2014-10-22"
  s.description = "    Dentaku is a parser and evaluator for mathematical formulas\n"
  s.email = ["rubysolo@gmail.com"]
  s.homepage = "http://github.com/rubysolo/dentaku"
  s.licenses = ["MIT"]
  s.require_paths = ["lib"]
  s.rubyforge_project = "dentaku"
  s.rubygems_version = "2.1.9"
  s.summary = "A formula language parser and evaluator"

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_development_dependency(%q<rake>, [">= 0"])
      s.add_development_dependency(%q<rspec>, [">= 0"])
    else
      s.add_dependency(%q<rake>, [">= 0"])
      s.add_dependency(%q<rspec>, [">= 0"])
    end
  else
    s.add_dependency(%q<rake>, [">= 0"])
    s.add_dependency(%q<rspec>, [">= 0"])
  end
end
