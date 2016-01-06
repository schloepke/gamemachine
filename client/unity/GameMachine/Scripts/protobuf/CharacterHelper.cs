using io.gamemachine.messages;
using System.IO;
using UnityEngine;

namespace GameMachine {
    public class CharacterHelper {

        public static ItemSlots GetItemSlots(Character character) {
            if (character.itemSlotData == null) {
                Debug.Log("ItemSlotData is null");
                return new ItemSlots();
            }

            GmSerializer serializer = new GmSerializer();
            MemoryStream stream = new MemoryStream(System.Convert.FromBase64String(character.itemSlotData));
            return serializer.Deserialize(stream, new ItemSlots(), typeof(ItemSlots)) as ItemSlots;
        }

        public static void SetItemSlots(Character character, ItemSlots slots) {
            GmSerializer serializer = new GmSerializer();
            MemoryStream stream = new MemoryStream();
            serializer.Serialize(stream, slots);
            character.itemSlotData = System.Convert.ToBase64String(stream.ToArray());
        }
    }
}
