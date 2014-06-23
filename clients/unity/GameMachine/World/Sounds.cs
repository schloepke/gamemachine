using UnityEngine;
using System.Collections;

public class Sounds : MonoBehaviour
{

    public AudioClip steelsword;

    public void PlaySword()
    {
        audio.PlayOneShot(steelsword);
    }
    // Use this for initialization
    void Start()
    {
        //audio.PlayOneShot(steelsword);
    }
	
    // Update is called once per frame
    void Update()
    {
	
    }
}
