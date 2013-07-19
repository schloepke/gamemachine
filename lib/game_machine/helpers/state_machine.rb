module GameMachine
  module Helpers
    module StateMachine

      def load_state(id, &block)
        @actor_states[id] ||= @initial_state
        self.state = @actor_states[id]
        if block_given?
          yield
          save_state(id)
        end
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
        @initial_state = self.state.dup
      end
    end
  end
end
