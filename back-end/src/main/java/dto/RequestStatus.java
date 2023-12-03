package dto;

public enum RequestStatus {
    Accepted("Accepted"),
    Rejected("Rejected"),
    Sent("Sent");

    private final String stringValue;

    RequestStatus(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
