using UnityEngine;
using System.Collections;
using UnityEditor;
using GameMachine;
using System.Collections.Generic;
using Ionic.Zip;
using System.IO;
using System.Linq;
using GameMachine.Common;
using System.Text;

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
                if (GUILayout.Button("Build Game Machine", GUILayout.Width(300))) {
                    RunGameMachine("build clean", serverConfig.configName);
                }
            }



            EditorGUILayout.Separator();

            EditorGUILayout.Separator();
            EditorGUILayout.LabelField("Unity server", EditorStyles.boldLabel);
            prop = so.FindProperty("buildScenes");
            EditorGUILayout.PropertyField(prop, true); // True means show children
            EditorGUILayout.Separator();

            prop = so.FindProperty("runHeadless");
            prop.boolValue = EditorGUILayout.Toggle("Run headless", prop.boolValue);
            EditorGUILayout.Separator();

            buildTarget = (BuildTarget)EditorGUILayout.EnumPopup("Target", buildTarget);
            EditorGUILayout.BeginVertical(GUILayout.Height(40));
            EditorGUILayout.Space();
            EditorGUILayout.EndVertical();

            if (TimeUtil.EpochTime() - lastUpdate > 3) {
                lastUpdate = TimeUtil.EpochTime();
                ProcessTracker.UpdateProcesses();
            }

            if (ProcessTracker.HasProcess(unityProcess)) {
                if (GUILayout.Button("Stop", GUILayout.Width(300))) {
                    ProcessTracker.Stop(unityProcess);
                }
            } else {
                if (File.Exists(ExePath())) {
                    if (GUILayout.Button("Start", GUILayout.Width(300))) {
                        StartUnity(so.FindProperty("runHeadless").boolValue);
                    }
                }
                if (buildTarget == BuildTarget.StandaloneWindows ||
                    buildTarget == BuildTarget.StandaloneWindows64 ||
                    buildTarget == BuildTarget.StandaloneOSXIntel ||
                    buildTarget == BuildTarget.StandaloneOSXIntel64 ||
                    buildTarget == BuildTarget.StandaloneOSXUniversal) {

                    if (GUILayout.Button("Build", GUILayout.Width(300))) {
                        Build();
                    }
                }
            }

            if (File.Exists(Logfile())) {
                if (GUILayout.Button("View log", GUILayout.Width(300))) {
                    System.Diagnostics.Process.Start("notepad.exe", Logfile());
                }
            }
            

        }

        private void RunGameMachine(string command, string config) {
            string envFile = serverConfig.serverPath + "/env.dat";
            string clientMessages = Application.dataPath + "/GameMachine/Scripts/messages.cs";
            string serverMessages = serverConfig.serverPath + "/messages.cs";
            string fileName;

            if (IsWindows()) {
                fileName = serverConfig.serverPath + "/bin/game_machine_simple.bat";
            } else {
                fileName = serverConfig.serverPath + "/bin/game_machine.sh";
            }

            if (!string.IsNullOrEmpty(config)) {
                File.WriteAllText(envFile, config);
            }

            string workingDirectory = serverConfig.serverPath;
            string arguments = " " + command;

            System.Diagnostics.Process proc = ProcessTracker.Start(gameMachineProcess, fileName, arguments, workingDirectory);

            if (command == "build clean") {
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

        private void Build() {
            List<string> scenePaths = new List<string>();
            serverConfig.buildScenes.ForEach(scene => scenePaths.Add(AssetDatabase.GetAssetPath(scene)));
           
            EnsurePath();
            BuildOptions options = BuildOptions.Development;
            BuildPipeline.BuildPlayer(scenePaths.ToArray(), ExePath(), buildTarget, options);
        }

        private void StartUnity(bool headless) {
            string fileName = ExePath();
            string arguments = " -logFile ./logfile";

            if (headless) {
                arguments = " -batchmode -nographcis -logFile ./logfile";
            }
            
            string workingDirectory = Path();
            ProcessTracker.Start(unityProcess, fileName, arguments, workingDirectory);
        }

        private void EnsurePath() {
            if (!Directory.Exists(Path())) {
                Directory.CreateDirectory(Path());
            }
        }

        private string Logfile() {
            return Path() + "/logfile";
        }

        private string ExePath() {
            if (buildTarget.ToString().Contains("Windows")) {
                return Path() + "/" + buildName + ".exe";
            } else {
                return Path() + "/" + buildName;
            }
        }

        private string Path() {
            return System.IO.Path.GetFullPath(Application.dataPath + "/../GameMachine");
        }


        private void ZipBuild() {
            using (ZipFile zip = new ZipFile()) {
                zip.AddDirectory(Path());
                zip.CompressionLevel = Ionic.Zlib.CompressionLevel.BestCompression;
                zip.Comment = "This zip was created at " + System.DateTime.Now.ToString("G");
                zip.Save(string.Format("test{0}.zip", 1));
            }
        }
    }
}
