using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using TrackData = io.gamemachine.messages.TrackData;
using UserDefinedData = io.gamemachine.messages.UserDefinedData;
using GameMachine.Core;
using System.Reflection;

namespace GameMachine {
    namespace Common {
        public class NetworkFields : MonoBehaviour {

            public enum FieldType {
                None,
                Int,
                Float,
                Vector,
                Quaternion,
                Bool,
                IntFloat
            }

            public enum SynchronizedField {
                Position,
                Vaxis,
                Haxis,
                Yaxis,
                Speed,
                Hidden,
                UserdefVector3,
                UserdefQuaternion,
                UserdefValue1,
                UserdefValue2,
                UserdefValue3,
                UserdefValue4,
                UserdefValue5,
                UserdefValue6,
                UserdefValue7,
                UserdefValue8,
                UserdefValue9,
                UserdefValue10,
                UserdefValue11,
                UserdefValue12,
                UserdefValue13,
                UserdefValue14,
                UserdefValue15,
            }

            public enum UserDefValue {
                Int,
                BroadcastInt,
                Float,
                BroadcastFloat,
                Bool,
                BroadcastBool
            }

            public enum Status {
                Disabled,
                Enabled
            }

            public enum Source {
                TrackData,
                UserDefinedData
            }

            [Header("Standard fields")]
            public bool positionEnabled = false;
            public bool rotationEnabled = false;
            public bool vaxisEnabled = false;
            public bool haxisEnabled = false;
            public bool yaxisEnabled = false;
            public bool speedEnabled = false;
            public bool hiddenEnabled = false;
            

            [Header("Optional fields")]
            public bool userdefVector3Enabled = false;
            public bool userdefQuaternionEnabled = false;
            public bool userdefValue1Enabled = false;
            public bool userdefValue2Enabled = false;
            public bool userdefValue3Enabled = false;
            public bool userdefValue4Enabled = false;
            public bool userdefValue5Enabled = false;
            public bool userdefValue6Enabled = false;
            public bool userdefValue7Enabled = false;
            public bool userdefValue8Enabled = false;
            public bool userdefValue9Enabled = false;
            public bool userdefValue10Enabled = false;
            public bool userdefValue11Enabled = false;
            public bool userdefValue12Enabled = false;
            public bool userdefValue13Enabled = false;
            public bool userdefValue14Enabled = false;
            public bool userdefValue15Enabled = false;

            [Header("Number type")]
            public UserDefValue userdefValue1Type;
            public UserDefValue userdefValue2Type;
            public UserDefValue userdefValue3Type;
            public UserDefValue userdefValue4Type;
            public UserDefValue userdefValue5Type;
            public UserDefValue userdefValue6Type;
            public UserDefValue userdefValue7Type;
            public UserDefValue userdefValue8Type;
            public UserDefValue userdefValue9Type;
            public UserDefValue userdefValue10Type;
            public UserDefValue userdefValue11Type;
            public UserDefValue userdefValue12Type;
            public UserDefValue userdefValue13Type;
            public UserDefValue userdefValue14Type;
            public UserDefValue userdefValue15Type;

            public string userdefValue1Name;
            public string userdefValue2Name;
            public string userdefValue3Name;
            public string userdefValue4Name;
            public string userdefValue5Name;
            public string userdefValue6Name;
            public string userdefValue7Name;
            public string userdefValue8Name;
            public string userdefValue9Name;
            public string userdefValue10Name;
            public string userdefValue11Name;
            public string userdefValue12Name;
            public string userdefValue13Name;
            public string userdefValue14Name;
            public string userdefValue15Name;

            public Vector3 position = Vector3.zero;
            public float vaxis = 0f;
            public float haxis = 0f;
            public float yaxis = 0f;
            public float speed = 0f;
            public bool hidden = false;
           

            public Vector3 userdefVector3 = Vector3.zero;
            public Quaternion userdefQuaternion = Quaternion.identity;

            public int userdefInt1 = 0;
            public int userdefInt2 = 0;
            public int userdefInt3 = 0;
            public int userdefInt4 = 0;
            public int userdefInt5 = 0;
            public int userdefInt6 = 0;
            public int userdefInt7 = 0;
            public int userdefInt8 = 0;
            public int userdefInt9 = 0;
            public int userdefInt10 = 0;
            public int userdefInt11 = 0;
            public int userdefInt12 = 0;
            public int userdefInt13 = 0;
            public int userdefInt14 = 0;
            public int userdefInt15 = 0;

            public float userdefFloat1 = 0f;
            public float userdefFloat2 = 0f;
            public float userdefFloat3 = 0f;
            public float userdefFloat4 = 0f;
            public float userdefFloat5 = 0f;
            public float userdefFloat6 = 0f;
            public float userdefFloat7 = 0f;
            public float userdefFloat8 = 0f;
            public float userdefFloat9 = 0f;
            public float userdefFloat10 = 0f;
            public float userdefFloat11 = 0f;
            public float userdefFloat12 = 0f;
            public float userdefFloat13 = 0f;
            public float userdefFloat14 = 0f;
            public float userdefFloat15 = 0f;

            public bool userdefBool1 = false;
            public bool userdefBool2 = false;
            public bool userdefBool3 = false;
            public bool userdefBool4 = false;
            public bool userdefBool5 = false;
            public bool userdefBool6 = false;
            public bool userdefBool7 = false;
            public bool userdefBool8 = false;
            public bool userdefBool9 = false;
            public bool userdefBool10 = false;
            public bool userdefBool11 = false;
            public bool userdefBool12 = false;
            public bool userdefBool13 = false;
            public bool userdefBool14 = false;
            public bool userdefBool15 = false;

            private struct InputData {
                public string name;
                public NetworkFields.SynchronizedField syncField;
                public NetworkFields.Source source;
                public NetworkFields.FieldType fieldType;
                public PropertyInfo remoteField;
                public FieldInfo localField;
                public bool broadcast;
                public GMKeyCode keycode;
            }

            private static Dictionary<string, InputData> inputDatas = new Dictionary<string, InputData>();
            private static Dictionary<GMKeyCode, InputData> inputDatasByKeycode = new Dictionary<GMKeyCode, InputData>();
            private Util util;
            private TrackData trackData;
            private static bool loaded = false;
            public bool running = false;

            // Use this for initialization
            void Awake() {
                util = Util.Instance;

                if (!loaded) {
                    CreateInputData();
                    foreach (string name in inputDatas.Keys) {
                        //Debug.Log("NetworkField: " + name);
                    }
                }
               
                loaded = true;
                ResetTrackData();
            }

            void Start() {
                running = true;
            }

            public void ResetTrackData() {
                trackData = new TrackData();
                trackData.userDefinedData = new UserDefinedData();
            }

            public void UpdateFromNetwork(TrackData newTrackData, bool hasDelta) {
                trackData = newTrackData;
                foreach (InputData inputData in inputDatas.Values) {
                    //Debug.Log(inputData.name);

                    // broadcasts only applied to user defined values
                    if (trackData.broadcast != 1) {
                        if (inputData.source == NetworkFields.Source.TrackData) {
                            if (inputData.syncField == NetworkFields.SynchronizedField.Position) {
                                if (hasDelta) {
                                    position = util.TrackdataToVector3(trackData, position);
                                } else {
                                    position = util.TrackdataToVector3(trackData);
                                }
                            }
                        } else {
                            if (inputData.syncField == NetworkFields.SynchronizedField.UserdefVector3) {
                                userdefVector3 = util.UserDefinedDataToVector3(trackData.userDefinedData);
                            } else if (inputData.syncField == NetworkFields.SynchronizedField.UserdefQuaternion) {
                                userdefQuaternion = util.UserDefinedDataToQuaternion(trackData.userDefinedData);
                            }
                        }
                    }


                    if (inputData.fieldType == NetworkFields.FieldType.Int) {
                        SetLocalIntFromRemote(inputData);
                    } else if (inputData.fieldType == NetworkFields.FieldType.Bool) {
                        SetLocalBoolFromRemote(inputData);
                    } else if (inputData.fieldType == NetworkFields.FieldType.IntFloat) {
                        SetLocalFloatFromRemoteIntFloat(inputData);
                    } else if (inputData.fieldType == NetworkFields.FieldType.Float) {
                        SetLocalFloatFromRemote(inputData);
                    }

                }
            }

            public TrackData GetTrackData() {
                return trackData;
            }

            // gets local values

            public Vector3 GetVector3(string name) {
                InputData inputData = GetInputData(name);
                return (Vector3)inputData.localField.GetValue(this);
            }

            public Vector3 GetVector3(GMKeyCode code) {
                InputData inputData = GetInputData(code);
                return (Vector3)inputData.localField.GetValue(this);
            }

            public Quaternion GetQuaternion(string name) {
                InputData inputData = GetInputData(name);
                return (Quaternion)inputData.localField.GetValue(this);
            }

            public Quaternion GetQuaternion(GMKeyCode code) {
                InputData inputData = GetInputData(code);
                return (Quaternion)inputData.localField.GetValue(this);
            }

            public int GetInt(string name) {
                InputData inputData = GetInputData(name);
                int value = (int)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, 0);
                }
                return value;
            }

            public int GetInt(GMKeyCode code) {
                InputData inputData = GetInputData(code);
                int value = (int)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, 0);
                }
                return value;
            }

            public float GetFloat(string name) {
                InputData inputData = GetInputData(name);
                float value = (float)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, 0f);
                }
                return value;
            }

            public float GetFloat(GMKeyCode code) {
                InputData inputData = GetInputData(code);
                float value = (float)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, 0f);
                }
                return value;
            }

            public bool GetBool(string name) {
                InputData inputData = GetInputData(name);
                bool value = (bool)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, false);
                }
                return value;
            }

            public bool GetBool(GMKeyCode code) {
                InputData inputData = GetInputData(code);
                bool value = (bool)inputData.localField.GetValue(this);
                if (inputData.broadcast) {
                    inputData.localField.SetValue(this, false);
                }
                return value;
            }

            // sets remote values
            public void SetVector3(string name, Vector3 value) {
                InputData inputData = GetInputData(name);
                if (inputData.source == NetworkFields.Source.TrackData) {
                    trackData = util.Vector3ToTrackData(value, trackData);
                } else {
                    trackData.userDefinedData = util.Vector3ToUserDefinedData(value, trackData.userDefinedData);
                }
            }

            public void SetVector3(GMKeyCode code, Vector3 value) {
                InputData inputData = GetInputData(code);
                if (inputData.source == NetworkFields.Source.TrackData) {
                    trackData = util.Vector3ToTrackData(value, trackData);
                } else {
                    trackData.userDefinedData = util.Vector3ToUserDefinedData(value, trackData.userDefinedData);
                }
            }

            public void SetQuaternion(string name, Quaternion value) {
                InputData inputData = GetInputData(name);
                if (inputData.source == NetworkFields.Source.TrackData) {
                    trackData = util.QuaternionToTrackData(value, trackData);
                } else {
                    trackData.userDefinedData = util.QuaternionToUserDefinedData(value, trackData.userDefinedData);
                }
            }

            public void SetQuaternion(GMKeyCode code, Quaternion value) {
                InputData inputData = GetInputData(code);
                if (inputData.source == NetworkFields.Source.TrackData) {
                    trackData = util.QuaternionToTrackData(value, trackData);
                } else {
                    trackData.userDefinedData = util.QuaternionToUserDefinedData(value, trackData.userDefinedData);
                }
            }

            public void SetInt(string name, int value) {
                InputData inputData = GetInputData(name);
                SetRemoteInt(inputData, value);
            }

            public void SetInt(GMKeyCode code, int value) {
                InputData inputData = GetInputData(code);
                SetRemoteInt(inputData, value);
            }

            public void SetFloat(string name, float value) {
                InputData inputData = GetInputData(name);
                SetRemoteInt(inputData, util.FloatToInt(value, false));
            }

            public void SetFloat(GMKeyCode code, float value) {
                InputData inputData = GetInputData(code);
                SetRemoteInt(inputData, util.FloatToInt(value, false));
            }

            public void SetBool(string name, bool value) {
                InputData inputData = GetInputData(name);
                SetRemoteInt(inputData, value == true ? 1 : 0);
            }

            public void SetBool(GMKeyCode code, bool value) {
                InputData inputData = GetInputData(code);
                SetRemoteInt(inputData, value == true ? 1 : 0);
            }
            
            // internal

            private InputData GetInputData(GMKeyCode code) {
                if (inputDatasByKeycode.ContainsKey(code)) {
                    return inputDatasByKeycode[code];
                } else {
                    throw new Exception("No input field with name " + name + " found");
                }
            }

            private InputData GetInputData(string name) {
                if (inputDatas.ContainsKey(name)) {
                    return inputDatas[name];
                } else {
                    throw new Exception("No input field with name " + name + " found");
                }
            }

            private object GetRemoteObject(InputData inputData) {
                if (inputData.source == NetworkFields.Source.TrackData) {
                    return inputData.remoteField.GetValue(trackData, null);
                } else {
                    return inputData.remoteField.GetValue(trackData.userDefinedData, null);
                }
            }

            private int GetRemoteInt(InputData inputData) {
                return Convert.ToInt32(GetRemoteObject(inputData));
            }

            private float GetRemoteFloat(InputData inputData) {
                return (float)GetRemoteObject(inputData);
            }

            private void SetRemoteInt(InputData inputData, int value) {
                if (inputData.source == NetworkFields.Source.TrackData) {
                    inputData.remoteField.SetValue(trackData, value, null);
                } else {
                    TrackData td;
                    if (inputData.broadcast) {
                        if (NetworkSettings.instance == null) {
                            return;
                        }
                        td = new TrackData();
                        td.broadcast = 1;
                        td.userDefinedData = new UserDefinedData();
                        inputData.remoteField.SetValue(td.userDefinedData, value, null);
                        trackData.id = NetworkSettings.instance.username;
                        io.gamemachine.messages.Entity entity = new io.gamemachine.messages.Entity();
                        entity.id = "0";
                        entity.trackData = trackData;
                        ActorSystem.instance.client.SendEntity(entity);
                    } else {
                        inputData.remoteField.SetValue(trackData.userDefinedData, value, null);
                    }
                    
                }
            }

            private void SetLocalIntFromRemote(InputData inputData) {
                int value = GetRemoteInt(inputData);
                if (inputData.broadcast && value == 0) {
                    return;
                }
                inputData.localField.SetValue(this, value);
            }

            private void SetLocalFloatFromRemote(InputData inputData) {
                float value = GetRemoteFloat(inputData);
                inputData.localField.SetValue(this, value);
            }

            private void SetLocalFloatFromRemoteIntFloat(InputData inputData) {
                float value = util.IntToFloat(GetRemoteInt(inputData), false);
                if (inputData.broadcast && value == 0f) {
                    return;
                }
                inputData.localField.SetValue(this, value);
            }

            private void SetLocalBoolFromRemote(InputData inputData) {
                int value = GetRemoteInt(inputData);
                if (value > 0) {
                    inputData.localField.SetValue(this, true);
                } else if (!inputData.broadcast) {
                    inputData.localField.SetValue(this, false);
                }
            }

            private void CreateInputData() {
                FieldInfo field;
                PropertyInfo remoteField;
                FieldInfo localField;
                bool broadcast;
                string localName;
                string remoteName;
                string friendlyName;
                BindingFlags bindFlags = BindingFlags.Instance | BindingFlags.Public | BindingFlags.NonPublic
                | BindingFlags.Static;
                NetworkFields.FieldType fieldType = NetworkFields.FieldType.None;
                NetworkFields.Source source;
                InputData inputData = new InputData();

                foreach (NetworkFields.SynchronizedField syncField in System.Enum.GetValues(typeof(NetworkFields.SynchronizedField))) {
                    string name = syncField.ToString();
                    broadcast = false;
                    friendlyName = "";
                    string fieldName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1);
                    string propName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1) + "Enabled";
                    field = this.GetType().GetField(propName);


                    if ((bool)field.GetValue(this)) {
                        if (name.StartsWith("Userdef")) {
                            source = NetworkFields.Source.UserDefinedData;
                        } else {
                            source = NetworkFields.Source.TrackData;
                        }

                        if (name.StartsWith("UserdefValue")) {
                            //Debug.Log("Value field");
                            string typeName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1) + "Type";
                            NetworkFields.UserDefValue userdefValue = (NetworkFields.UserDefValue)this.GetType().GetField(typeName).GetValue(this);
                            if (userdefValue == NetworkFields.UserDefValue.Int) {
                                fieldType = NetworkFields.FieldType.Int;
                            } else if (userdefValue == NetworkFields.UserDefValue.BroadcastInt) {
                                fieldType = NetworkFields.FieldType.Int;
                                broadcast = true;
                            } else if (userdefValue == NetworkFields.UserDefValue.Float) {
                                fieldType = NetworkFields.FieldType.IntFloat;
                            } else if (userdefValue == NetworkFields.UserDefValue.BroadcastFloat) {
                                fieldType = NetworkFields.FieldType.IntFloat;
                                broadcast = true;
                            } else if (userdefValue == NetworkFields.UserDefValue.Bool) {
                                fieldType = NetworkFields.FieldType.Bool;
                            } else if (userdefValue == NetworkFields.UserDefValue.BroadcastBool) {
                                fieldType = NetworkFields.FieldType.Bool;
                                broadcast = true;
                            } else {
                                throw new Exception("Invalid value");
                            }

                            localName = name.Replace("UserdefValue", "userdef" + userdefValue.ToString().Replace("Broadcast", ""));
                            remoteName = name.Replace("UserdefValue", "userdefInt");

                            localField = this.GetType().GetField(localName, bindFlags);
                            remoteField = typeof(UserDefinedData).GetProperty(remoteName, bindFlags);

                            friendlyName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1) + "Name";
                            
                            friendlyName = (string)this.GetType().GetField(friendlyName).GetValue(this);
                            
                        } else {
                            if (name.Contains("Position") || name.Contains("Vector3")) {
                                fieldType = NetworkFields.FieldType.Vector;
                            } else if (name.Contains("Rotation") || name.Contains("Quaternion")) {
                                fieldType = NetworkFields.FieldType.Quaternion;
                            } else if (name.Contains("Hidden")) {
                                fieldType = NetworkFields.FieldType.Bool;
                            } else if (name.Contains("VelX")) {
                                fieldType = NetworkFields.FieldType.Float;
                            } else if (name.Contains("VelZ")) {
                                fieldType = NetworkFields.FieldType.Float;
                            } else {
                                fieldType = NetworkFields.FieldType.IntFloat;
                            }
                            localName = fieldName;
                            localField = this.GetType().GetField(fieldName, bindFlags);
                            remoteName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1);
                            if (source == NetworkFields.Source.TrackData) {
                                remoteField = typeof(TrackData).GetProperty(remoteName, bindFlags);
                            } else {
                                remoteField = typeof(UserDefinedData).GetProperty(remoteName, bindFlags);
                            }
                        }

                        //Debug.Log(remoteName + ":" + source.ToString()+" broadcast:"+broadcast);
                        //if (localField == null) {
                        //    throw new Exception("localField " + localName + " is null");
                        //}
                        //if (remoteField == null) {
                        //    Debug.Log("remoteField " + localName + " is null");
                        //}

                        if (!string.IsNullOrEmpty(friendlyName)) {
                            inputData.name = friendlyName;
                            //Debug.Log("friendlyName: " + friendlyName);
                        } else {
                            inputData.name = name;
                        }

                        inputData.keycode = (GMKeyCode)Enum.Parse(typeof(GMKeyCode), inputData.name);    
                        inputData.syncField = syncField;
                        inputData.source = source;
                        inputData.remoteField = remoteField;
                        inputData.localField = localField;
                        inputData.fieldType = fieldType;
                        inputData.broadcast = broadcast;
                        inputDatas[inputData.name] = inputData;
                        inputDatasByKeycode[inputData.keycode] = inputData;
                    }
                }
            }

            
        }
    }
}
