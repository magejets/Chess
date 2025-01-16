package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator {
    public KnightMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validKnightMoves = new ArrayList<ChessMove>();

        int[][] moves = new int[][] {
                { 1,  2},
                { 2,  1},
                { 1, -2},
                {-2,  1},
                {-1,  2},
                { 2, -1},
                {-1, -2},
                {-2, -1}
        };
        for (int[] i : moves) {
            if (myPosition.getRow()    + i[0] <= 8 && myPosition.getRow()    + i[0] >= 1 &&
                    myPosition.getColumn() + i[1] <= 8 && myPosition.getColumn() + i[1] >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1])) == null) {
                    validKnightMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1]), null));
                } else if (board.getPiece(new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1])).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validKnightMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1]), null));
                }
            }
        }

        return validKnightMoves;
    }
}
