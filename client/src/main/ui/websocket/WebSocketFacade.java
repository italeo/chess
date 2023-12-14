package ui.websocket;

import chess.*;
import com.google.gson.*;
import model.Game;
import server.SessionManager;
import ui.ChessBoardDrawer;
import webSeverMessages.serverMessages.Error;
import webSeverMessages.serverMessages.LoadGame;
import webSeverMessages.serverMessages.Notification;
import webSeverMessages.userCommands.JoinObserver;
import webSeverMessages.userCommands.JoinPlayer;
import webSeverMessages.userCommands.Leave;
import webSeverMessages.userCommands.Resign;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    // Session variable used to establish game play
    Session session;

    public WebSocketFacade(String url) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // Set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    var deserializer = createGsonDeserializer();
                    ServerMessage serverMessage = deserializer.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR -> errorNotification(message);
                        case LOAD_GAME -> loadGameNotification(message);
                        case NOTIFICATION -> generalNotification(message);
                    }
                }
            });
        } catch (URISyntaxException | IOException | DeploymentException e) {
            e.printStackTrace();
        }
    }

    // ---------------- Methods to display the Notifications on to the screen --------------------------
    private void errorNotification(String message) {
        Error errorMsg = new Gson().fromJson(message, Error.class);
    }

    private void loadGameNotification(String message) {
        var deserializer = createGsonDeserializer();
        LoadGame loadGameMsg = deserializer.fromJson(message, LoadGame.class);
        Game game = loadGameMsg.getGame();

        // Save game to sessionManager
        SessionManager.setGame(game);
        SessionManager.setGameID(game.getGameID());

        // Draw the current state of the board
        ChessBoardDrawer chessBoardDrawer = new ChessBoardDrawer();
        System.out.println("\n");
        chessBoardDrawer.drawBoard(game);

        // Implement the logic to print the game/board to the screen
    }

    private void generalNotification(String message) {
        Gson gson = new Gson();
        Notification notification = gson.fromJson(message, Notification.class);
        String notificationMsg = notification.getMessage();
        System.out.print(notificationMsg);

    }

    // Required method for EndPoint
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    // ---------------------- Methods needed to connect to WebSocketHandler -----------------------------------

    public void joinObserver(JoinObserver joinObserver) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
    }

    public void joinPlayer(JoinPlayer joinPlayer) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
    }

    public void leaveGame(Leave leave) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(leave));
    }

    public void resignPlayer(Resign resign) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(resign));
    }


    // ------------------------------------------- END --------------------------------------------------------


    // ------------------------------- TYPE ADAPTER CLASSES -------------------------------------------------------
    public static Gson createGsonDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // This line should only be needed if your board class is using a Map to store chess pieces instead of a 2D array.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(ChessGame.class,
                (JsonDeserializer<ChessGame>) (el, type, ctx) -> ctx.deserialize(el, ChessGameImpl.class));

        gsonBuilder.registerTypeAdapter(ChessBoard.class,
                (JsonDeserializer<ChessBoard>) (el, type, ctx) -> ctx.deserialize(el, ChessBoardImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPiece.class,
                (JsonDeserializer<ChessPiece>) (el, type, ctx) -> ctx.deserialize(el, ChessPieceImpl.class));

        gsonBuilder.registerTypeAdapter(ChessMove.class,
                (JsonDeserializer<ChessMove>) (el, type, ctx) -> ctx.deserialize(el, ChessMoveImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPosition.class,
                (JsonDeserializer<ChessPosition>) (el, type, ctx) -> ctx.deserialize(el, ChessPositionImpl.class));

        gsonBuilder.registerTypeAdapter(ChessPieceImpl.class,
                (JsonDeserializer<ChessPieceImpl>) (el, type, ctx) -> {
                    ChessPieceImpl chessPiece = null;
                    if (el.isJsonObject()) {
                        String pieceType = el.getAsJsonObject().get("type").getAsString();
                        switch (pieceType) {
                            case "PAWN" -> chessPiece = ctx.deserialize(el, PawnPieceImpl.class);
                            case "ROOK" -> chessPiece = ctx.deserialize(el, RookPieceImpl.class);
                            case "KNIGHT" -> chessPiece = ctx.deserialize(el, KnightPieceImpl.class);
                            case "BISHOP" -> chessPiece = ctx.deserialize(el, BishopPieceImpl.class);
                            case "QUEEN" -> chessPiece = ctx.deserialize(el, QueenPieceImpl.class);
                            case "KING" -> chessPiece = ctx.deserialize(el, KingPieceImpl.class);
                        }
                    }
                    return chessPiece;
                });

        return gsonBuilder.create();
    }

    // ---------------------------------- END ----------------------------------------------------------------
}
