
require 'dentaku'

module GameMachine
  module DefaultHandlers
    class TeamHandler

      attr_reader :requirements, :matched

      def self.matched
        if @matched
          @matched
        else
          @matched = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def self.requirements
        if @requirements
          @requirements
        else
          @requirements = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def initialize
        @requirements = self.class.requirements
        @matched = self.class.matched
      end

      def update_teams(key)
      end

      def team_created(team,create_team_message)
        matched[team.name] = {}
        if create_team_message.requirements
          unless requirements[team.name]
            requirements[team.name] = {:c => Dentaku::Calculator.new, :expr => []}
            create_team_message.requirements.each do |expr|
              requirements[team.name][:expr] << expr
            end
          end
        end
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
        matching = []
        teams.teams.each do |team|
          if team.owner == teams_request.player_id
            matching << team
            next
          end

          if requirements[team.name]
            if teams_request.skills
              if matched[team.name][teams_request.player_id]
                matching << team
              elsif pass_skilltest?(teams_request.player_id,teams_request.skills,requirements[team.name])
                matching << team
                matched[team.name][teams_request.player_id] = true
              end
            end
          else
            matching << team
            next
          end
        end

        teams.teams = matching
        teams
      end

      def pass_skilltest?(player_id,skills,req)
        req[:c].clear
        req[:c].store(skills.marshal_dump)
        req[:expr].each do |expr|
          unless req[:c].evaluate(expr)
            return false
          end
        end
        true
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
