package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board = new ChessBoard();
    private Winner winner;

    public ChessGame() {
        turn = TeamColor.WHITE;
        winner = Winner.NONE_YET;
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

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public enum Winner {
        WHITE,
        BLACK,
        STALEMATE,
        NONE_YET
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = this.getBoard().getPiece(startPosition).pieceMoves(this.getBoard(), startPosition);
        // if no piece, no moves. Also, if stale or checkmate no moves
        if ((this.getBoard().getPiece(startPosition) == null) ||
            this.isInStalemate(this.getBoard().getPiece(startPosition).getTeamColor()) ||
            this.isInCheckmate(this.getBoard().getPiece(startPosition).getTeamColor())) {
            return null;
        }
        // remove all the illegal moves given us by the pieceMove function
        validMoves.removeIf(move -> {
            try {
                return isInCheck(move, this.getBoard().getPiece(startPosition).getTeamColor());
            } catch (InvalidMoveException e) {
                return false;
            }
        });

        validMoves.addAll(castling(startPosition));

        return validMoves;
    }

    /**
     * Looks at valid castling possibilities
     *
     * @param startPosition the piece to see if is king and can castle
     * @return Set of valid castling moves for requested piece, if that
     * piece is a king
     */
    public Collection<ChessMove> castling(ChessPosition startPosition) {
        try {
            ArrayList<ChessMove> castles = new ArrayList<ChessMove>();
            // castling functionality
            ChessPiece me = this.getBoard().getPiece(startPosition);
            if (me.getPieceType() == ChessPiece.PieceType.KING) {
                // king side
                if (startPosition.getColumn() == 5) { // to avoid indexing errors
                    if (!this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "K" : "k") &&
                            !this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "R-r" : "r-r") &&
                            (!isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()), null),
                                    me.getTeamColor()) &&
                                    !isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1), null),
                                            me.getTeamColor()) &&
                                    !isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 2), null),
                                            me.getTeamColor()))) {
                        if (this.getBoard().getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1)) == null &&
                                this.getBoard().getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 2)) == null) {
                            castles.add(new ChessMove(startPosition,
                                    new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 2), null));
                        }
                    }
                }

                // queen side
                if (startPosition.getColumn() == 5) { // to avoid indexing errors
                    if (!this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "K" : "k") &&
                            !this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "R-l" : "r-l") &&
                            (!isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()), null),
                                    me.getTeamColor()) &&
                                    !isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1), null),
                                            me.getTeamColor()) &&
                                    !isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 2), null),
                                            me.getTeamColor()) &&
                                    !isInCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 3), null),
                                            me.getTeamColor()))) {
                        if (this.getBoard().getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1)) == null &&
                                this.getBoard().getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 2)) == null &&
                                this.getBoard().getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 3)) == null) {
                            castles.add(new ChessMove(startPosition,
                                    new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 2), null));
                        }
                    }
                }
            }

            return castles;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece me = this.getBoard().getPiece(move.getStartPosition());
        if (me != null &&
            (this.getTeamTurn() == me.getTeamColor()) &&
            this.validMoves(move.getStartPosition()).contains(move)) {
            this.setBoard(this.getBoard().makeMove(move));

            // for the castling logic
            if (me.getPieceType() == ChessPiece.PieceType.KING && !this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "K" : "k")) {
                this.board.piecesMoved.put((me.getTeamColor() == TeamColor.WHITE ? "K" : "k"), true);
            }
            if (me.getPieceType() == ChessPiece.PieceType.ROOK) {
                if (move.getStartPosition().getColumn() == 1 && !this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "R-l" : "r-l")) {
                    this.board.piecesMoved.put((me.getTeamColor() == TeamColor.WHITE ? "R-l" : "r-l"), true);
                } else if (move.getStartPosition().getColumn() == 8 &&
                        !this.board.piecesMoved.get(me.getTeamColor() == TeamColor.WHITE ? "R-r" : "r-r")) {
                    this.board.piecesMoved.put((me.getTeamColor() == TeamColor.WHITE ? "R-r" : "r-r"), true);
                }
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
        // at the end switch the turn
        this.setTeamTurn(this.getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param move a hypothetical move to see if check is maintained
     * @param teamColor which team to check for check
     * @return True if the specified move places king in check
     */
    public boolean isInCheck(ChessMove move, TeamColor teamColor) throws InvalidMoveException {
        ChessBoard tempBoard = new ChessBoard(this.board); // store the board as it is
        ChessBoard hypBoard = this.getBoard().makeMove(move); // modify the board
        boolean check = this.isInCheck(hypBoard, teamColor); // see if the king is still in check
        this.setBoard(tempBoard); // fix the board
        return check; // return that hypothetical
    }

    /**
     * Determines if the given team is in check
     *
     * @param board a hypothetical board to see if check is maintained
     * @param teamColor which team to check for check
     * @return True if the specified move places king in check
     */
    public boolean isInCheck(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = null;

        // find this teams king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
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

        // see if any of the pieces' moves end on the king square
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition enemyPosition = new ChessPosition(i, j);
                ChessPiece enemy = board.getPiece(enemyPosition);
                if (canSlayKing(enemy, enemyPosition, kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given piece can kill the king
     *
     * @param enemy the piece to check
     * @param enemyPosition the position of the piece to check
     * @param kingPosition where the king is at
     * @return True if one of the piece's moves can strike the king
     */
    private boolean canSlayKing(ChessPiece enemy, ChessPosition enemyPosition, ChessPosition kingPosition) {
        if (enemy != null) {
            Collection<ChessMove> strikes = enemy.pieceMoves(board, enemyPosition);
            for (ChessMove strike : enemy.pieceMoves(board, enemyPosition)) {
                if (strike.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return this.isInCheck(this.getBoard(), teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check
     * @return True if the specified team's king can safely move
     */
    private boolean noEscape(TeamColor teamColor) {
        // go through all the pieces on the board
        for (int i = 1; i <= 8; i++) { // default to 1
            for (int j = 1; j <= 8; j++) { // default to 1
                // check to see if each teammate on the board could save the king
                ChessPosition piecePos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(piecePos);

                if (canSaveKing(piece, piecePos, teamColor)) {
                    return false;
                }

            }
        }
        // if nothing can be done, alas, checkmate (or stalemate if not in check)
        return true;
    }

    /**
     * Determines if the given piece can get the king out of check
     *
     * @param piece the piece to check
     * @param piecePos the position of the piece to check
     * @param teamColor which team to check
     * @return True if one of the piece's moves can save the king
     */
    private boolean canSaveKing(ChessPiece piece, ChessPosition piecePos, TeamColor teamColor) {
        // see if one of his teammates can save the king
        try {
            if (piece != null && piece.getTeamColor() == teamColor) {
                for (ChessMove move : piece.pieceMoves(this.getBoard(), piecePos)) {
                    if (!(this.isInCheck(move, teamColor))) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // doesn't do anything because the loop is going to loop through the moves we already know are valid
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
        // if the king is in check and can't move then checkmate
        return this.isInCheck(teamColor) && this.noEscape(teamColor);

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // if the king isn't in check and can't move then stalemate
        return !(this.isInCheck(teamColor)) && this.noEscape(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public String toString() {
        return "{\n\t\"turn\" : " + turn + "\n\t\"board\" : " + board + "\n}";
    }
}
