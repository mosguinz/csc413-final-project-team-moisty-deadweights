package dto;

import org.bson.Document;
import java.util.List;
public class FriendRequestDto extends BaseDto {
    private String senderId;
    private String receiverId;
    private RequestStatus status;

    // Constructors
    public FriendRequestDto() {
        super();
    }

    public FriendRequestDto(String uniqueId) {
        super(uniqueId);
    }

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }



    // Convert DTO to MongoDB Document
    public Document toDocument() {
        return new Document()
                .append("senderId", this.senderId)
                .append("receiverId", this.receiverId)
                .append("status", this.status.toString());
    }

    // Convert MongoDB Document to DTO
    public static FriendRequestDto fromDocument(Document document) {
        FriendRequestDto dto = new FriendRequestDto();
        dto.senderId = document.getString("senderId");
        dto.receiverId = document.getString("receiverId");
        dto.status = RequestStatus.valueOf(document.getString("status"));
        return dto;
    }
}