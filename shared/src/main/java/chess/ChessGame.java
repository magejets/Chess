package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {
        turn = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //throw new RuntimeException("Not implemented");
        Collection<ChessMove> validMoves = board.getPiece(startPosition).pieceMoves(this.getBoard(), startPosition);

        // logic for check, checkmate, and stalemate
        // check for check
            // if the king is in check
                // if this piece is the king
                    // ? implement method that takes a board or a move to determine check?
                    // the only valid moves are ones that get it out of check
                // if this piece is not the king
                    // there are no valid moves
        // check for checkmate
            // no valid moves. Game over. I think. Maybe this and stalemate aren't necessary
        // check for stalemate
            // game over

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        if (this.validMoves(move.getStartPosition()).contains(move)) {
            this.setBoard(this.getBoard().makeMove(move));
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
        // at the end switch the turn
        this.setTeamTurn(this.getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;

        // find this teams king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = this.getBoard().getPiece(new ChessPosition(i, j));
                if (piece != null &&
                    piece.getTeamColor() == teamColor &&
                    piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(i, j);
                    break; // so this doesn't work to quit the for loop. Set i and j manually? use while?
                }
            }
        }

        // make sure there was actually a king
        if (kingPosition == null) {
            return false;
        }
        /*
  | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
8 |   |   |   |   |   |   |   |   |
7 |   |   |   |   |   |   |   |   |
6 |   |   |   |   |   |   |   |   |
5 | k |   |   |   |   | R |   | K |
4 |   |   |   |   |   |   |   |   |
3 |   |   |   |   |   |   |   |   |
2 |   |   |   |   |   |   |   |   |
1 |   |   |   |   |   |   |   |   |*/

        // see if any of the pieces' moves end on the king square
        for (int i = 1; i <= 8; i++) { // initialize to 1!
            for (int j = 1; j <= 8; j++) { // initialize to 1!
                ChessPosition enemyPosition = new ChessPosition(i, j);
                ChessPiece enemy = this.getBoard().getPiece(enemyPosition);
                if (enemy != null) {
                    Collection<ChessMove> strikes = enemy.pieceMoves(this.getBoard(), enemyPosition); // debug
                    for (ChessMove strike : enemy.pieceMoves(this.getBoard(), enemyPosition)) {
                        if (strike.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
