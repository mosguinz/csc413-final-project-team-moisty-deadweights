package handler;

import dao.AuthDao;
import dao.UserDao;
import dto.AuthDto;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;
import static handler.GsonTool.gson;

public class LogoutHandler implements BaseHandler{
    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        AuthDao authDao = AuthDao.getInstance();
        String hash = request.getCookieValue("auth");
        var authR = AuthFilter.doFilter(request);
        System.out.println("jhdfb:"+authR.userName);

//        var userName = gson.fromJson(request.getBody(), UserDto.class);

        
        System.out.println("HASH: " + hash);
        System.out.println("request: " + request.getBody());

        Document filter = new Document("hash", hash);
        var authRes = authDao.query(filter);
        System.out.println("Size: "+authRes.size());
        AuthDto authDto =  authRes.get(0);
        authDao.remove(authDto);
        var resp = new RestApiAppResponse<>(true, null, "Log out successful");

        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }
}
