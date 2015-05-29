using UnityEngine;
using System.Collections;

using Characters = io.gamemachine.messages.Characters;
using Character = io.gamemachine.messages.Character;
using Players = io.gamemachine.messages.Players;
using Player = io.gamemachine.messages.Player;

public interface IAdminApi {

    void OnPlayerCharacters(Characters characters);
    void OnPlayerCharactersError(string error);

    void OnPlayerDeleted(string result);
    void OnPlayerDeleteError(string error);

    void OnPlayerPasswordSet(string result);
    void OnPlayerPasswordError(string error);

    void OnCharacterSearch(Characters characters);
    void OnCharacterSearchError(string error);

    void OnPlayerSearch(Players players);
    void OnPlayerSearchError(string error);

    
}
