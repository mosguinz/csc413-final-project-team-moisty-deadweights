package handler;

import dao.TransactionDao;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class GetTransactionsHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var txDao = TransactionDao.getInstance();
        var authLookup = AuthFilter.doFilter(request);

        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        // all tx involving this user
        Document filter = new Document("$or", List.of(
                new Document("userId", authLookup.userName),
                new Document("toId", authLookup.userName)
        ));
        
        try {
            var resp = new RestApiAppResponse<>(true, txDao.query(filter), null);
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(StatusCodes.SERVER_ERROR);
        }
    }

}
