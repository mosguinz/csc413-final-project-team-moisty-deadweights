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

public class CreateRequestHandler implements BaseHandler {

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {

        var requestDto = gson.fromJson(request.getBody(), TransferRequestDto.class);

        var authLookup = AuthFilter.doFilter(request);
        if (!authLookup.isLoggedIn) {
            return new HttpResponseBuilder().setStatus(StatusCodes.UNAUTHORIZED);
        }

        var userDao = UserDao.getInstance();
        var senderQuery = userDao.query(new Document("userName", authLookup.userName));
        if (senderQuery.size() != 1) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        var recipientQuery = userDao.query(new Document("userName", requestDto.getToUserName()));
        if (recipientQuery.size() != 1) {
            return new HttpResponseBuilder().setStatus(StatusCodes.BAD_REQUEST);
        }

        UserDto sender = senderQuery.get(0), recipient = recipientQuery.get(0);

        requestDto.setFromId( sender.getUniqueId() );
        requestDto.setFromUserName( sender.getUserName() );
        requestDto.setToId( recipient.getUniqueId() );

        requestDto.setStatus( RequestStatus.Sent );
        var requestDao = TransferRequestDao.getInstance();
        requestDao.put(requestDto);

        var resp = new RestApiAppResponse<>(true, List.of(sender, recipient), null);
        return new HttpResponseBuilder().setStatus(StatusCodes.OK).setBody(resp);
    }

}
