
require 'securerandom'
require 'digest/md5'

module GameMachine
  class Uniqueid
    class << self
      def generate_token(id)
        digest(id + random_salt)
      end


      def random_salt
        SecureRandom.urlsafe_base64
      end


      def digest(string)
        Digest::MD5.hexdigest string
      end
    end
  end
end
