/* 
*  This file is part of the Unity networking tutorial by M2H (http://www.M2H.nl)
*  The original author of this code is Mike Hergaarden minus some minor changes.
*/
  
#pragma strict

public static var usingChat : boolean = false;  //Can be used to determine if we need to stop player movement since we're chatting
var skin : GUISkin;                     //Skin
static var showChat : boolean= true;           //Show/Hide the chat
private var inputField : String= "";
private var scrollPosition : Vector2;
private var width : int= 400;
private var height : int= 180;
private var playerName : String;
private var lastUnfocus : float =0;
private var window : Rect;
private var lastEntry : float = 0.0;
private static var thisScript : FPSChat;
private var chatEntries = new ArrayList();

class FPSChatEntry {
    var name : String= "";
    var text : String= "";  
}

function Awake(){
    usingChat=false;
   thisScript = this;
   
    //window = Rect(Screen.width-185, 20 + scoreBoard.scoreBoardHeight, width, height);
    window = Rect(0, Screen.height - height, width, height);
    window.y = Screen.height - height; //scoreBoard.scoreBoardHeight;
    lastEntry = Time.time;
}

function CloseChatWindow () {
    showChat = false;
    inputField = "";
    chatEntries = new ArrayList();
}

function ShowChatWindow () {
    showChat = true;
    inputField = "";
    chatEntries = new ArrayList();
}

function OnGUI () {
    if(!showChat){
        return;
    }
	
    GUI.skin = skin;
    

    if (Event.current.type == EventType.keyDown && Event.current.character == "\n" && inputField.Length <= 0)  {
        if(lastUnfocus+0.25<Time.time){
            usingChat=true;
            GUI.FocusWindow(5);
            GUI.FocusControl("Chat input field");
            Screen.lockCursor = false;
        }
    }
   //Screen.lockCursor = screenLock;
    GUI.backgroundColor = new Color(161,176,131);
    window = GUI.Window (5, window, GlobalChatWindow, "");
}

function GlobalChatWindow (id : int) {
   GUILayout.BeginVertical();
    //GUILayout.Space(10);
    GUILayout.EndVertical();
    
  // Begin a scroll view. All rects are calculated automatically - 
  // it will use up any available screen space and make sure contents flow correctly.
  // This is kept small with the last two parameters to force scrollbars to appear.

  scrollPosition = GUILayout.BeginScrollView (scrollPosition);
  
  for (var entry : FPSChatEntry in chatEntries) {
        GUILayout.BeginHorizontal();
        if(entry.name==""){//Game message
            GUILayout.Label (entry.text);
        }else{
            GUILayout.Label (entry.name+": "+entry.text);
        }
        GUILayout.EndHorizontal();
        //GUILayout.Space(3);
    }

    // End the scrollview we began above.

    GUILayout.EndScrollView ();
    

    if (Event.current.type == EventType.keyDown && Event.current.character == "\n" && inputField.Length > 0)  {
        HitEnter(inputField);
    } else if (Event.current.type == EventType.keyDown && Event.current.character == "\n" && inputField.Length == 0) {
        inputField = ""; 
        GUI.UnfocusWindow ();
        lastUnfocus=Time.time;
        usingChat=false;
        //Screen.lockCursor = true;
    }

    GUI.SetNextControlName("Chat input field");
    inputField = GUILayout.TextField(inputField);

    if(Input.GetKeyDown("mouse 0")){
        if(usingChat){
            usingChat=false;
            GUI.UnfocusWindow ();//Deselect chat
            lastUnfocus=Time.time;
        }
    }
     GUI.DragWindow();
}

function HitEnter(msg : String){
    msg = msg.Replace("\n", "");
    msg = msg.Replace("\n", "");
    ApplyGlobalChatText("player",msg);
    inputField = ""; //Clear line
    //GUI.UnfocusWindow ();
    lastUnfocus=Time.time;
    usingChat=false;
    this.gameObject.GetComponent("Chat").SendMessage("SendChatMessage",msg);
    //Screen.lockCursor = true;
}

static function StaticMsg(msg : String){
    thisScript.HitEnter(msg);
}

function ApplyGlobalChatText (name : String, msg : String) {
    var entry : FPSChatEntry = new FPSChatEntry();
    entry.name = name;
    entry.text = msg;
    chatEntries.Add(entry);
    lastEntry = Time.time;

    if (chatEntries.Count > 40){
        chatEntries.RemoveAt(0);
    }
    scrollPosition.y = 1000000; 
}

function receiveMessage(msg : String) {
	ApplyGlobalChatText("server",msg);
}