module GameMachine
  module RestApi
    class Auth < Actor::Base

      def on_receive(message)
        params = body_to_params(message.fetch(:body,nil))
        if auth = login(params['username'],params['password'])
          response = auth
        else
          response = "error"
        end
        getSender.tell(response,get_self)
      rescue Exception => e
        GameMachine.logger.error "#{self.class.name} #{e.to_s}"
        getSender.tell('error',get_self)
      end

      private

      def login(user,pass)
        Application.auth_handler.authorize(user,pass)
      end


      def body_to_params(body)
        data = URI.decode_www_form(body)
        data.each_with_object({}) {|d,acc| acc[d[0]] = d[1]}
      end
    end
  end
end
