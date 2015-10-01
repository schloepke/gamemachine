//using UnityEngine;
//using System.Collections;
//using UMA;

//public class UMASpawnHandler : MonoBehaviour {
//    public string characterSavePath = "Assets/Character Saves";

//    public string fallbackCharacter = "TestCharacter";

//    public UMARecipeBase[] additionalRecipes;

//    public string characterName = "TestCharacter";
//    public string mainScene = "demo";

//    void Start() {
//        DontDestroyOnLoad(gameObject);

//        if (characterName.Length != 0) {
//            var avatar = gameObject.GetComponent<UMAAvatarBase>();
//            if (avatar != null) {
//                string finalPath = characterSavePath + "/" + characterName + ".txt";
//                var asset = ScriptableObject.CreateInstance<UMATextRecipe>();
//                asset.recipeString = System.IO.File.ReadAllText(finalPath);
//                if (asset != null) {
//                    avatar.Initialize();
//                    avatar.Load(asset);
//                    avatar.Load(asset, additionalRecipes);
//                    Application.LoadLevel(mainScene);
//                } else {
//                    Debug.LogError("Failed To Load Asset \"" + finalPath + "\"\nAssets must be inside the project and descend from the UMARecipeBase type");
//                }

//            }
//        } else {
//            Debug.LogError("Couldn't find a character to load. Make sure the character save path is correct and that the name override is blank if you are using the character creator.");
//        }
//    }
//}
