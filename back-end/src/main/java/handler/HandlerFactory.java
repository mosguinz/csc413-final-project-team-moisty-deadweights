package handler;

import request.ParsedRequest;

public class HandlerFactory {
    // routes based on the path. Add your custom handlers here
    public static BaseHandler getHandler(ParsedRequest request) {
        return switch (request.getPath()) {
            case "/createUser" -> new CreateUserHandler();
            case "/createDeposit" -> new CreateDepositHandler();
            case "/getTransactions" -> new GetTransactionsHandler();
            case "/getRequests" -> new GetRequestsHandler();
            case "/login" -> new LoginHandler();
            case "/withdraw" -> new WithdrawHandler();
            case "/request" -> new CreateRequestHandler();
            case "/resolveRequest" -> new ResolveRequestsHandler();
            case "/search" -> new SearchUserHandler();
            default -> new FallbackHandler();
        };
    }

}
