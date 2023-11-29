package handler;

import dao.UserDao;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

import static handler.GsonTool.gson;

public class CreateUserHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var userDto = gson.fromJson(request.getBody(), UserDto.class);
        var userDao = UserDao.getInstance();
        List<UserDto> userLookup = userDao.query(new Document("userName", userDto.getUserName()));

        RestApiAppResponse<UserDto> resp;
        String status;

        if (!userLookup.isEmpty()) {
            resp = new RestApiAppResponse<>(false, null, "Username already taken");
            status = StatusCodes.OK;
        } else {
            userDto.setPassword(DigestUtils.sha256Hex(userDto.getPassword()));
            userDao.put(userDto);
            resp = new RestApiAppResponse<>(true, null, "User created");
            status = StatusCodes.OK; // StatusCodes.BAD_REQUEST;
        }

        return new HttpResponseBuilder().setStatus(status).setBody(resp);
    }
}
