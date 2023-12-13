package ui.websocket;

import chess.*;
import com.google.gson.*;
import model.Game;
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
import java.lang.reflect.Type;
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
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
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
        LoadGame loadGameMsg = new Gson().fromJson(message, LoadGame.class);
        Game game = loadGameMsg.getGame();
        System.out.println("The game from LoadGame: " + game);

        // Implement the logic to print the game/board to the screen
    }

    private void generalNotification(String message) {
        var deserializer = createGsonDeserializer();
        Notification notification = deserializer.fromJson(message, Notification.class);
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

    // Type adapter for ChessGame need to change to JsonSerializer  n
//    public static class GameAdapter implements JsonDeserializer<ChessGame> {
//        @Override
//        public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
//            return new Gson().fromJson(el, ChessGameImpl.class);
//        }
//    }
//
//    // Type adapter for ChessBoard
//    public static class BoardAdapter implements JsonDeserializer<ChessBoard> {
//        @Override
//        public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
//            var builder = new GsonBuilder();
//            builder.registerTypeAdapter(ChessPiece.class, new PieceAdapter());
//
//            return builder.create().fromJson(el, ChessBoardImpl.class);
//        }
//    }
//
//
//    public static class PieceAdapter implements JsonDeserializer<ChessPiece> {
//
//
//        @Override
//        public ChessPiece deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//            String pieceType = jsonElement.getAsJsonObject().get("type").getAsString();
//            Gson gson = new Gson();
//            return switch (pieceType) {
//                case "KING" -> gson.fromJson(jsonElement, KingPieceImpl.class);
//                case "QUEEN" -> gson.fromJson(jsonElement, QueenPieceImpl.class);
//                case "KNIGHT" -> gson.fromJson(jsonElement, KnightPieceImpl.class);
//                case "BISHOP" -> gson.fromJson(jsonElement, BishopPieceImpl.class);
//                case "ROOK" -> gson.fromJson(jsonElement, RookPieceImpl.class);
//                case "PAWN" -> gson.fromJson(jsonElement, PawnPieceImpl.class);
//                default -> throw new JsonParseException("Unknown chess piece type" + pieceType);
//            };
//        }
//    }
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
