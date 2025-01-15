package chess;

import java.lang.reflect.Array;
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

        switch (this.type) {
            case KING:
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
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
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1]), null));
                        } else if (board.getPiece(new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1])).getTeamColor() != this.getTeamColor()) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i[0], myPosition.getColumn() + i[1]), null));
                        }
                    }
                }
                break;
            case ROOK:
                validMoves.addAll(rookMoves(board, myPosition));
                break;
            case PAWN:
                validMoves.addAll(pawnMoves(board, myPosition));
                break;
        }

        return validMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
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
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), PieceType.QUEEN));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), PieceType.BISHOP));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), PieceType.ROOK));
                validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), PieceType.KNIGHT));
            }
        } else if (board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn())) == null) { // non initial, non promotion moves
            validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn()), null));
        }
        for (int j = -1; j <= 1; j += 2) { // scan to both the left and right for capture logic
            if ((myPosition.getColumn() + j <= 8) && (myPosition.getColumn() + j >= 1)) {
                ChessPiece cornerPiece = board.getPiece(new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j));
                if ((cornerPiece != null) && (cornerPiece.getTeamColor() != board.getPiece(myPosition).getTeamColor())) { // needs to be checked for promotion
                    if (myPosition.getRow() == (direction == 1 ? 7 : 2)){
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), PieceType.QUEEN));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), PieceType.BISHOP));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), PieceType.ROOK));
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), PieceType.KNIGHT));
                    } else {
                        validPawnMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn() + j), null));
                    }
                }
            }
        }
        return validPawnMoves;
    }
    // this code has been moved to a method so it could be reused with queen
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

    // this code has been moved to a method so it can be reused with queen
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
