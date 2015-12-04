using UnityEngine;
using UnityEditor;
using System.Collections.Generic;
using System.IO;
using System;

namespace GameMachine {
    [CustomEditor(typeof(ServerConfig))]
    public class ServerConfigEditor : Editor {

        private string gameMachineProcess = "game_machine";
        private string unityProcess = "unity";
        private SerializedProperty prop;
        private SerializedObject so;
        private ServerConfig serverConfig;
        private string buildName = "unityServer";
        private BuildTarget buildTarget;
        private double lastUpdate = 0D;
        private string logContent;
        private Vector2 scrollPos;

        void OnEnable() {

            so = new SerializedObject(target);
            lastUpdate = TimeUtil.EpochTime();
        }

        public override void OnInspectorGUI() {
            if (so == null) {
                return;
            }

            so.Update();
            serverConfig = target as ServerConfig;

            ShowConfig();
            //DrawDefaultInspector();
            if (so != null) {
                so.ApplyModifiedProperties();
            }
        }

        private void ShowConfig() {

            EditorGUILayout.LabelField("Game Machine server", EditorStyles.boldLabel);
            prop = so.FindProperty("serverPath");
            prop.stringValue = EditorGUILayout.TextField("Server path", prop.stringValue);
            PlayerPrefs.SetString("serverPath", prop.stringValue);

            prop = so.FindProperty("configName");
            prop.stringValue = EditorGUILayout.TextField("Config name", prop.stringValue);

            EditorGUILayout.Separator();

            if (ProcessTracker.HasProcess(gameMachineProcess)) {
                //if (GUILayout.Button("Stop Server", GUILayout.Width(300))) {
                //    ProcessTracker.Stop(gameMachineProcess);
                //}
                EditorGUILayout.LabelField("Game Machine is running");
            } else {
                if (GUILayout.Button("Start Game Machine", GUILayout.Width(300))) {
                    RunGameMachine("server", serverConfig.configName);
                }
                if (GUILayout.Button("Build Game Machine (full)", GUILayout.Width(300))) {
                    RunGameMachine("build clean", serverConfig.configName);
                }

                if (GUILayout.Button("Build Game Machine (compile only)", GUILayout.Width(300))) {
                    RunGameMachine("build compile", serverConfig.configName);
                }
            }



            EditorGUILayout.Separator();

            EditorGUILayout.Separator();
            EditorGUILayout.LabelField("Unity server", EditorStyles.boldLabel);
            prop = so.FindProperty("buildScenes");
            EditorGUILayout.PropertyField(prop, true);
            EditorGUILayout.Separator();

            prop = so.FindProperty("runHeadless");
            prop.boolValue = EditorGUILayout.Toggle("Run headless", prop.boolValue);
            EditorGUILayout.Separator();

           

            buildTarget = (BuildTarget)EditorGUILayout.EnumPopup("Target", buildTarget);
            EditorGUILayout.BeginVertical(GUILayout.Height(20));
            EditorGUILayout.Space();
            EditorGUILayout.EndVertical();

            if (TimeUtil.EpochTime() - lastUpdate > 3) {
                lastUpdate = TimeUtil.EpochTime();
                ProcessTracker.UpdateProcesses();
            }
           

            if (ProcessTracker.HasProcess(unityProcess)) {
                if (GUILayout.Button("Stop Unity", GUILayout.Width(300))) {
                    ProcessTracker.Stop(unityProcess);
                }
            } else {
                if (buildTarget == BuildTarget.StandaloneWindows ||
                    buildTarget == BuildTarget.StandaloneWindows64 ||
                    buildTarget == BuildTarget.StandaloneLinux ||
                    buildTarget == BuildTarget.StandaloneLinux64 ||
                    buildTarget == BuildTarget.StandaloneOSXIntel ||
                    buildTarget == BuildTarget.StandaloneOSXIntel64 ||
                    buildTarget == BuildTarget.StandaloneOSXUniversal) {

                    if (GUILayout.Button("Build", GUILayout.Width(300))) {
                        if (Directory.Exists(PublishPath())) {
                            Directory.Delete(PublishPath(), true);
                        }
                        Build();
                    }
                }

                if (File.Exists(ExePath())) {
                    if (GUILayout.Button("Start single instance", GUILayout.Width(300))) {
                        StartUnity(so.FindProperty("runHeadless").boolValue);
                    }
                }
            }

            if (File.Exists(Logfile())) {
                if (GUILayout.Button("View log", GUILayout.Width(300))) {
                    System.Diagnostics.Process.Start("notepad.exe", Logfile());
                }
            }

            EditorGUILayout.BeginVertical(GUILayout.Height(20));
            EditorGUILayout.Space();
            EditorGUILayout.EndVertical();

            EditorGUILayout.LabelField("Unity process manager", EditorStyles.boldLabel);
            EditorGUILayout.Separator();

            prop = so.FindProperty("deployCount");
            prop.intValue = EditorGUILayout.IntField("Deploy instance count", prop.intValue);
            EditorGUILayout.Separator();


            if (File.Exists(ExePath())) {
                if (GUILayout.Button("Deploy " + serverConfig.deployCount + " instances to process manager", GUILayout.Width(300))) {
                    Deploy();
                }
            }

        }

        private void RunGameMachine(string command, string config) {
            string envFile = serverConfig.serverPath + "/env.dat";
            string clientMessages = Application.dataPath + "/GameMachine/Scripts/messages.cs";
            string serverMessages = serverConfig.serverPath + "/messages.cs";
            string fileName;

            if (IsWindows()) {
                fileName = serverConfig.serverPath + "/bin/game_machine.bat";
            } else {
                fileName = serverConfig.serverPath + "/bin/game_machine.sh";
            }

            if (!string.IsNullOrEmpty(config)) {
                File.WriteAllText(envFile, config);
            }

            string workingDirectory = serverConfig.serverPath;
            string arguments = " " + command;

            System.Diagnostics.Process proc = ProcessTracker.Start(gameMachineProcess, fileName, arguments, workingDirectory);

            if (command.StartsWith("build")) {
                proc.WaitForExit();
                ProcessTracker.RemoveProcess(proc.Id);
                Debug.Log("Copying messages.cs");
                if (File.Exists(serverMessages)) {
                    File.Delete(clientMessages);
                    File.Copy(serverMessages, clientMessages);
                }
            }
        }
        
        public static bool IsWindows() {
            return System.IO.Path.DirectorySeparatorChar == '\\';
        }

        private void Deploy() {
            string baseDir = serverConfig.serverPath + "/process_manager/unity";
            if (Directory.Exists(baseDir)) {
                Directory.Delete(baseDir, true);
            }

            for (int i = 0; i < serverConfig.deployCount; i++) {
                string targetDir = serverConfig.serverPath + "/process_manager/unity/instance" + i;
                FileUtils.DirectoryCopy(PublishPath(), targetDir, true);
                CreateStartFile(i, "windows");
                CreateStartFile(i, "linux");

                string exeOld;
                string exeNew;

                string dataOld = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer_Data";
                string dataNew = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer" + i + "_Data";

                Directory.Move(dataOld, dataNew);

                if (IsWindowsBuild()) {
                    exeOld = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer.exe";
                    exeNew = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer" + i + ".exe";
                } else {
                    exeOld = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer";
                    exeNew = serverConfig.serverPath + "/process_manager/unity/instance" + i + "/unityServer" + i;
                }
                Directory.Move(exeOld, exeNew);
            }
        }

        private void Build() {
            List<string> scenePaths = new List<string>();
            serverConfig.buildScenes.ForEach(scene => scenePaths.Add(AssetDatabase.GetAssetPath(scene)));

            EnsurePublishPath();
            BuildOptions options = BuildOptions.Development;
            BuildPipeline.BuildPlayer(scenePaths.ToArray(), ExePath(), buildTarget, options);
        }

        private void StartUnity(bool headless) {
            string fileName = ExePath();
            string arguments = " -logFile ./logfile";

            if (headless) {
                arguments = " -batchmode -nographics -logFile ./logfile";
            }

            string workingDirectory = PublishPath();
            ProcessTracker.Start(unityProcess, fileName, arguments, workingDirectory);
        }

        private void EnsurePublishPath() {
            if (!Directory.Exists(PublishPath())) {
                Directory.CreateDirectory(PublishPath());
            }
        }

        private string Logfile() {
            return PublishPath() + "/logfile";
        }

        private string ExePath() {
            if (buildTarget.ToString().Contains("Windows")) {
                return PublishPath() + "/" + buildName + ".exe";
            } else {
                return PublishPath() + "/" + buildName;
            }
        }

        private string PublishPath() {
            return System.IO.Path.GetFullPath(Application.dataPath + "/../game_machine_publish");
        }

        private bool IsWindowsBuild() {
            return (buildTarget == BuildTarget.StandaloneWindows || buildTarget == BuildTarget.StandaloneWindows64);
        }

        private void CreateStartFile(int instance, string os) {

            string startFile;
            List<string> lines = new List<string>();
            string newline = Environment.NewLine;
            if (os == "windows") {
                lines.Add("@echo off");
                lines.Add("set UNITY_HOME=%~dp0");
                lines.Add("echo %UNITY_HOME%");
                lines.Add(@"""%UNITY_HOME%unityServer" + instance + @".exe"" -batchmode -nographics -logFile ./logfile");
                startFile = serverConfig.serverPath + "/process_manager/unity/instance" + instance + "/start.bat";
            } else {
                newline = "\n";
                lines.Add("#!/bin/bash");
                lines.Add(@"DIR=$( cd ""$(dirname ""${BASH_SOURCE[0]}"" )"" && pwd )");
                lines.Add(@"$DIR/unityServer" + instance + @" -batchmode -nographics -logFile ./logfile");
                startFile = serverConfig.serverPath + "/process_manager/unity/instance" + instance + "/start.sh";
            }
            File.WriteAllText(startFile, string.Join(newline, lines.ToArray()));
        }
    }
}
