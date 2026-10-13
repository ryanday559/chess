package dataaccess.memory;

import chess.ChessGame;
import dataaccess.GameDataAccess;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Random;

public class MemoryGameDataAccess implements GameDataAccess {
    private Map<Integer, GameData> gameMap = new HashMap<Integer, GameData>();

    public MemoryGameDataAccess(){
    }

    public Collection<GameData> getGames() {
        return gameMap.values();
    }

    public int createGame(String gameName) {
        int gameID = createGameID();
        ChessGame game = new ChessGame();
        gameMap.put(gameID, new GameData(gameID, null, null, gameName, game));
        return gameID;
    }

    public GameData getGame(int gameID) {
        return gameMap.get(gameID);
    }

    public void updatePlayerColor(ChessGame.TeamColor playerColor, String username, int gameID) {
        GameData gameData = getGame(gameID);
        if (playerColor == ChessGame.TeamColor.BLACK) {
            gameData = gameData.updateBlackUsername(username);
        }
        else {
            gameData = gameData.updateBlackUsername(username);
        }
        gameMap.put(gameID, gameData);
    }

    public void clear() {
        gameMap = new HashMap<Integer, GameData>();
    }

    private int createGameID() {
        Random rng = new Random();
        int gameID;
        do {
            gameID = rng.nextInt();
        } while (!gameMap.containsKey(gameID));
        return gameID;
    }
}
