package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator {
    public KingMoveCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validKingMoves = new ArrayList<ChessMove>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && (myPosition.getRow() + i <= 8 && myPosition.getRow() + i >= 1) && (myPosition.getColumn() + j <= 8 && myPosition.getColumn() + j >= 1)) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) == null) {
                        validKingMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    } else if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        validKingMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                    }
                }
            }
        }

        return validKingMoves;
    }
}
