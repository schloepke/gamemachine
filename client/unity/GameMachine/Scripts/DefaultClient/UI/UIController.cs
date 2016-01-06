using UnityEngine;
using UnityEngine.UI;
using GameMachine.Common;
using io.gamemachine.messages;

namespace GameMachine {
    namespace ClientLib {
        public class UIController : MonoBehaviour {

            public static UIController instance;
            public LoginUI loginUI;
            public MainUI mainUI;
            public CharactersUI charactersUI;
            public CharacterUI characterUI;
            public AccountUI accountUI;
            public PasswordUI passwordUI;
            public PlayerUI playerUI;
            private DefaultClient client;
            private Login login;

            void Awake() {
                instance = this;
               
            }

            void Start() {
                ResetUI();
            }

            // Destroy UI when we load the first level
            private void OnLevelWasLoaded(int level) {
                if (level > 0) {
                    Destroy(this.gameObject);
                }
               
            }

            public void SetClient(DefaultClient client) {
                this.client = client;
                login = client.gameObject.AddComponent<Login>() as Login;
                login.SetLoginUi(loginUI);
            }

            public Login GetLogin() {
                return login;
            }

            public void Notice(string message, Color color) {
                Text text = mainUI.transform.Find("notice").Find("Text").GetComponent<Text>();
                text.text = message;
                text.color = color;
            }

            public void LoadMain() {
                ResetUI();
                mainUI.gameObject.SetActive(true);
            }

            public void LoadPassword() {
                ResetUI();
                passwordUI.gameObject.SetActive(true);
                passwordUI.Load();
            }

            public void LoadLogin() {
                ResetUI();
                loginUI.gameObject.SetActive(true);
                loginUI.Load();
            }

            public void LoadCharacter(string characterId) {
                ResetUI();
                characterUI.gameObject.SetActive(true);
                characterUI.Load(characterId);
            }

            public void LoadCharacters() {
                ResetUI();
                charactersUI.gameObject.SetActive(true);
                charactersUI.Load();
            }

            public void LoadAccount() {
                ResetUI();
                accountUI.gameObject.SetActive(true);
                accountUI.Load();
            }

            public void LoadPlayer(string playerId) {
                ResetUI();
                playerUI.gameObject.SetActive(true);
                playerUI.playerId = playerId;
                playerUI.Load(playerId);
            }

            public void OnCharacterSelected(Character character) {
                if (character.zone == null) {
                    Debug.Log("NULL2");
                }
                NetworkSettings.instance.character = character;
                if (string.IsNullOrEmpty(client.GetZone().name)) {
                    Debug.Log("Server assigned zone is null");
                } else {
                    if (character.zone.name != client.GetZone().name) {
                        Debug.Log("Character zone " + character.zone.name + " != server assigned zone " + client.GetZone().name);
                    }
                    if (!NetworkSettings.instance.sameAddress(client.GetZone().hostname, NetworkSettings.instance.hostname)) {
                        Debug.Log("Not connected to correct region host, reconnecting to " + client.GetZone().hostname);
                        client.ReconnectToZone();
                    }
                }
                
                
                ResetUI();
                Application.LoadLevel(client.mainScene);
            }

            public void ResetUI() {
                Notice("", Color.white);
                mainUI.Show();
                accountUI.gameObject.SetActive(false);
                loginUI.gameObject.SetActive(false);
                characterUI.gameObject.SetActive(false);
                charactersUI.gameObject.SetActive(false);
                passwordUI.gameObject.SetActive(false);
                playerUI.gameObject.SetActive(false);
            }

        }
    }
}
