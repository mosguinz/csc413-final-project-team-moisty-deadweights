package dto;

import org.bson.Document;

public class TransferRequestDto extends BaseDto {

    public double amount;
    public String fromId;
    public String toId;

    public TransferRequestDto() {
        super();
    }

    public TransferRequestDto(String uniqueId) {
        super(uniqueId);
    }

    public TransferRequestDto(double amount, String fromId, String toId) {
        this.amount = amount;
        this.fromId = fromId;
        this.toId = toId;
    }

    public static TransferRequestDto fromDocument(Document match) {
        TransferRequestDto transferRequestDto = new TransferRequestDto();
        transferRequestDto.amount = match.getDouble("amount");
        transferRequestDto.fromId = match.getString("fromId");
        transferRequestDto.toId = match.getString("toId");
        return transferRequestDto;
    }

    public Document toDocument() {
        return new Document()
                .append("amount", this.amount)
                .append("fromId", this.fromId)
                .append("toId", this.toId);
    }

}
