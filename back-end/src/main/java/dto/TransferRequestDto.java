package dto;

import org.bson.Document;

public class TransferRequestDto extends BaseDto {

    public double amount;
    public String fromUserName;
	public String fromId;
    public String toUserName;
    public String toId;
    public RequestStatus status;

    public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public TransferRequestDto() {
        super();
    }

    public TransferRequestDto(String uniqueId) {
        super(uniqueId);
    }

    public String getFromId() {
        return this.fromId;
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public void setToUserName(String toId) {
		this.toUserName = toId;
	}

    public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus state) {
		this.status = state;
	}

	public Document toDocument() {
        return new Document()
                .append("amount", this.amount)
                .append("fromUserName", this.fromUserName)
                .append("fromId", this.fromId)
                .append("toUserName", this.toUserName)
                .append("toId", this.toUserName)
                .append("status", this.status);
    }

    public static TransferRequestDto fromDocument(Document match) {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.amount = match.getDouble("amount");
        transferRequestDto.fromUserName = match.getString("fromUserName");
        transferRequestDto.fromId = match.getString("fromId");
        transferRequestDto.toUserName = match.getString("toUserName");
        transferRequestDto.toId = match.getString("toId");
        transferRequestDto.status = switch (match.get("status").toString()) {
            case "Sent" -> RequestStatus.Sent;
            case "Accepted" -> RequestStatus.Accepted;
            case "Rejected" -> RequestStatus.Rejected;
            default -> RequestStatus.Sent;
        };
        return transferRequestDto;
    }
}
