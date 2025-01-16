package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator {
    public PawnMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validPawnMoves = new ArrayList<ChessMove>();
        int direction = board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        if (myPosition.getRow() == (direction == 1 ? 2 : 7)) { // initial move
            if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), null));
                if (board.getPiece(new ChessPosition(myPosition.getRow() + direction * 2, myPosition.getColumn())) == null) {
                    validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction * 2, myPosition.getColumn()), null));
                }
            }

        } else if (myPosition.getRow() == (direction == 1 ? 7 : 2)) { // promotion moving straight
            if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            }
        } else if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) { // non initial, non promotion moves
            validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), null));
        }
        for (int j = -1; j <= 1; j += 2) { // scan to both the left and right for capture logic
            if ((myPosition.getColumn() + j <= 8) && (myPosition.getColumn() + j >= 1)) {
                ChessPiece cornerPiece = board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j));
                if ((cornerPiece != null) && (cornerPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor())) { // needs to be checked for promotion
                    if (myPosition.getRow() == (direction == 1 ? 7 : 2)){
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), ChessPiece.PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), ChessPiece.PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), ChessPiece.PieceType.ROOK));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), ChessPiece.PieceType.KNIGHT));
                    } else {
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), null));
                    }
                }
            }
        }
        return validPawnMoves;
    }
}
