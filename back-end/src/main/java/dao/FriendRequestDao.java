package dao;

import com.mongodb.client.MongoCollection;
import dto.FriendRequestDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestDao extends BaseDao<FriendRequestDto> {

    private static FriendRequestDao instance;

    private FriendRequestDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static FriendRequestDao getInstance() {
        if (instance == null) {
            instance = new FriendRequestDao(MongoConnection.getCollection("friend_requests"));
        }
        return instance;
    }

    public static FriendRequestDao getInstance(MongoCollection<Document> collection) {
        if (instance == null) {
            instance = new FriendRequestDao(collection);
        }
        return instance;
    }

    @Override
    public List<FriendRequestDto> query(Document filter) {
        List<Document> documents = collection.find(filter).into(new ArrayList<>());
        return documents.stream().map(FriendRequestDto::fromDocument).collect(Collectors.toList());
    }

    // Find all friend requests for a specific user
    public List<FriendRequestDto> findFriendRequestsByUserId(String userId) {
        Document filter = new Document()
                .append("$or", Arrays.asList(
                        new Document("senderId", userId),
                        new Document("receiverId", userId)
                ));
        return query(filter);
    }

    // Find a specific friend request between two users
    public FriendRequestDto findFriendRequest(String senderId, String receiverId) {
        Document filter = new Document("senderId", senderId)
                .append("receiverId", receiverId);
        List<FriendRequestDto> result = query(filter);
        return result.isEmpty() ? null : result.get(0);
    }
}