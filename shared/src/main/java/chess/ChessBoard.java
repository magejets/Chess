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

    public ChessBoard(ChessBoard newBoard) { // duplicator constructor
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = newBoard.board[i][j];
            }
        }

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

    // add method make move. Won't check for legality but will be used by ChessGame.makeMove; this'll take a ChessMove
    public ChessBoard makeMove(ChessMove move){ // add exceptions
        // check that this is in fact a legal move for this piece (disregarding king danger)
        if (this.getPiece(move.getStartPosition()) != null){
            if (this.getPiece(move.getStartPosition()).pieceMoves(
            this, move.getStartPosition()).contains(move)) {
                // put the piece in the end position
                this.board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1]
                    = this.board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1];
                // remove the piece from the start position
                this.board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = ' ';
            }
        }

        return this;
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

    @Override
    public String toString() {
        StringBuilder returnVal = new StringBuilder("Board:\n  | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |\n");
        for (int i = 7; i >= 0; i--) {
            returnVal.append(i + 1);
            for (int j = 0; j < 8; j++) {
                returnVal.append(" | ");
                returnVal.append(this.board[i][j]);
            }
            returnVal.append(" |\n");
        }
        return returnVal.toString();
    }
}
