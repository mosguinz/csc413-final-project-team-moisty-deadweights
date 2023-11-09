package handler;

import dao.AuthDao;
import dao.UserDao;
import dto.AuthDto;
import java.time.Instant;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import request.ParsedRequest;
import response.CustomHttpResponse;
import response.HttpResponseBuilder;

class LoginDto{
  String userName;
  String password;
}

public class LoginHandler implements BaseHandler{

  @Override
  public HttpResponseBuilder handleRequest(ParsedRequest request) {
    return null; // todo
  }
}
