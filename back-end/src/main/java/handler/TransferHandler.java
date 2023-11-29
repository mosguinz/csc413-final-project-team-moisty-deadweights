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

import java.util.List;

import static handler.GsonTool.gson;

public class TransferHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        System.out.println(request.getBody());

        var authLookup = AuthFilter.doFilter(request);
        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        var userDao = UserDao.getInstance();
        var senderQuery = userDao.query(new Document("userName", authLookup.userName));
        if (senderQuery.size() != 1) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        var txDto = gson.fromJson(request.getBody(), TransactionDto.class);
        var recipientQuery = userDao.query(new Document("userName", txDto.getToId()));
        if (recipientQuery.size() != 1) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        // TODO: Why the fuck is the sender DTO not correct??
        UserDto sender = senderQuery.get(0), recipient = recipientQuery.get(0);
        if (sender.getBalance() < txDto.getAmount()) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        sender.setBalance(sender.getBalance() - txDto.getAmount());
        recipient.setBalance(recipient.getBalance() + txDto.getAmount());
        userDao.put(sender);
        userDao.put(recipient);

        var txDao = TransactionDao.getInstance();
        txDto.setUserId(sender.getUserName());
        txDto.setTransactionType(TransactionType.Transfer);
        txDto.setAmount(txDto.getAmount());
        txDao.put(txDto);

        var resp = new RestApiAppResponse<>(true, List.of(sender, recipient), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }

}