using UnityEngine;
using System.Collections;
using  System.Collections.Generic;

namespace GameMachine
{
    public class GroupUi : MonoBehaviour
    {
        
        private Rect groupWindow;
        private float windowLeft;
        private float windowHeight;
        private float windowWidth;
        private float windowTop;
        private List<string> members = new List<string>();
        public string title;
        public string channelName;
        private Messenger messenger;

        public void AddMember(string member)
        {
            members.Add(member);
        }

        public void RemoveMember(string member)
        {
            members.Remove(member);
        }

        public void Join(Messenger messenger, string group)
        {
            this.messenger = messenger;
            channelName = group;
            title = "Group";
            InvokeRepeating("UpdateMembers", 0.01f, 5.0F);
        }

        void OnGUI()
        {
            GUI.contentColor = Color.green;
            GUI.backgroundColor = Color.black;

            groupWindow = GUI.Window(0, groupWindow, WindowFunction, title);
        }
        
        void WindowFunction(int windowID)
        {
            int memberTop = 15;

            foreach (string member in members)
            {
                
                GUI.Label(new Rect(5, memberTop, 140, 20), member);
                memberTop += 25;
            }
            GUI.DragWindow();
        }

        void UpdateMembers()
        {
            members.Clear();
            if (messenger.subscribers.ContainsKey(channelName))
            {
                foreach (string member in messenger.subscribers[channelName])
                {
                    AddMember(member);
                }
            }
        }

        // Use this for initialization
        void Start()
        {
            windowWidth = 190;
            windowHeight = 300;
            windowLeft = Screen.width - 200;
            windowTop = 200;
            groupWindow = new Rect(windowLeft, windowTop, windowWidth, windowHeight);
        }
        
        void Update()
        {
            
        }
    }
}

