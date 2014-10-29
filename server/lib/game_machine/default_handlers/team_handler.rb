
module GameMachine
  module DefaultHandlers
    class TeamHandler

      def initialize
      end

      def update_teams(key)
      end

      # Should return a Match object if a match is found, otherwise nil
      def match!(key,team_name)
        teams = GameMachine::Models::Teams.find(key)
        
        GameMachine.logger.info "match! team_name:#{team_name} teams:#{teams}"
        if team = teams.teams.select {|team| team.name != team_name}.first
          return GameMachine::Models::StartMatch.new(:team_names => [team_name,team.name])
        end
        nil
      end

      # Called when Game Machine has started the match
      def match_started(match)

      end

      # Filter you can apply to the teams list sent to clients
      # You can add whatever extra fields you want to the TeamsRequest
      # class on the client and they will show up here.
      def teams_filter(teams,teams_request)
        teams
      end

      # return true if member has rights to create team
      def can_create_team?(team_name,member)
        true
      end

      # return true if member has rights to join team
      def can_add_member?(team_name,member)
        true
      end

      def destroy_on_owner_leave?
        true
      end
    end
  end
end
