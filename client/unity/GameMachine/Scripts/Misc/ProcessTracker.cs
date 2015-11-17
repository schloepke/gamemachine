using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using UnityEngine;

namespace GameMachine {
    public class ProcessTracker {
        public static Dictionary<int, string> processes = new Dictionary<int, string>();

        public static Process Start(string name, string fileName, string arguments, string workingDirectory) {
            if (HasProcess(name)) {
                throw new System.Exception("Process already exists " + name);
            }

            var startInfo = new System.Diagnostics.ProcessStartInfo();
            startInfo.FileName = fileName;
            startInfo.Arguments = arguments;
            startInfo.WorkingDirectory = workingDirectory;
            Process process = Process.Start(startInfo);
            AddProcess(process, name);
            return process;
        }

        public static void Stop(string name) {
            Load();
            List<Process> running = Process.GetProcesses().ToList();
            KeyValuePair<int, string> entry = processes.Where(pair => pair.Value == name).FirstOrDefault();
            Process process = running.Where(pr => pr.Id == entry.Key).FirstOrDefault();

            if (process != null) {
                UnityEngine.Debug.Log("Killing process " + name);
                process.Kill();
            } else {
                UnityEngine.Debug.Log("Process not found " + name);
            }

            RemoveProcess(entry.Key);
        }

        public static void AddProcess(Process process, string name) {
            Load();
            processes.Add(process.Id, name);
            Save();
        }

        public static void RemoveProcess(int id) {
            Load();
            processes.Remove(id);
            Save();
        }

        public static void UpdateProcesses() {
            List<Process> running = Process.GetProcesses().ToList();
            foreach (Process process in running) {
                if (process.MainWindowTitle.Contains("Game Machine")) {
                    UnityEngine.Debug.Log("Game Machine running");
                }
            }
            Load();
            foreach (KeyValuePair<int, string> pair in processes) {
                Process process = running.Where(pr => pr.Id == pair.Key).FirstOrDefault();
                if (process == null) {
                    UnityEngine.Debug.Log("Removing process entry " + pair.Value);
                    RemoveProcess(pair.Key);
                }
            }
            Save();
        }

        public static bool HasProcess(string name) {
            Load();
            return processes.Any(pair => pair.Value == name);
        }

        public static void Load() {
            if (File.Exists(DataFile())) {
                string data = File.ReadAllText(DataFile());
                processes = JsonConvert.DeserializeObject<Dictionary<int, string>>(data);
            } else {
                UnityEngine.Debug.Log("datafile not found");
                processes = new Dictionary<int, string>();
            }
        }

        public static void Save() {
            string data = JsonConvert.SerializeObject(processes);
            File.WriteAllText(DataFile(), data);
        }

        private static string DataFile() {
            return System.IO.Path.GetFullPath(Application.dataPath + "/../processes.json");
        }
    }
}