package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    // some sort of 2d array to store the pieces in their respective rows and columns
    private char[][] board = new char[][] {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
    };

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'R';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'r';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'N';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'n';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'B';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'b';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'Q';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'q';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.KING) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'K';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.KING) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'k';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'P';
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            this.board[ position.getRow() - 1][ position.getColumn() - 1] = 'p';
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return switch (this.board[position.getRow() - 1][position.getColumn() - 1]) {
            case ' ' -> null;
            case 'r' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            case 'R' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            case 'n' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            case 'N' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            case 'b' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            case 'B' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            case 'q' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
            case 'Q' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
            case 'k' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
            case 'K' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
            case 'p' -> new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            case 'P' -> new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            default -> null;
        };

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new char[][] {
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}

        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;

        return Objects.deepEquals(this.board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
