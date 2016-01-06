using UnityEngine;
using UnityEngine.UI;
using System.Collections;

namespace GameMachine {
    namespace ClientLib {
        public class CharacterOptions : MonoBehaviour {

            public string characterId;
            public bool unused = true;
            // Use this for initialization
            void Start() {

            }

            public void Load(string characterId, bool unused) {
                this.characterId = characterId;
                this.unused = unused;
                if (unused) {
                    CharacterText().text = "Empty Slot";
                    transform.Find("login").gameObject.SetActive(false);
                    transform.Find("create").gameObject.SetActive(true);
                    transform.Find("delete").gameObject.SetActive(false);
                } else {
                    CharacterText().text = characterId;
                    transform.Find("login").gameObject.SetActive(true);
                    transform.Find("create").gameObject.SetActive(false);
                    transform.Find("delete").gameObject.SetActive(true);
                }
                
            }

            public Text CharacterText() {
                return transform.Find("name").GetComponent<Text>();
            }
        }
    }
}
