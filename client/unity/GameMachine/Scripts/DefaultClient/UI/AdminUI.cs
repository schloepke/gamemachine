using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using GameMachine.Common;
using GameMachine.HttpApi;
using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Player = io.gamemachine.messages.Player;
using Players = io.gamemachine.messages.Players;

namespace GameMachine {
    namespace ClientLib {
        public class AdminUI : MonoBehaviour, IAdminApi, IPlayerApi {
            private Transform panel;
            private Transform result;
            private Transform results;
            void Start() {
                panel = transform.Find("panel");
                results = transform.Find("results");
                result = transform.Find("result");
                result.gameObject.SetActive(false);
                gameObject.GetComponent<Canvas>().enabled = false;
            }


            public void Load() {
                gameObject.GetComponent<Canvas>().enabled = true;
                ClearResults();
            }

            private void ClearResults() {
                foreach (Transform t in results) {
                    Destroy(t.gameObject);
                }
            }

            private Button SearchButton() {
                return panel.transform.Find("search").GetComponent<Button>();
            }

            private InputField SearchInput() {
                return panel.transform.Find("search_input").GetComponent<InputField>();
            }

            public void PlayerSearch() {
                ClearResults();
                AdminApi.instance.PlayerSearch(SearchInput().text,this);
            }

            public void LoadPlayerUI(string playerId) {
                UIController.instance.LoadPlayer(playerId);
            }

            void IAdminApi.OnPlayerSearch(Players players) {
                result.gameObject.SetActive(true);
                foreach (Player player in players.player) {
                    GameObject go = GameObject.Instantiate(result.gameObject) as GameObject;
                    go.name = player.id;
                    go.transform.SetParent(results,false);
                    go.transform.Find("Text").GetComponent<Text>().text = player.id;
                    Button b = go.transform.GetComponent<Button>();
                    AddListener(b, player.id);
                }
                result.gameObject.SetActive(false);
            }

            void AddListener(Button b, string value) {
                b.onClick.AddListener(() => LoadPlayerUI(value));
            }

            void IAdminApi.OnPlayerSearchError(string error) {
                UIController.instance.Notice(error, Color.red);
            }

            void IAdminApi.OnPlayerCharacters(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }


            void IPlayerApi.OnPlayerCreated(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCreateError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayer(Player player) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordChanged(string result) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPasswordError(string error) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharacters(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IPlayerApi.OnPlayerCharactersError(string error) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnCharacterSearch(Characters characters) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnCharacterSearchError(string error) {
                throw new System.NotImplementedException();
            }



            void IAdminApi.OnPlayerDeleted(string result) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnPlayerDeleteError(string error) {
                throw new System.NotImplementedException();
            }


            void IAdminApi.OnPlayerPasswordSet(string result) {
                throw new System.NotImplementedException();
            }

            void IAdminApi.OnPlayerPasswordError(string error) {
                throw new System.NotImplementedException();
            }

            
        }
    }
}
