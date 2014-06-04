module Web
  module Controllers
    class BaseController
      attr_reader :params
      attr_reader :request

      def set_request(request,params)
        request.body.rewind
        @request = request
        @params = params
        self
      end

    end
  end
end
