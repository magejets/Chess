import chess.*;
import dataaccess.authdao.SQLAuthDao;
import dataaccess.gamedao.SQLGameDao;
import dataaccess.userdao.SQLUserDao;
import dataaccess.userdao.UserDao;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server testPage = new Server(new SQLUserDao(), new SQLAuthDao(), new SQLGameDao());
        int port = testPage.run(8080);
    }
}