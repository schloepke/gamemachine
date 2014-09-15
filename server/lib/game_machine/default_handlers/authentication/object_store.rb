
# This class is set in the configuration.

# This is a simple implementation that works together with the built in
# user registration and login via http. User data is stored in the object database.

# The minimum necessary to implement is the authtoken_for method.  That method can
# block as it is only called once when the client first connects and is cached
# internally after that. So you could make an external http call for example.
require 'digest/md5'
module GameMachine
  module DefaultHandlers
    module Authentication
      class ObjectStore
        include Models

        def initialize
          @sessions = {}
        end

        # Returns true if authorized, false if not
        def authorize(username,password)
          GameMachine.logger.info "authorize: #{username}"
          if player_entity = EntityLib::PlayerEntity.find(username,2000)
            if player_entity.password_is_valid(password)
              @sessions[username] = authtoken(username,password)
              player_entity.player.set_authtoken(@sessions[username])
              player_entity.save
              return @sessions[username]
            else
              GameMachine.logger.info "player: #{player_entity.player.id} password does not match"
              false
            end
          else
            GameMachine.logger.info "player: #{username} not found"
            false
          end
          false
        end

        # Returns a session token for a logged in user.  This must be a string and
        # should not be too long, as it gets sent with every message.
        def authtoken_for(username)
          if authtoken = @sessions.fetch(username,nil)
            return authtoken

          # user authenticated on different server, we have to look up their authtoken
          # and save it in the local sessions hash
          elsif player_entity = EntityLib::PlayerEntity.find(username,2000)
            if authtoken = player_entity.player.authtoken
              @sessions[username] = authtoken
              return authtoken
            else
              GameMachine.logger.info "Authoken for #{username} is nil"
              nil
            end
          else
            GameMachine.logger.info "User #{username} not found"
            nil
          end
        end

        private

        def authtoken(username,password)
          Digest::MD5.hexdigest("#{username}#{password}#{rand(10000)}")
        end
      end
    end
  end
end

