package handler;

import dao.AuthDao;
import org.bson.Document;
import request.ParsedRequest;

public class AuthFilter {

  public static class AuthResult {
    public boolean isLoggedIn;
    public String userName;
    public String userId;
  }

  public static AuthResult doFilter(ParsedRequest parsedRequest){
    AuthDao authDao = AuthDao.getInstance();
    var result = new AuthResult();
    String hash = parsedRequest.getCookieValue("auth");
    System.out.println("hash: " + hash);
    if(hash == null){
      return result;
    }
    Document filter = new Document("hash", hash);
    var authRes = authDao.query(filter);
    if(authRes.size() == 0){
        result.isLoggedIn = false;
        return result;
    }
    result.isLoggedIn = true;
    result.userName = authRes.get(0).getUserName();
    result.userId = authRes.get(0).getUserId();
    return result;
  }
}
