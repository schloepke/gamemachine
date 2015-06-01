using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Linq;
using GameMachine.Common;
using GameMachine.HttpApi;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;
using Players = io.gamemachine.messages.Players;

namespace GameMachine {
    namespace DefaultClient {
        public class PlayerUI : MonoBehaviour, IPlayerApi, IAdminApi {
            private Transform panel;
            public string playerId;
            private int maxCharacters = 6;

            void Start() {
                panel = transform.Find("panel");
                gameObject.GetComponent<Canvas>().enabled = false;
            }

            public void Load(string playerId) {
                gameObject.GetComponent<Canvas>().enabled = true;
                panel.Find("player_id").GetComponent<Text>().text = playerId;
                SetError("");
                SetPasswordButton().interactable = true;
                DeleteButton().interactable = true;
                AdminApi.instance.PlayerCharacters(playerId, this);
            }

            public void SetPassword() {
                SetError("");
                SetPasswordButton().interactable = false;
                DeleteButton().interactable = false;
                AdminApi.instance.SetPassword(playerId, NewPasswordInput().text, this);
            }

            public void DeleteUser() {
                SetError("");
                SetPasswordButton().interactable = false;
                DeleteButton().interactable = false;
                AdminApi.instance.DeletePlayer(playerId, this);
            }

            private InputField NewPasswordInput() {
                return panel.transform.Find("new_password_input").GetComponent<InputField>();
            }

            private Button SetPasswordButton() {
                return panel.transform.Find("set_password").GetComponent<Button>();
            }

            private Button DeleteButton() {
                return panel.transform.Find("delete_user").GetComponent<Button>();
            }

            private void SetError(string text) {
                panel.transform.Find("error").Find("Text").GetComponent<Text>().text = text;
            }

            void IAdminApi.OnPlayerPasswordSet(string result) {
                SetPasswordButton().interactable = true;
                DeleteButton().interactable = true;
                SetError("Password set");

            }

            void IAdminApi.OnPlayerPasswordError(string error) {
                SetPasswordButton().interactable = true;
                DeleteButton().interactable = true;
                SetError("Error changing password");
            }

            void IAdminApi.OnPlayerDeleted(string result) {
                UIController.instance.LoadAdmin();
                UIController.instance.Notice(result + " deleted", Color.red);
            }

            void IAdminApi.OnPlayerDeleteError(string error) {
                SetError("Error deleting user " + error);
            }

            void IAdminApi.OnPlayerCharacters(Characters characters) {
               
            }

            void IAdminApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordChanged(string result) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharacters(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayer(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerError(string error) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnCharacterSearch(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnCharacterSearchError(string error) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnPlayerSearch(io.gamemachine.messages.Players players) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnPlayerSearchError(string error) {
                throw new System.NotImplementedException();
            }




           
        }
    }
}
