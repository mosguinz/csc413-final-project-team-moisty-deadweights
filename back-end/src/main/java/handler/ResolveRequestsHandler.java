package handler;

import dao.TransactionDao;
import dao.UserDao;
import dao.TransferRequestDao;
import dto.TransactionDto;
import dto.RequestStatus;
import dto.TransactionType;
import dto.TransferRequestDto;
import dto.UserDto;
import org.bson.Document;
import org.bson.types.ObjectId;

import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

import static handler.GsonTool.gson;

public class ResolveRequestsHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        System.out.println("GOT TO HANDLER");
        var requestDto = gson.fromJson(request.getBody(), TransferRequestDto.class);
        System.out.println(request.getBody());
        var requestDao = TransferRequestDao.getInstance();
        var amount = requestDto.getAmount();

        var authLookup = AuthFilter.doFilter(request);
        // dealing with not being logged in
        if (!authLookup.isLoggedIn) {
            var resp = new RestApiAppResponse<>(true, null, "Not Logged In");
            System.out.println(resp.message);
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED).setBody(resp);
        }

        System.out.println("IF 1");
        var userDao = UserDao.getInstance();
        // dealing with finding the sender of the request (also receives money)
        var senderQuery = userDao.query(new Document("_id", new ObjectId(requestDto.fromId)));
        if (senderQuery.size() != 1) {
            var resp = new RestApiAppResponse<>(true, null, "Could not find sender of the request");
            System.out.println(resp.message);
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
        }

        System.out.println("IF 2");
        // dealing with finding the receiver of the request (also sends money)
        var recipientQuery = userDao.query(new Document("_id", new ObjectId(requestDto.toId)));
        if (recipientQuery.size() != 1) {
            var resp = new RestApiAppResponse<>(true, null, "Could not find the receiver of the request");
            System.out.println(resp.message);
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
        }

        System.out.println("IF 3");
        // flipping receiver and sender so the receiver here is the one receiving money
        // and the sender is the one sending money
        UserDto moneyReceiver = senderQuery.get(0), moneySender = recipientQuery.get(0);

        System.out.println("FLIP");
        if( requestDto.getStatus() == RequestStatus.Rejected ) {
            // adding the updated request into the dao.
        System.out.println("put attempted");
            requestDao.put(requestDto);
        System.out.println("put sucess");
            // return no data, and just a message
            var resp = new RestApiAppResponse<>(true, null, "Successfully rejected transfer");
            System.out.println(resp.message);
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        }
        System.out.println("IF 4");
        if( requestDto.getStatus() == RequestStatus.Accepted ) {
            // dealing with low balance
            if (moneySender.getBalance() < amount) {
                var resp = new RestApiAppResponse<>(true, null, "Not enough funds available");
                System.out.println(resp.message);
                return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
            }
        System.out.println("inner IF");
            moneySender.setBalance(moneySender.getBalance() - amount);
            moneyReceiver.setBalance(moneyReceiver.getBalance() + amount);
            // updating all balances
            userDao.put(moneyReceiver);
            userDao.put(moneySender);
            TransactionDto txDto = new TransactionDto();
            TransactionDao txDao = TransactionDao.getInstance();
            txDto.setUserId(moneySender.getObjectId().get("_id").toString());
            txDto.setToId(moneyReceiver.getObjectId().get("_id").toString());
            txDto.setTransactionType(TransactionType.Transfer);
            txDto.setAmount(amount);
            txDao.put(txDto);
            // adding in the updated request which marks it as accepted
            requestDao.put(requestDto);
            var resp = new RestApiAppResponse<>(true, null, "Successfully Accepted transfer");
            System.out.println(resp.message);
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        }

        var resp = new RestApiAppResponse<>(true, List.of(moneyReceiver, moneySender), "Status was set to Sent");
        System.out.println(resp.message);
        return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
    }

}
