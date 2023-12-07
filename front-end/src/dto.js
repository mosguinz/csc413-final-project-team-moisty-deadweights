class TransactionDto {

    /** @type {string} */
    userId;
    /** @type {string} */
    toId;
    /** @type {number} */
    amount;
    /** @type {"Deposit" | "Transfer" | "Withdraw"} */
    transactionType;
    /** @type {number} */
    timestamp;

    constructor(o) {
        this.userId = o.userId;
        this.toId = o.toId;
        this.amount = o.amount;
        this.transactionType = o.transactionType;
        this.timestamp = o.timestamp;
    }
}

class UserDto {

    /** @type {string} */
    userName;
    /** @type {number} */
    balance;

    constructor(o) {
        this.userName = o.userName;
        this.balance = o.balance;
    }
}

export {
    TransactionDto, UserDto,
}
