using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Chat;
using GameMachine.Core;
using GameMachine.Models.Team;

public class TeamDemo : MonoBehaviour
{

    // Use this for initialization
    void Start ()
    {
        this.gameObject.AddComponent<ChatManager> ();
        this.gameObject.AddComponent<TeamUi> ();

        /*
         * Skills based matchmaking
         * 
         * TeamRequirements.AddRequirement adds an expression that will be evaluated against player skills.  If a player
         * doesn't pass the skill test for a team, that team is not sent to their client for display.
         * 
         * TeamMemberSkills.AddSkill adds a skill and it's value to a player.
         * 
         * Expressions can use the following operators:
         * Logic: < > <= >= <> != = AND OR
         * Math: + - * / %
         * 
         * You can have multiple requirements per team.  All requirements have to return true for a player to
         * be able to join the team.
         * 
         * Likewise you can add as many skills as you wish to a player.
         * 
         * Server code and also agents can alternatively set player skills from the server side and disallow
         * players from setting skills directly.
         * 
         */
        TeamRequirements.AddRequirement ("uberteam", "rating >= 2000");
        TeamRequirements.AddRequirement ("uberteam", "wins >= 400");
        TeamRequirements.AddRequirement ("loserville", "rating >= 10 AND wins < 10");

       
        PlayerSkills skills = new PlayerSkills ();
        skills.AddSkill ("rating", 300);
        skills.AddSkill ("wins", 2);
        skills.Send ();
    }
	
    // Update is called once per frame
    void Update ()
    {
	
    }
}
