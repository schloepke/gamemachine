using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace GameMachine {
    public class NpcName : MonoBehaviour {

        public static System.Random rnd = new System.Random();

        private static List<string> consonants = new List<string>() {
            "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "x", "y", "z"
        };

        private static List<string> vowels = new List<string>() {
            "a","i","e","o","u"
        };

        private static List<string> endings = new List<string>() {
           "ith", "ton", "on", "field", "man"
        };

        private static string Pick(List<string> names) {
            int index = rnd.Next(names.Count);
            return names[index];
        }

        public static string GetName() {
            return Pick(consonants) + Pick(vowels) + Pick(consonants) + Pick(endings);
        }

        public static Queue<string> GetNames(int count) {
            List<string> names = new List<string>();
            for (int n = 0; n < 10; n++) {
                for (int i = 0; i < 1000; i++) {
                    string name = GetName();
                    names.Add(name);
                }
                names = names.Distinct().Take(count).ToList();
                if (names.Count >= count) {
                    Queue<string> queue = new Queue<string>();
                    foreach (string name in names.Distinct().Take(count)) {
                        queue.Enqueue(name);
                    }
                    return queue;
                }

            }
            throw new UnityException("Unable to create enough names " + names.Count);
        }
    }
}
