using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using GameMachine.Common;
using Character = io.gamemachine.messages.Character;

namespace GameMachine {
    namespace DefaultClient {
        public class UIController : MonoBehaviour {

            public static UIController instance;
            private AdminUI adminUI;
            private LoginUI loginUI;
            private MainUI mainUI;
            private CharactersUI charactersUI;
            private CharacterUI characterUI;
            private AccountUI accountUI;
            private PasswordUI passwordUI;
            private PlayerUI playerUI;
            private DefaultClient.Client client;
            private Login login;
            public string mainScene = "main";

            void Awake() {
                instance = this;
            }
            // Use this for initialization
            void Start() {
                adminUI = transform.Find("admin").GetComponent<AdminUI>();
                mainUI = transform.Find("menu").GetComponent<MainUI>();
                accountUI = transform.Find("account").GetComponent<AccountUI>();
                loginUI = transform.Find("login").GetComponent<LoginUI>();
                passwordUI = transform.Find("password").GetComponent<PasswordUI>();
                charactersUI = transform.Find("characters").GetComponent<CharactersUI>();
                characterUI = transform.Find("character").GetComponent<CharacterUI>();
                playerUI = transform.Find("player").GetComponent<PlayerUI>();
            }

            // Destroy UI when we load the first level
            private void OnLevelWasLoaded(int level) {
                Destroy(this.gameObject);
            }

            public void SetClient(DefaultClient.Client client) {
                this.client = client;
                login = client.gameObject.AddComponent<GameMachine.Login>() as GameMachine.Login;
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
                mainUI.Load();
            }

            public void LoadPassword() {
                ResetUI();
                passwordUI.Load();
            }

            public void LoadLogin() {
                ResetUI();
                loginUI.Load();
            }

            public void LoadCharacter(string characterId) {
                ResetUI();
                characterUI.Load(characterId);
            }

            public void LoadCharacters() {
                ResetUI();
                charactersUI.Load();
            }

            public void LoadAccount() {
                ResetUI();
                accountUI.Load();
            }

            public void LoadAdmin() {
                ResetUI();
                adminUI.Load();
            }

            public void LoadPlayer(string playerId) {
                ResetUI();
                playerUI.playerId = playerId;
                playerUI.Load(playerId);
            }

            public void OnCharacterSelected(Character character) {
                NetworkSettings.instance.character = character;
                ResetUI();
                Application.LoadLevel(mainScene);
            }

            public void ResetUI() {
                Notice("", Color.white);
                mainUI.Show();
                adminUI.gameObject.GetComponent<Canvas>().enabled = false;
                accountUI.gameObject.GetComponent<Canvas>().enabled = false;
                loginUI.gameObject.GetComponent<Canvas>().enabled = false;
                characterUI.gameObject.GetComponent<Canvas>().enabled = false;
                charactersUI.gameObject.GetComponent<Canvas>().enabled = false;
                passwordUI.gameObject.GetComponent<Canvas>().enabled = false;
                playerUI.gameObject.GetComponent<Canvas>().enabled = false;
            }

        }
    }
}
