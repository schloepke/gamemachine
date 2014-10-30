using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Chat;
using GameMachine.Core;

public class TeamDemo : MonoBehaviour
{

    // Use this for initialization
    void Start ()
    {
        this.gameObject.AddComponent<ChatManager> ();
        this.gameObject.AddComponent<TeamUi> ();

        TeamRequirements.AddRequirement ("uberteam", "rating >= 2000");
        TeamRequirements.AddRequirement ("uberteam", "wins >= 400");
        TeamRequirements.AddRequirement ("loserville", "rating >= 10 AND wins < 10");

        TeamMemberSkills.AddSkill (User.Instance.username, "rating", 300);
        TeamMemberSkills.AddSkill (User.Instance.username, "wins", 2);
    }
	
    // Update is called once per frame
    void Update ()
    {
	
    }
}
