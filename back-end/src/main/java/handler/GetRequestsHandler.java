package handler;

import dao.TransferRequestDao;
import dto.RequestStatus;

import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

public class GetRequestsHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var transferRequestDao = TransferRequestDao.getInstance();
        var authLookup = AuthFilter.doFilter(request);

        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        // all tx involving this user
        Document filter = new Document("$and", List.of(
                new Document("toId", authLookup.userId),
                new Document("status", "Sent")
        ));

        try {
            var resp = new RestApiAppResponse<>(true, transferRequestDao.query(filter), null);
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(StatusCodes.SERVER_ERROR);

        }
    }

}
