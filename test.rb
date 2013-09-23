require 'rubygems'
require 'ffi'

module Detour
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), 'libdetour.so')
  if File.exists?(sofile)
    ffi_lib sofile
  end
end

module Recast
  extend FFI::Library
  sofile = File.join(File.dirname(__FILE__), 'librecast.so')
  if File.exists?(sofile)
    ffi_lib sofile
  end
end


class Pathing
  def self.test
  end

end
