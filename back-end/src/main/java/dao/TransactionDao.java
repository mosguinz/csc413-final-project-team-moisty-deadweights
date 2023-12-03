package dao;

import com.mongodb.client.MongoCollection;
import dto.TransactionDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionDao extends BaseDao<TransactionDto> {

    private static TransactionDao instance;

    private TransactionDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static TransactionDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new TransactionDao(MongoConnection.getCollection("TransactionDao"));
        return instance;
    }

    public static TransactionDao getInstance(MongoCollection<Document> collection) {
        instance = new TransactionDao(collection);
        return instance;
    }

    public List<TransactionDto> query(Document filter) {
        List<Document> res = collection.find(filter).into(new ArrayList<>());
        return res.stream().map(TransactionDto::fromDocument).collect(Collectors.toList());
    }

}
