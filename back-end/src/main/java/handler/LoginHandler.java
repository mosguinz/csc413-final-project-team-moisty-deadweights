package handler;

import dao.AuthDao;
import dao.UserDao;
import dto.AuthDto;
import dto.UserDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;

import java.time.Instant;

import static handler.GsonTool.gson;

class LoginDto {
    String userName;
    String password;
}

public class LoginHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var userDto = gson.fromJson(request.getBody(), UserDto.class);
        var userDao = UserDao.getInstance();
        var userLookup = userDao.query(
                new Document()
                        .append("userName", userDto.getUserName())
                        .append("password", DigestUtils.sha256Hex(userDto.getPassword()))
        );

        if (userLookup.isEmpty()) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        userDto = userLookup.get(0);

        var authDto = new AuthDto();
        var authDao = AuthDao.getInstance();
        var expireTime = Instant.now().getEpochSecond() + 6000; // 10 minutes
        var hash = DigestUtils.sha256Hex(authDto.getUniqueId() + expireTime);
        authDto.setExpireTime(expireTime);
        authDto.setHash(hash);
        authDto.setUserName(userDto.getUserName());
        // authDto.setUserId(userDto.getUniqueId());
        System.out.println("started printint out document for auth");
        System.out.println(authDto.toDocument());
        System.out.println("finished printint out document for auth");
        authDao.put(authDto);

        return new HttpResponseBuilder().setStatus(StatusCodes.OK)
                .setHeader("Set-Cookie", "auth=" + hash);
    }
}
