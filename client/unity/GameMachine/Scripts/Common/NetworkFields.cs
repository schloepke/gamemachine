using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using GameMachine.Common;
using System;
using System.Linq;
using io.gamemachine.messages;
using Newtonsoft.Json;
using GameMachine.Core;

namespace GameMachine {
    public class NetworkFields {
        
        public class FieldSet {
            public bool active = false;
            public GmField gmField;
            public FieldType fieldType;
            public GMKeyCode keyCode;
            public bool broadcast = false;
            public bool fixedField = false;
            public int userCode;
        }

        public enum GmField {
            Position,
            Vaxis,
            Haxis,
            Yaxis,
            Speed,
            Hidden,
            SecondaryVector3,
            Rotation,
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

        public enum FieldType {
            None,
            Int,
            BroadcastInt,
            Float,
            BroadcastFloat,
            Bool,
            BroadcastBool
        }

        public Dictionary<GmField, FieldSet> fieldMap = new Dictionary<GmField, FieldSet>();
        public Dictionary<GMKeyCode, FieldSet> codeMap = new Dictionary<GMKeyCode, FieldSet>();

        [JsonIgnore]
        private TrackData trackData;

        [JsonIgnore]
        private Vector3 position = Vector3.zero;

        public void Init() {
            ResetTrackData();
            foreach (FieldSet set in fieldMap.Values) {
                codeMap[set.keyCode] = set;
            }
        }

        public void UpdateFromNetwork(TrackData trackData, bool hasDelta) {
            this.trackData = trackData;
            if (trackData.broadcast != 1) {
                if (hasDelta) {
                    position = GmUtil.TrackdataDeltaToVector3(trackData, position);
                } else {
                    position = GmUtil.TrackdataToVector3(trackData);
                }
            }
        }

        public TrackData GetTrackData() {
            return trackData;
        }

        public bool GetBool(GMKeyCode code) {
            FieldSet set = codeMap[code];
            int value = GetUserValue(set.gmField);
            return (value == 0 ? false : true);
        }

        public int GetInt(GMKeyCode code) {
            FieldSet set = codeMap[code];
            return GetUserValue(set.gmField);
        }

        public float GetFloat(GMKeyCode code) {
            FieldSet set = codeMap[code];
            int value = GetUserValue(set.gmField);
            return GmUtil.IntToFloat(value);
        }

        public Vector3 GetPosition() {
            return position;
        }

        public Vector3 GetSecondaryVector3() {
            return GmUtil.UserDefinedDataToVector3(trackData.userDefinedData);
        }

        public Quaternion GetRotation() {
            return GmUtil.TrackdataToQuaternion(trackData);
        }

        public void SetPosition(Vector3 value) {
            trackData = GmUtil.Vector3ToTrackData(value, trackData);
        }

        public void SetSecondaryVector3(Vector3 value) {
            trackData.userDefinedData = GmUtil.Vector3ToUserDefinedData(value, trackData.userDefinedData);
        }

        public void SetRotation(Quaternion value) {
            trackData = GmUtil.QuaternionToTrackData(value, trackData);
        }

        public void SetInt(GMKeyCode code, int value) {
            FieldSet set = codeMap[code];
            SetUserValue(set, value);
        }

        public void SetFloat(GMKeyCode code, float value) {
            FieldSet set = codeMap[code];
            int intValue = GmUtil.FloatToInt(value);
            SetUserValue(set, intValue);
        }

        public void SetBool(GMKeyCode code, bool value) {
            FieldSet set = codeMap[code];
            int intValue =  (value == true ? 1 : 0);
            SetUserValue(set, intValue);
        }

        public GMKeyCode FieldToKey(GmField field) {
            switch (field) {
                case GmField.Haxis:
                    return GMKeyCode.Haxis;
                case GmField.Vaxis:
                    return GMKeyCode.Vaxis;
                case GmField.Yaxis:
                    return GMKeyCode.Yaxis;
                case GmField.Position:
                    return GMKeyCode.Position;
                case GmField.Speed:
                    return GMKeyCode.PlayerSpeed;
                case GmField.Hidden:
                    return GMKeyCode.Hidden;
                case GmField.SecondaryVector3:
                    return GMKeyCode.SecondaryVector3;
                case GmField.Rotation:
                    return GMKeyCode.Rotation;
                default:
                    throw new UnityException("Invalid Gmfield");
            }
        }

        public bool IsFixedField(GmField customField) {
            if (
                customField == GmField.Position ||
                customField == GmField.Vaxis ||
                customField == GmField.Haxis ||
                customField == GmField.Yaxis ||
                customField == GmField.Speed ||
                customField == GmField.Hidden ||
                customField == GmField.Rotation ||
                customField == GmField.SecondaryVector3
            ) {
                return true;
            } else {
                return false;
            }
        }

        public List<GmField> FixedFields() {
            List<GmField> fields = Enum.GetValues(typeof(GmField)).Cast<GmField>().ToList();
            return fields.Where(k => IsFixedField(k)).ToList();
        }

        public List<GmField> UserFields() {
            List<GmField> fields = Enum.GetValues(typeof(GmField)).Cast<GmField>().ToList();
            return fields.Where(k => !IsFixedField(k)).ToList();
        }

        public List<GMKeyCode> Keycodes() {
            List<GMKeyCode> keycodes = Enum.GetValues(typeof(GMKeyCode)).Cast<GMKeyCode>().ToList();
            return keycodes.Where(k => k != GMKeyCode.Vaxis && k != GMKeyCode.Yaxis && k != GMKeyCode.Haxis && k != GMKeyCode.Position && k != GMKeyCode.Rotation &&
            k != GMKeyCode.Hidden && k != GMKeyCode.PlayerSpeed && k != GMKeyCode.SecondaryVector3).ToList();
        }

        public List<string> KeycodeNames() {
            return Keycodes().Select(k => k.ToString()).ToList();
        }

        private void ResetTrackData() {
            trackData = new TrackData();
            trackData.userDefinedData = new UserDefinedData();
        }

        private void Broadcast(UserDefinedData data) {
            if (NetworkSettings.instance == null) {
                return;
            }
            TrackData td = new TrackData();
            td.broadcast = 1;
            td.userDefinedData = data;
            trackData.id = NetworkSettings.instance.username;
            Entity entity = new Entity();
            entity.id = "0";
            entity.trackData = trackData;
            ActorSystem.instance.client.SendEntity(entity);
        }

        private int GetUserValue(GmField code) {
            UserDefinedData data = trackData.userDefinedData;
            switch (code) {
                case GmField.Haxis:
                    return trackData.haxis;
                case GmField.Vaxis:
                    return trackData.vaxis;
                case GmField.Yaxis:
                    return trackData.yaxis;
                case GmField.Speed:
                    return trackData.speed;
                case GmField.Hidden:
                    return trackData.hidden;
                case GmField.UserdefValue1:
                    return data.userdefInt1;
                case GmField.UserdefValue2:
                    return data.userdefInt2;
                case GmField.UserdefValue3:
                    return data.userdefInt3;
                case GmField.UserdefValue4:
                    return data.userdefInt4;
                case GmField.UserdefValue5:
                    return data.userdefInt5;
                case GmField.UserdefValue6:
                    return data.userdefInt6;
                case GmField.UserdefValue7:
                    return data.userdefInt7;
                case GmField.UserdefValue8:
                    return data.userdefInt8;
                case GmField.UserdefValue9:
                    return data.userdefInt9;
                case GmField.UserdefValue10:
                    return data.userdefInt10;
                case GmField.UserdefValue11:
                    return data.userdefInt11;
                case GmField.UserdefValue12:
                    return data.userdefInt12;
                case GmField.UserdefValue13:
                    return data.userdefInt13;
                case GmField.UserdefValue14:
                    return data.userdefInt14;
                case GmField.UserdefValue15:
                    return data.userdefInt15;
                default:
                    throw new UnityException("Invalid Gmfield");
            }
        }

        private void SetUserValue(FieldSet set, int value) {
            UserDefinedData data;
            if (set.broadcast) {
                data = new UserDefinedData();
            } else {
                data = trackData.userDefinedData;
            }

            switch (set.gmField) {
                case GmField.Haxis:
                    trackData.haxis = value;
                    break;
                case GmField.Vaxis:
                    trackData.vaxis = value;
                    break;
                case GmField.Yaxis:
                    trackData.yaxis = value;
                    break;
                case GmField.Speed:
                    trackData.speed = value;
                    break;
                case GmField.Hidden:
                    trackData.hidden = value;
                    break;
                case GmField.UserdefValue1:
                    data.userdefInt1 = value;
                    break;
                case GmField.UserdefValue2:
                    data.userdefInt2 = value;
                    break;
                case GmField.UserdefValue3:
                    data.userdefInt3 = value;
                    break;
                case GmField.UserdefValue4:
                    data.userdefInt4 = value;
                    break;
                case GmField.UserdefValue5:
                    data.userdefInt5 = value;
                    break;
                case GmField.UserdefValue6:
                    data.userdefInt6 = value;
                    break;
                case GmField.UserdefValue7:
                    data.userdefInt7 = value;
                    break;
                case GmField.UserdefValue8:
                    data.userdefInt8 = value;
                    break;
                case GmField.UserdefValue9:
                    data.userdefInt9 = value;
                    break;
                case GmField.UserdefValue10:
                    data.userdefInt10 = value;
                    break;
                case GmField.UserdefValue11:
                    data.userdefInt11 = value;
                    break;
                case GmField.UserdefValue12:
                    data.userdefInt12 = value;
                    break;
                case GmField.UserdefValue13:
                    data.userdefInt13 = value;
                    break;
                case GmField.UserdefValue14:
                    data.userdefInt14 = value;
                    break;
                case GmField.UserdefValue15:
                    data.userdefInt15 = value;
                    break;
                default:
                    throw new UnityException("Invalid Gmfield");
            }

            if (set.broadcast) {
                Broadcast(data);
            }
        }
    }
}
