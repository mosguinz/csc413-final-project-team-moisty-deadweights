package handler;

import dao.UserDao;
import dto.UserDto;

import java.util.List;

import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;


public class GetUserHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var usrDao = UserDao.getInstance();
        var authLookup = AuthFilter.doFilter(request);

        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        // getting the current Usr
        Document filter = new Document("userName", authLookup.userName);

        try {
            UserDto usrDto = usrDao.query(filter).get(0);
            usrDto.loadUniqueId(usrDto.getObjectId());
            var resp = new RestApiAppResponse<>(true, List.of(usrDto), null);
            System.out.println("printing out response for get user");
            System.out.println(resp.toString());
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(StatusCodes.SERVER_ERROR);
        }
    }

}
