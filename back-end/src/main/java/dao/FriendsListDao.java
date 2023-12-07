package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dto.FriendsListDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class FriendsListDao extends BaseDao<FriendsListDto> {
    private static FriendsListDao instance;

    private FriendsListDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static FriendsListDao getInstance() {
        if (instance == null) {
            instance = new FriendsListDao(MongoConnection.getCollection("friends_lists"));
        }
        return instance;
    }

    public static FriendsListDao getInstance(MongoCollection<Document> collection) {
        if (instance == null) {
            instance = new FriendsListDao(collection);
        }
        return instance;
    }

    @Override
    public List<FriendsListDto> query(Document filter) {
        List<Document> documents = collection.find(filter).into(new ArrayList<>());
        return documents.stream().map(FriendsListDto::fromDocument).collect(Collectors.toList());
    }

    public void addFriend(String userId, String friendId) {
        FriendsListDto friendsList = findFriendsListByUserId(userId);
        if (friendsList == null) {
            // Create a new friends list with the friendId
            FriendsListDto newFriendsList = new FriendsListDto();
            newFriendsList.setUserId(userId);
            newFriendsList.setFriends(new ArrayList<>(Collections.singletonList(friendId)));
            collection.insertOne(newFriendsList.toDocument());
        } else {
            // Add friendId to the existing friends list
            Document filter = new Document("userId", userId);
            collection.updateOne(filter, Updates.addToSet("friends", friendId));
        }
    }

    public void removeFriend(String userId, String friendId) {
        Document filter = new Document("userId", userId);
        collection.updateOne(filter, Updates.pull("friends", friendId));
    }

    public FriendsListDto findFriendsListByUserId(String userId) {
        Document filter = new Document("userId", userId);
        return query(filter).stream().findFirst().orElse(null);
    }
}
