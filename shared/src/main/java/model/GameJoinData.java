package model;

import chess.ChessGame;

public record GameJoinData(ChessGame.TeamColor color, int gameID) {
}
