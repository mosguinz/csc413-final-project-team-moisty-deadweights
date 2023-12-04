package handler;

import dao.FriendRequestDao;
import dto.FriendRequestDto;
import dto.RequestStatus;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import static handler.GsonTool.gson;
import static handler.StatusCodes.*;

public class FriendRequestHandler implements BaseHandler {

    private final FriendRequestDao friendRequestDao;

    public FriendRequestHandler(FriendRequestDao friendRequestDao) {
        this.friendRequestDao = friendRequestDao;
    }

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        String action = extractAction(request); // Extract the action from the request

        try {
            switch (action) {
                case "send":
                    return handleSendRequest(request);
                case "accept":
                    return handleAcceptRequest(request);
                case "reject":
                    return handleRejectRequest(request);
                default:
                    return new HttpResponseBuilder().setStatus(BAD_REQUEST);
            }
        } catch (Exception e) {
            // Log error and return an appropriate error response
            return new HttpResponseBuilder().setStatus(SERVER_ERROR);
        }
    }

    private String extractAction(ParsedRequest request) {
        // Extract action from the path or query parameter
        // Adjust this method based on how your application's requests are structured
        String path = request.getPath();
        if (path != null && !path.isEmpty()) {
            String[] pathParts = path.split("/");
            // Assuming the action is the second part of the path (e.g., /friendrequest/send)
            if (pathParts.length > 1) {
                return pathParts[1];
            }
        }
        // Optionally, check for action in query parameters if not found in the path
        // return request.getQueryParam("action");
        return null; // Default to null if action cannot be determined
    }


    private HttpResponseBuilder handleSendRequest(ParsedRequest request) {
        FriendRequestDto friendRequestDto = gson.fromJson(request.getBody(), FriendRequestDto.class);
        friendRequestDto.setStatus(RequestStatus.Sent);
        friendRequestDao.put(friendRequestDto);
        return new HttpResponseBuilder().setStatus(OK);
    }

    private HttpResponseBuilder handleAcceptRequest(ParsedRequest request) {
        FriendRequestDto friendRequestDto = gson.fromJson(request.getBody(), FriendRequestDto.class);
        updateFriendRequestStatus(friendRequestDto, RequestStatus.Accepted);
        return new HttpResponseBuilder().setStatus(OK);
    }

    private HttpResponseBuilder handleRejectRequest(ParsedRequest request) {
        FriendRequestDto friendRequestDto = gson.fromJson(request.getBody(), FriendRequestDto.class);
        updateFriendRequestStatus(friendRequestDto, RequestStatus.Rejected);
        return new HttpResponseBuilder().setStatus(OK);
    }

    private void updateFriendRequestStatus(FriendRequestDto friendRequestDto, RequestStatus status) {
        FriendRequestDto existingRequest = friendRequestDao.findFriendRequest(friendRequestDto.getSenderId(), friendRequestDto.getReceiverId());
        if (existingRequest != null) {
            existingRequest.setStatus(status);
            friendRequestDao.put(existingRequest);
        }
    }

    // Additional methods as/if needed
}