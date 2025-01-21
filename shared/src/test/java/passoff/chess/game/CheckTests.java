package passoff.chess.game;

import chess.ChessGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.chess.TestUtilities;

public class CheckTests {
    @Test
    @DisplayName("IsCheck test")
    public void isCheckTest() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | |b| | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|r| | | |R| |K|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | |q|
                    """));

        assert game.isInCheck(ChessGame.TeamColor.WHITE);
    }

    @Test
    @DisplayName("IsCheckmate no way out test")
    public void isCheckmateTrueTest() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | |b| | |r| |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|r| | | |R| |K|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | |q|
                    """));

        assert game.isInCheckmate(ChessGame.TeamColor.WHITE);
    }

    @Test
    @DisplayName("IsCheckmate not in check test")
    public void isCheckmateFalseTest() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | |b| | |r| |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|r| | | |R| |K|
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | | |
                    """));

        assert !(game.isInCheckmate(ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("IsCheckmate can be saved test")
    public void isCheckmateSavableTest() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | |b| | |r| |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|p| | | | | |K|
                    | | | | | |R| | |
                    | | | | | | | | |
                    | | | | | | | | |
                    | | | | | | | |q|
                    """));

        assert !(game.isInCheckmate(ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("IsStalemate test")
    public void isStalemate() {
        var game = new ChessGame();
        game.setBoard(TestUtilities.loadBoard("""
                    | | | |b| | |r| |
                    | | | | | | | | |
                    | | | | | | | | |
                    |k|p| | | | | | |
                    | | | | | | | | |
                    |q| | | | | | | |
                    | | | | | | | | |
                    | | | | | | | |K|
                    """));

        assert !game.isInStalemate(ChessGame.TeamColor.WHITE);
    }
}
