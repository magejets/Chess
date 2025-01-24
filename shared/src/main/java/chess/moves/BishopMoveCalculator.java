package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {
                { 1,  1},
                {-1,  1},
                { 1, -1},
                {-1, -1}
        };
        var calc = new SlideMoveCalculator();
        return calc.pieceMoves(board, myPosition, directions);
    }
}
