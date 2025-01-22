package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator {
    public BishopMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validBishopMoves = new ArrayList<ChessMove>();

        boolean blockage = false;
        int i = 0;
        int j = 0;

        while (!blockage) { // up and right
            i++;
            j++;
            boolean capture = true;
            if ((myPosition.getRow() + i <= 8) && (myPosition.getColumn() + j <= 8)) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                    myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                            myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        i = 0;
        j = 0;
        while (!blockage) { // down and right
            i--;
            j++;
            boolean capture = true;
            if ((myPosition.getRow() + i >= 1) && (myPosition.getColumn() + j <= 8)) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                            myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        i = 0;
        j = 0;
        while (!blockage) { // up and left
            i++;
            j--;
            boolean capture = true;
            if ((myPosition.getRow() + i <= 8) && (myPosition.getColumn() + j >= 1)) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                            myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }
        blockage = false;
        i = 0;
        j = 0;
        while (!blockage) { // down and left
            i--;
            j--;
            boolean capture = true;
            if ((myPosition.getRow() + i >= 1) && (myPosition.getColumn() + j >= 1)) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i,
                        myPosition.getColumn() + j)).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                            myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }

        return validBishopMoves;
    }
}
