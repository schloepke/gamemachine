using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Core;
using ProtoBuf;
using GameMachine.Common;
using GameMessage = io.gamemachine.messages.GameMessage;
using GmStats = io.gamemachine.messages.GmStats;

namespace GameMachine {
    namespace Common {
        public class StatsUI : MonoBehaviour, GameMachine.Core.Behavior {

            private GameObject container;
            private GameObject template;
            private Canvas canvas;
            public static StatsUI instance;
            public string routeName = "StatsHandler";
            private GameMessageHandler messageHandler;
            private bool active = false;
            private GmStats currentStats;

            void Start() {
                instance = this;
                messageHandler = GameMessageHandler.Instance;
                messageHandler.Register(this, "GmStats");
                InvokeRepeating("SendRequest", 1f, 5f);
                container = transform.Find("stats").gameObject;
                template = transform.Find("template").gameObject;
                template.SetActive(false);
                canvas = gameObject.GetComponent<Canvas>() as Canvas;
                canvas.enabled = false;
            }

            void Update() {
                if (Input.GetKeyDown(KeyCode.F10)) {
                    if (!InputManager.KeyInputDisabled()) {
                        if (active) {
                            active = false;
                            canvas.enabled = false;
                        } else {
                            active = true;
                            canvas.enabled = true;
                            ShowStats();
                        }
                    }
                }
            }

            void ShowStats() {
                if (currentStats != null) {
                    canvas.enabled = false;
                    foreach (Transform t in container.transform) {
                        Destroy(t.gameObject);
                    }
                    ShowStat("Player bytes out", currentStats.playerBytesOut);
                    ShowStat("Bytes out", currentStats.bytesOut);
                    ShowStat("Messages in/out", currentStats.messageCountInOut);
                    ShowStat("Messages in", currentStats.messageCountIn);
                    ShowStat("Messages out", currentStats.messageCountOut);
                    ShowStat("Connections", currentStats.connectionCount);
                    ShowStat("Entities in visual range", GameEntityManager.GameEntityCount());
                    canvas.enabled = true;
                }
            }

            void ShowStat(string name, long stat) {
                AddText(name, Color.white);
                AddText(stat.ToString(), Color.green);
            }

            void AddText(string name, Color color) {
                GameObject go = GameObject.Instantiate(template);
                go.transform.SetParent(container.transform);
                go.name = name;
                go.SetActive(true);
                Text text = go.GetComponent<Text>();
                text.text = name;
                text.color = color;
            }

            public void OnError(object message) {

            }

            public void OnMessage(object message) {
                if (message is GmStats) {
                    currentStats = (GmStats)message;
                    if (active) {
                        ShowStats();
                    }
                }
            }

            public void SendRequest() {
                GmStats stats = new GmStats();
                GameMachine.Core.ActorSystem.sendImmediate = true;
                messageHandler.SendReliable(stats, routeName);
                messageHandler.SendGameMessages();
                GameMachine.Core.ActorSystem.sendImmediate = false;
            }
        }
    }
}
