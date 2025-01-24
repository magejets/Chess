package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class SlideMoveCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        for (int[] dir: directions) {
            boolean blocked = false;
            int i = 0;
            int j = 0;
            while(!blocked) {
                i += dir[0];
                j += dir[1];
                if (myPosition.getRow() + i <= 8 && myPosition.getRow() + i >= 1 &&
                        myPosition.getColumn() + j <= 8 && myPosition.getColumn() + j >= 1) {
                    ChessPiece me = board.getPiece(myPosition);
                    ChessPiece them = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j));
                    if (them == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    } else if (them.getTeamColor() != me.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                        blocked = true;
                    } else {
                        blocked = true;
                    }
                } else {
                    blocked = true;
                }
            }
        }
        return moves;
    }
}
