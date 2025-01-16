package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator {
    public QueenMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validQueenMoves = new ArrayList<ChessMove>();

        var diagonal = new BishopMoveCalculator();
        var straight = new RookMoveCalculator();

        validQueenMoves.addAll(straight.pieceMoves(board, myPosition));
        validQueenMoves.addAll(diagonal.pieceMoves(board, myPosition));

        return validQueenMoves;
    }
}
