package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int direction = board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        if (myPosition.getRow() == (direction == 1 ? 2 : 7)) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null &&
            board.getPiece(new ChessPosition(myPosition.getRow() + (direction * 2), myPosition.getColumn())) == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + (direction * 2), myPosition.getColumn()), null));
            }
            if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), null));

            }
        }
        if (myPosition.getRow() < 7 && myPosition.getRow() > 2) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), null));
            }
        } else if (myPosition.getRow() == (direction == 1 ? 7 : 2)) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                    myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                    myPosition.getColumn()), ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                    myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                    myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
        }
        // capture
        for (int j = -1; j <= 1; j += 2) {
            if (myPosition.getColumn() + j <= 8 && myPosition.getColumn() + j >= 1) {
                ChessPiece me = board.getPiece(myPosition);
                ChessPiece them = board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j));
                if (them != null && me.getTeamColor() != them.getTeamColor()) {
                    if (myPosition.getRow() != (direction == 1 ? 7 : 2)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), null));
                    } else if (myPosition.getRow() == (direction == 1 ? 7 : 2)) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                                myPosition.getColumn() + j), ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                                myPosition.getColumn() + j), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                                myPosition.getColumn() + j), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction,
                                myPosition.getColumn() + j), ChessPiece.PieceType.KNIGHT));
                    }
                }
            }
        }
        // capture promote
        return moves;
    }
}
