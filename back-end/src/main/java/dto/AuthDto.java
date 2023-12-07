package dto;

import org.bson.Document;

public class AuthDto extends BaseDto {

    private String userId;
    private String userName;
    private Long expireTime;
    private String hash;

    public static AuthDto fromDocument(Document document) {
        final AuthDto auth = new AuthDto();
        auth.expireTime = document.getLong("expireTime");
        auth.userName = document.getString("userName");
        auth.hash = document.getString("hash");
        auth.userId = document.getString("userId");
        return auth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    @Override
    public Document toDocument() {
        return new Document()
                .append("userName", this.userName)
                .append("expireTime", this.expireTime)
                .append("hash", this.hash)
                .append("userId", this.userId);
    }
}
