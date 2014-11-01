
require 'dentaku'

module GameMachine
  module DefaultHandlers
    class TeamHandler
      include Models
      attr_reader :requirements, :matched, :presetskills

      def self.matched
        if @matched
          @matched
        else
          @matched = java.util.concurrent.ConcurrentHashMap.new
        end
      end

      def initialize
        @matched = self.class.matched
      end

      def player_can_set_skills?
        true
      end

      def scope_key(str,player_id)
        "#{game_id(player_id)}^^#{str}"
      end

      def game_id(player_id)
        player_service = GameMachine::JavaLib::PlayerService.get_instance
        player_service.get_game_id(player_id)
      end

      def update_teams(key)
      end

      def team_created(team,create_team_message)
        matched[team.name] = {}
      end

      # Should return a Match object if a match is found, otherwise nil
      def match!(teams,team)
        my_rating = team_rating(team)
        best = nil
        best_team = nil
        teams.teams.each do |other|

          if other.members.size < other.min_for_match
            next
          end

          if rating = team_rating(other)
            diff = my_rating > rating ? my_rating - rating : rating - my_rating
            if best.nil? || diff < best
              best = diff
              best_team = other
            end
          end
        end

        if best_team
          GameMachine::Models::StartMatch.new(:team_names => [team.name,best_team.name])
        else
          nil
        end
      end

      # Called when Game Machine has started the match
      def match_started(match)

      end

      def team_rating(team)
        rating = 0
        team.members.each do |member|
          if skills = skills_for_player(member)
            if skills[:rating] && skills[:rating] > 0
              rating += skills[:rating]
            end
          end
        end
        rating
      end

      def skills_for_player(player_id)
        player_skills_id = "player_skills_#{player_id}"
        if player_skills = PlayerSkills.find(scope_key(player_skills_id,player_id))
          skills = player_skills.skills.marshal_dump
        else
          skills = nil
        end
        skills
      end

      # Filter you can apply to the teams list sent to clients
      # You can add whatever extra fields you want to the TeamsRequest
      # class on the client and they will show up here.
      def teams_filter(teams,teams_request)
        skills = skills_for_player(teams_request.player_id)
        matching = []
        teams.teams.each do |team|
          if team.owner == teams_request.player_id
            matching << team
            next
          end

          if team.requirements
            if skills
              if matched[team.name] && matched[team.name][teams_request.player_id]
                matching << team
              elsif pass_skilltest?(teams_request.player_id,skills,team.requirements)
                matching << team
                 matched[team.name] ||= {}
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
        c = Dentaku::Calculator.new
        c.store(skills)
        #puts "Skills=#{skills.inspect} requirements=#{req.inspect}"
        req.each do |expr|
          unless c.evaluate(expr)
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
