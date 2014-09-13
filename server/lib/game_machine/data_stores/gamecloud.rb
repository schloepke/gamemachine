module GameMachine
  module DataStores
    class Gamecloud

      attr_reader :serialization
      def initialize(serialization)
        @serialization = serialization
      end

      def connect
        JavaLib::Couchclient.get_instance.set_credentials(
          Application.config.gamecloud.host,
          Application.config.gamecloud.user,
          Application.config.gamecloud.api_key
        )
      end

      def get(id)
        if serialization == 'json'
          JavaLib::Couchclient.get_instance.getString(id)
        else
          JavaLib::Couchclient.get_instance.getBytes(id)
        end
      end

      def delete(id)
        JavaLib::Couchclient.get_instance.delete(id)
      end

      def set(id,value)
        if serialization == 'json'
          JavaLib::Couchclient.get_instance.putString(id,value)
        else
          JavaLib::Couchclient.get_instance.putBytes(id,value)
        end
      end
    end
  end
end