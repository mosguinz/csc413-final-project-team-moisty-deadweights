package handler;

import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import org.bson.Document;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

import static handler.GsonTool.gson;

public class CreateDepositHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        var txDto = gson.fromJson(request.getBody(), TransactionDto.class);
        var txDao = TransactionDao.getInstance();
        var authRes = AuthFilter.doFilter(request);

        if (!authRes.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        var userDao = UserDao.getInstance();
        var userLookup = userDao.query(new Document("userName", authRes.userName));
        if (userLookup.size() != 1) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        var userDto = userLookup.get(0);
        txDto.setTransactionType(TransactionType.Deposit);
        txDto.setUserId(authRes.userName);
        txDao.put(txDto);

        userDto.setBalance(userDto.getBalance() + txDto.getAmount());
        userDao.put(userDto);

        var resp = new RestApiAppResponse<>(true, List.of(txDto), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }

}
