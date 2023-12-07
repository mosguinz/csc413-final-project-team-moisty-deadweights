package dto;

import org.bson.Document;

public class UserDto extends BaseDto {

    private String userName;
    private String password;
    private Double balance = 0.0d;

    public UserDto() {
        super();
    }

    public UserDto(String uniqueId) {
        super(uniqueId);
    }

    public static UserDto fromDocument(Document match) {
        UserDto userDto = new UserDto();
        userDto.userName = match.getString("userName");
        userDto.password = match.getString("password");
        userDto.balance = match.getDouble("balance");
        userDto.loadUniqueId(match);
        return userDto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Document toDocument() {
        return new Document()
                .append("userName", this.userName)
                .append("password", this.password)
                .append("balance", this.balance);
    }
}
