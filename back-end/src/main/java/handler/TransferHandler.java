package handler;

import dao.TransactionDao;
import dao.UserDao;
import dto.TransactionDto;
import dto.TransactionType;
import dto.UserDto;
import org.bson.Document;
import org.bson.types.ObjectId;

import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.ArrayList;
import java.util.List;

import static handler.GsonTool.gson;

public class TransferHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {

        var authLookup = AuthFilter.doFilter(request);
        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        var userDao = UserDao.getInstance();
        var senderQuery = userDao.query(new Document("_id", new ObjectId( authLookup.userId)));
        System.out.println("PRINTING IMPORTANT INFO");
        System.out.println(authLookup.userId);
        System.out.println(senderQuery.size());
        if (senderQuery.size() != 1) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "sender not found");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }

        var txDto = gson.fromJson(request.getBody(), TransactionDto.class);
        var recipientQuery = userDao.query(new Document("userName", txDto.getToId()));
        if (recipientQuery.size() != 1) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "recipient not found");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }

        // TODO: Why the fuck is the sender DTO not correct??
        UserDto sender = senderQuery.get(0), recipient = recipientQuery.get(0);
        if (sender.getBalance() < txDto.getAmount()) {
            RestApiAppResponse res = new RestApiAppResponse<>(false, new ArrayList<>(), "Not enough balance");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(res);
        }

        sender.setBalance(sender.getBalance() - txDto.getAmount());
        recipient.setBalance(recipient.getBalance() + txDto.getAmount());
        userDao.put(sender);
        userDao.put(recipient);

        var txDao = TransactionDao.getInstance();
        txDto.setUserId(authLookup.userId);
        txDto.setToId(recipient.getObjectId().get("_id").toString());
        txDto.setTransactionType(TransactionType.Transfer);
        txDto.setAmount(txDto.getAmount());
        txDao.put(txDto);

        var resp = new RestApiAppResponse<>(true, List.of(sender, recipient), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }

}
