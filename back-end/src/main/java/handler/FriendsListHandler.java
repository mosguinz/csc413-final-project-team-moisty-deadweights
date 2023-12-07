package handler;

import dao.FriendsListDao;
import dto.FriendsListDto;
import request.ParsedRequest;
import response.HttpResponseBuilder;
import static handler.GsonTool.gson;
import static handler.StatusCodes.*;

public class FriendsListHandler implements BaseHandler {

    private final FriendsListDao friendsListDao;

    public FriendsListHandler() {
        this.friendsListDao = FriendsListDao.getInstance();
    }

    @Override
    public HttpResponseBuilder handleRequest(ParsedRequest request) {
        String action = extractAction(request);

        try {
            switch (action) {
                case "addFriend":
                    return handleAddFriend(request);
                case "removeFriend":
                    return handleRemoveFriend(request);
                default:
                    return new HttpResponseBuilder().setStatus(BAD_REQUEST);
            }
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(SERVER_ERROR);
        }
    }

    private String extractAction(ParsedRequest request) {
        // Assuming the action is part of the path (e.g., /friendsList/addFriend)
        String[] pathParts = request.getPath().split("/");
        if (pathParts.length > 2) {
            return pathParts[2];
        }
        return null;
    }

    private HttpResponseBuilder handleAddFriend(ParsedRequest request) {
        try {
            // Deserialize request body to FriendsListDto
            FriendsListDto dto = gson.fromJson(request.getBody(), FriendsListDto.class);
            friendsListDao.addFriend(dto.getUserId(), dto.getFriends().get(0)); // Assuming list contains the friend to add

            return new HttpResponseBuilder().setStatus(OK);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(SERVER_ERROR);
        }
    }

    private HttpResponseBuilder handleRemoveFriend(ParsedRequest request) {
        try {
            // Deserialize request body to FriendsListDto
            FriendsListDto dto = gson.fromJson(request.getBody(), FriendsListDto.class);
            friendsListDao.removeFriend(dto.getUserId(), dto.getFriends().get(0)); // Assuming list contains the friend to remove

            return new HttpResponseBuilder().setStatus(OK);
        } catch (Exception e) {
            return new HttpResponseBuilder().setStatus(SERVER_ERROR);
        }
    }
}