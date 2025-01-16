package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator {
    public RookMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validRookMoves = new ArrayList<ChessMove>();

        boolean blockage = false;
        int i;
        int j;

        i = 0;
        while (!blockage) { // up
            i++;
            boolean capture = true;
            if (myPosition.getRow() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validRookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        i = 0;
        while (!blockage) { // down
            i--;
            boolean capture = true;
            if (myPosition.getRow() + i >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validRookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        j = 0;
        while (!blockage) { // right
            j++;
            boolean capture = true;
            if (myPosition.getColumn() + j <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validRookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        j = 0;
        while (!blockage) { // left
            j--;
            boolean capture = true;
            if (myPosition.getColumn() + j >= 1) {
                if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validRookMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }

        return validRookMoves;
    }
}
