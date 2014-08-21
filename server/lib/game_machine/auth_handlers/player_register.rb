module GameMachine
  module AuthHandlers
    class PlayerRegister
      include Models

      attr_accessor :params, :errors
      def initialize(params)
        @params = params
        @errors = {}
      end

      def valid?
        errors.size == 0
      end

      def add_error(field,message)
        errors[field] ||= []
        errors[field] << message
      end

      def error
          {'error' => errors}
      end

      def success(message)
        {'success' => message}
      end

      # This is called whenever a player is registered
      # It must return a simple hash with a success or error key
      def register!

        # Create one character at registration time
        password = params['password']
        username = params['username']
        character_name = params['character_name']

        # make sure user/character do not already exist
        if User.find(username,10000)
          add_error('username','must not already exist')
          return error
        end

        if username == character_name
          add_error('username','must be different from character name')
        end

        # Create user
        user = User.new(
          :id => username,
          :password => password,
        )
        if user.password.size > 4
          user.save
        else
          add_error('password', 'length must be > 4')
        end

        if valid?
          success("Account created!")
        else
          error
        end
      end

    end
  end
end
