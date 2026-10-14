package model;

import chess.ChessGame;

public record GameJoinData(ChessGame.TeamColor playerColor, int gameID) {
}
