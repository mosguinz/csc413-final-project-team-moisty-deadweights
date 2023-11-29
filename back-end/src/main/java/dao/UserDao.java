package dao;

import com.mongodb.client.MongoCollection;
import dto.UserDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao extends BaseDao<UserDto> {

    private static UserDao instance;

    private UserDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static UserDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new UserDao(MongoConnection.getCollection("UserDao"));
        return instance;
    }

    public static UserDao getInstance(MongoCollection<Document> collection) {
        instance = new UserDao(collection);
        return instance;
    }

    public List<UserDto> query(Document filter) {
        List<Document> res = collection.find(filter).into(new ArrayList<>());
        return res.stream().map(UserDto::fromDocument).collect(Collectors.toList());
    }

}
