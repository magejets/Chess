package chess;

import chess.moves.*;

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
                var kingCalculator = new KingMoveCalculator();
                validMoves.addAll(kingCalculator.pieceMoves(board, myPosition));
                break;
            case QUEEN:
                var queenCalculator = new QueenMoveCalculator();
                validMoves.addAll(queenCalculator.pieceMoves(board, myPosition));
                break;
            case BISHOP:
                var bishopCalculator = new BishopMoveCalculator();
                validMoves.addAll(bishopCalculator.pieceMoves(board, myPosition));
                break;
            case KNIGHT:
                var knightCalculator = new KnightMoveCalculator();
                validMoves.addAll(knightCalculator.pieceMoves(board, myPosition));
                break;
            case ROOK:
                var rookCalculator = new RookMoveCalculator();
                validMoves.addAll(rookCalculator.pieceMoves(board, myPosition));
                break;
            case PAWN:
                var pawnCalculator = new PawnMoveCalculator();
                validMoves.addAll(pawnCalculator.pieceMoves(board, myPosition));
                break;
        }

        return validMoves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
