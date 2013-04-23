module EntitySystem
  class GameRequest


    # There should be a way to declare the order in which actions are processed.  It should not be left up to the developer to decide in some ad hoc fashion
    # with no declaration
    def self.action_order
      [
        :purchase_currency,
        :purchase_item,
        :start_quest,
        :finish_quest
      ]
    end

    # Actions map directly to systems.
    def request(actions,user_components_data)
      UserComponentBase.update_user_components(user_components_data)
      actions.each do |action|
        "#{action}System".constantize.run
      end
    end
  end
end
