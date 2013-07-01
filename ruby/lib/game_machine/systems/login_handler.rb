module GameMachine
  module Systems
    class LoginHandler < Actor

      def on_receive(client_message)
        player_login = client_message.player_login
        auth = login(player_login.username,player_login.password)
        get_sender.tell(auth,nil)
      end

      private
      
      def login(user,pass)
        if user == username && pass == password
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
