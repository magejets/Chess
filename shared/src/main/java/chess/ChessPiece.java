package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        boolean blockage = false;
        int i = 0;
        int j = 0;
        switch (this.type) {
            case KING: // now add logic for the edge of the board
                for (i = -1; i <= 1; i++) {
                    for (j = -1; j <= 1; j++) {
                        if (!(i == 0 && j == 0) && (myPosition.getRow() + i <= 8 && myPosition.getRow() + i >= 1) && (myPosition.getColumn() + j <= 8 && myPosition.getColumn() + j >= 1)) {
                            if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) == null) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                            } else if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() != this.getTeamColor()){
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                validMoves.addAll(bishopMoves(board, myPosition));
                validMoves.addAll(rookMoves(board, myPosition));
                break;
            case BISHOP:
                validMoves.addAll(bishopMoves(board, myPosition));
                break;
            case KNIGHT:
                break;
            case ROOK:
                validMoves.addAll(rookMoves(board, myPosition));
                break;
            case PAWN:
                break;
        }

        return validMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validBishopMoves = new ArrayList<ChessMove>();

        boolean blockage = false;
        int i = 0;
        int j = 0;

        while (!blockage) { // up and right
            i++;
            j++;
            boolean capture = true;
            if ((myPosition.getRow() + i <= 8) && (myPosition.getColumn() + j <= 8)) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
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
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
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
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
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
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
                        capture = false;
                    }
                }
                if (capture) {
                    validBishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j), null));
                }
            } else {
                blockage = true;
            }
        }

        return validBishopMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validRookMoves = new ArrayList<ChessMove>();

        boolean blockage = false;
        int i = 0;
        int j = 0;

        while (!blockage) { // up
            i++;
            boolean capture = true;
            if (myPosition.getRow() + i <= 8) {
                if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())) != null) {
                    blockage = true;
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor() == this.getTeamColor()) {
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
                    if (board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn())).getTeamColor() == this.getTeamColor()) {
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
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
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
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + j)).getTeamColor() == this.getTeamColor()) {
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
