module GameMachine
  module DataStores
    class Gamecloud

      def connect
        JavaLib::Couchclient.get_instance.set_credentials(
          Application.config.gamecloud_host,
          Application.config.gamecloud_user,
          Application.config.gamecloud_api_key
        )
      end

      def get(id)
        JavaLib::Couchclient.get_instance.getBytes(id)
      end

      def delete(id)
        JavaLib::Couchclient.get_instance.delete(id)
      end

      def set(id,value)
        JavaLib::Couchclient.get_instance.putBytes(id,value)
      end
    end
  end
end