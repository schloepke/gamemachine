
# This class is set in the configuration.

# This is a simple implementation that works together with the built in
# user registration and login via http. User data is stored in the object database.

# The minimum necessary to implement is the authtoken_for method.  That method can
# block as it is only called once when the client first connects and is cached
# internally after that. So you could make an external http call for example.
require 'digest/md5'
module Example
  class AuthenticationHandler
    include Models

    def initialize
      @sessions = {}
    end

    # Returns true is authorized, false if not
    def authorize(username,password)
        GameMachine.logger.info "client: #{username} #{password}"
      if user = User.find(username,5000)
        GameMachine.logger.info "user: #{user.id} #{user.password}"
        if password == user.password
          @sessions[username] = authtoken(username,password)
          return @sessions[username]
        end
      end
      false
    end

    # Returns a session token for a logged in user.  This must be a string and
    # should not be too long, as it gets sent with every message.
    def authtoken_for(username)
      @sessions.fetch(username,nil)
    end

    private

    def authtoken(username,password)
      Digest::MD5.hexdigest("#{username}#{password}#{rand(10000)}")
    end
  end
end

