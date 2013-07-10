ENV['BUNDLE_GEMFILE'] ||= File.expand_path('../../Gemfile', __FILE__)
ENV['APP_ROOT'] ||= File.join(File.dirname(__FILE__), '../')
ENV['GAME_ENV'] ||= 'development'
require 'rubygems'
require 'bundler/setup' if File.exists?(ENV['BUNDLE_GEMFILE'])
Bundler.setup(:default)

