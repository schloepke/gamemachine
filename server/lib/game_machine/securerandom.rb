require 'securerandom'
require 'digest/md5'
module SecureRandom
  def self.hex(s)
    return Digest::MD5.hexdigest('broken')
    raise NotImplementedError
  end
end
