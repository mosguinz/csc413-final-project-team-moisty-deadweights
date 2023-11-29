package dao;

import com.mongodb.client.MongoCollection;
import dto.AuthDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthDao extends BaseDao<AuthDto> {

    private static AuthDao instance;

    private AuthDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static AuthDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new AuthDao(MongoConnection.getCollection("AuthDao"));
        return instance;
    }

    public static AuthDao getInstance(MongoCollection<Document> collection) {
        instance = new AuthDao(collection);
        return instance;
    }

    @Override
    public List<AuthDto> query(Document filter) {
        List<Document> res = collection.find(filter).into(new ArrayList<>());
        return res.stream().map(AuthDto::fromDocument).collect(Collectors.toList());
    }
}
