package dao;

import com.mongodb.client.MongoCollection;
import dto.TransferRequestDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransferRequestDao extends BaseDao<TransferRequestDto> {

    private static TransferRequestDao instance;

    private TransferRequestDao(MongoCollection<Document> collection) {
        super(collection);
    }

    public static TransferRequestDao getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new TransferRequestDao(MongoConnection.getCollection("TransferRequestDao"));
        return instance;
    }

    public static TransferRequestDao getInstance(MongoCollection<Document> collection) {
        instance = new TransferRequestDao(collection);
        return instance;
    }

    public List<TransferRequestDto> query(Document filter) {
        List<Document> res = collection.find(filter).into(new ArrayList<>());
        return res.stream().map(TransferRequestDto::fromDocument).collect(Collectors.toList());
    }

}
