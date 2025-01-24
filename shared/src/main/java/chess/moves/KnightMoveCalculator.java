package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {
                { 1,  2},
                { 2,  1},
                {-1,  2},
                { 2, -1},
                { 1, -2},
                {-2,  1},
                {-1, -2},
                {-2, -1}
        };
        for (int[] dir : directions) {
            if (myPosition.getRow() + dir[0] >= 1 && myPosition.getRow() + dir[0] <= 8 &&
            myPosition.getColumn() + dir[1] >= 1 && myPosition.getColumn() + dir[1] <= 8) {
                ChessPiece me = board.getPiece(myPosition);
                ChessPiece them = board.getPiece(new ChessPosition(myPosition.getRow() + dir[0], myPosition.getColumn() + dir[1]));
                if (them == null || me.getTeamColor() != them.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + dir[0], myPosition.getColumn() + dir[1]), null));
                }
            }
        }
        return moves;
    }
}
