package handler;

import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;

import static handler.GsonTool.gson;

public class WithdrawHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {

        var authLookup = AuthFilter.doFilter(request);
        if (!authLookup.isLoggedIn) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "not logged in");
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED).setBody(res);
        }

        var userDao = UserDao.getInstance();
        var userQuery = userDao.query(new Document("userName", authLookup.userName));
        if (userQuery.size() != 1) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "username not found");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }

        var txDto = gson.fromJson(request.getBody(), TransactionDto.class);
        UserDto userDto = userQuery.get(0);
        if (userDto.getBalance() < txDto.getAmount() || txDto.getAmount() < 0.001) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "not enough funds");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }

        var txDao = TransactionDao.getInstance();
        userDto.setBalance(userDto.getBalance() - txDto.getAmount());
        userDao.put(userDto);
        txDto.setTransactionType(TransactionType.Withdraw);
        txDto.setUserId( authLookup.userId );
        txDao.put(txDto);

        var resp = new RestApiAppResponse<>(true, List.of(txDto), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }
}
