require 'digest/md5'
module Example
  class AuthenticationHandler
    include Models

    def initialize
      @sessions = {}
    end

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

    def authtoken_for(username)
      @sessions.fetch(username,nil)
    end

    private

    def authtoken(username,password)
      Digest::MD5.hexdigest("#{username}#{password}#{rand(10000)}")
    end
  end
end

