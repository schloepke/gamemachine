module GameMachine
  module Helpers
    module StateMachine
      def become(id)
        @actor_states[id] ||= @initial_state
        self.state = @actor_states[id]
      end

      def save_state(id)
        @actor_states[id] = self.state
      end

      def destroy_state(id)
        @actor_states.delete(id)
      end

      def initialize_states
        @actor_states = {}
        initialize_state_machines
        @initial_state = self.state
      end
    end
  end
end
