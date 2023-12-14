package handler;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.Filters;

import dao.UserDao;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

public class IdToUserHandler implements BaseHandler {
    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        String searchTerm = request.getHeaderValue("q");
        var userDao = UserDao.getInstance();

        Document filter = new Document("_id", searchTerm);

        try {
            var resp = new RestApiAppResponse<>(true, userDao.query(filter), null);
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(StatusCodes.SERVER_ERROR);
        }
    }
}
