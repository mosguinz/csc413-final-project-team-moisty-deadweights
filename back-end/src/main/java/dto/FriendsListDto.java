package dto;
import org.bson.Document;
import java.util.List;

public class FriendsListDto extends BaseDto {
    private String userId;
    private List<String> friends;

    // Constructors
    public FriendsListDto() {
        super();
    }

    public FriendsListDto(String uniqueId, String userId, List<String> friends) {
        super(uniqueId);
        this.userId = userId;
        this.friends = friends;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("userId", this.userId);
        doc.append("friends", this.friends);
        return doc;
    }

    public static FriendsListDto fromDocument(Document document) {
        FriendsListDto dto = new FriendsListDto();
        dto.setUserId(document.getString("userId"));
        dto.setFriends(document.getList("friends", String.class));
        return dto;
    }
}
