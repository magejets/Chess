package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    if (myPosition.getRow() + i <= 8 && myPosition.getRow() + i >= 1 &&
                    myPosition.getColumn() + j <= 8 && myPosition.getColumn() + j >= 1) {
                        ChessPiece me = board.getPiece(myPosition);
                        ChessPiece them = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j));
                        if (them == null || them.getTeamColor() != me.getTeamColor()) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
