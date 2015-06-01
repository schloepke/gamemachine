using UnityEngine;
using UnityEngine.UI;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public class CharacterUI : MonoBehaviour {

            // Use this for initialization
            void Start() {
                if (NetworkSettings.instance != null) {
                    Text text = transform.Find("Text").GetComponent<Text>() as Text;
                    text.text = NetworkSettings.instance.character.id;
                }
            }

        }
    }
}
