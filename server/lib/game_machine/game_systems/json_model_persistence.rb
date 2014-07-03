module GameMachine
  module GameSystems
    class JsonModelPersistence < Actor::Base
      include Commands

      def on_receive(message)
        json_model = message
        if json_model.id
          if json_model.id.match(/find_by_id/)
            id = json_model.id.sub('find_by_id','')
            if json_model = json_model.class.find(id)
              commands.player.send_message(json_model,message.player_id)
            end
          else
            json_model.save
          end
        else
          unhandled(message)
        end
      end
    end
  end
end
