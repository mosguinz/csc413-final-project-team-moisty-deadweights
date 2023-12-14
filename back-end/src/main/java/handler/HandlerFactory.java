package handler;

import request.ParsedRequest;

public class HandlerFactory {
    // routes based on the path. Add your custom handlers here
    public static BaseHandler getHandler(ParsedRequest request) {
        return switch (request.getPath()) {
            case "/createUser" -> new CreateUserHandler();
            case "/idToUser" -> new IdToUserHandler();
            case "/createDeposit" -> new CreateDepositHandler();
            case "/getTransactions" -> new GetTransactionsHandler();
            case "/getRequests" -> new GetRequestsHandler();
            case "/friendRequest" -> new FriendRequestHandler();
            case "/friendsList" -> new FriendsListHandler();
            case "/login" -> new LoginHandler();
            case "/logout" -> new LogoutHandler();
            case "/withdraw" -> new WithdrawHandler();
            case "/requestFunds" -> new CreateRequestHandler();
            case "/resolveRequest" -> new ResolveRequestsHandler();
            case "/search" -> new SearchUserHandler();
            case "/transfer" -> new TransferHandler();
            case "/getUser" -> new GetUserHandler();
            default -> new FallbackHandler();
        };
    }

}
