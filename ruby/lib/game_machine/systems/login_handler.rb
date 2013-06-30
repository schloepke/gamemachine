module GameMachine
  module Systems
    class LoginHandler < Actor

      def on_receive(player_login)
        auth = login(player_login.username,player_login.password)
        sender.send_message(auth)
      end

      private
      
      def login(username,password)
        if username == self.username && password == self.password
          Settings.authtoken
        else
          false
        end
      end

      def username
        Settings.login_username
      end

      def password
        Settings.login_password
      end
    end
  end
end
