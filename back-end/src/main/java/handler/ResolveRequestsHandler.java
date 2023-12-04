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
import request.ParsedRequest;
import response.HttpResponseBuilder;
import response.RestApiAppResponse;

import java.util.List;

import static handler.GsonTool.gson;

public class ResolveRequestsHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {

        var requestDto = gson.fromJson(request.getBody(), TransferRequestDto.class);
        var requestDao = TransferRequestDao.getInstance();
        var amount = requestDto.getAmount();

        var authLookup = AuthFilter.doFilter(request);
        // dealing with not being logged in
        if (!authLookup.isLoggedIn) {
            var resp = new RestApiAppResponse<>(true, null, "Not Logged In");
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED).setBody(resp);
        }

        var userDao = UserDao.getInstance();
        // dealing with finding the sender of the request (also receives money)
        var senderQuery = userDao.query(new Document("userName", authLookup.userName));
        if (senderQuery.size() != 1) {
            var resp = new RestApiAppResponse<>(true, null, "Could not find sender of the request");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
        }

        var recipientQuery = userDao.query(new Document("userName", requestDto.getToUserName()));
        // dealing with finding the receiver of the request (also sends money)
        if (recipientQuery.size() != 1) {
            var resp = new RestApiAppResponse<>(true, null, "Could not find the receiver of the request");
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
        }

        // flipping receiver and sender so the receiver here is the one receiving money
        // and the sender is the one sending money
        UserDto recipient = senderQuery.get(0), sender = recipientQuery.get(0);

        if( requestDto.getStatus() == RequestStatus.Rejected ) {
            // adding the updated request into the dao.
            requestDao.put(requestDto);
            // return no data, and just a message
            var resp = new RestApiAppResponse<>(true, null, "Successfully rejected transfer");
            return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
        }
        if( requestDto.getStatus() == RequestStatus.Accepted ) {
            // dealing with low balance
            if (sender.getBalance() < amount) {
                var resp = new RestApiAppResponse<>(true, null, "Not enough funds available");
                return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
            }
            sender.setBalance(sender.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);
            // updating all balances
            userDao.put(sender);
            userDao.put(recipient);
            // adding in the updated request which marks it as accepted
            requestDao.put(requestDto);
        }

        var resp = new RestApiAppResponse<>(true, List.of(sender, recipient), "Status was set to Sent");
        return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST).setBody(resp);
    }

}
