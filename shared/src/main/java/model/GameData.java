package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, Boolean gameOver) {
    public GameData updateBlackUsername(String username) {
        return new GameData(
            gameID,
            whiteUsername,
            username,
            gameName,
            game,
            gameOver
        );
    }

    public GameData updateWhiteUsername(String username) {
        return new GameData(
                gameID,
                username,
                blackUsername,
                gameName,
                game,
                gameOver
        );
    }
}
