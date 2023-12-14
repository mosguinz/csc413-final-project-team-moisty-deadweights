import React from 'react';
import { TransactionDto, UserDto } from './dto';

// don't copy this
// without react
// let amount = 0;
// function setAmount(newAmount){
//     amount = newAmount;
//     // trigger page refresh!
// }

// amount = 3; // react doesn't know the value changed
// setAmount(5); // -> no polling + trigger change right away

// // if js wants to know if amount has changed
// // poll
// let lastAmount = amount;
// while(true){
//     if(lastAmount != amount){
//         // changed!
//     }
//     // sleep 500ms
// }

export default function HomePage() {

    const [amount, setAmount] = React.useState('');
    const [transactions, setTransactions] = React.useState([]);
    const [idToUserTable] = React.useState({});
    const [userName, setUserName] = React.useState('');
    const [userId, setUserId] = React.useState('');
    const [balance, setbalance] = React.useState('');

    function updateAmount(event) {
        const numberValue = Number(event.target.value);
        if (isNaN(numberValue)) {
            return;
        }
        setAmount(event.target.value);
    }

    function deposit() {
        const transactionDto = {
            amount: Number(amount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        fetch('/createDeposit', options)
            .then((res) => res.json())
            .then(() => {
                setAmount('');
                fetchTransaction();
                updateUser();
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    async function withdraw() {
        const transactionDto = {
            amount: Number(amount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        fetch('/withdraw', options)
            .then((res) => res.json())
            .then(() => {
                setAmount('');
                fetchTransaction();
                updateUser();
            })
            .catch((error) => {
                setAmount('');
                console.log(error);
            }) // it did not work
    }

    React.useEffect(() => {
        // triggers when componenet mounds
        // https://react.dev/reference/react/useEffect
        // fetching data
        // https://developer.mozilla.org/en-US/docs/Web/API/fetch
        fetchTransaction()
        updateUser();
    }, []);

        /*
         * Get the user by using thier cookie
         *
         */
    async function updateUser() {
        const options = {
            method: 'POST',
            credentials: 'include',
        };
        await fetch('/getUser', options)
            .then((res) => res.json())
            .then((apiRes) => {
                setUserName(apiRes.data[0].userName);
                setbalance(apiRes.data[0].balance);
                setUserId(apiRes.data[0].uniqueId);
            })
            .catch((error) => {
                setAmount('');
                console.log(error);
            }) // it did not work

    }

    async function updateTable() {
        transactions.filter((transaction) => {
            return transaction.transactionType.toString() == "Transfer";
        })
        .forEach( async (transfer) => {
            var toUser = await idToUser(transfer.toId);
            idToUserTable[transfer.toId] = toUser;
            var fromUser = await idToUser(transfer.userId);
            idToUserTable[transfer.userId] = fromUser;
        });
    }

    /**
     * Make the card for listing transactions.
     * @param {String} id
     */
    async function idToUser(id) {
        const options = {
            method: 'POST',
            Headers: { q : id },
            credentials: 'include',
        };
        const res = await fetch('/getUser', options);
        var apiRes = await res.json()
        return apiRes.data[0].userName;
    }


    /*
     * Get the transactions for the given user
     */
    async function fetchTransaction() {
        await updateUser();
        var res = await fetch('/getTransactions');
        var apiRes = await res.json();
        setTransactions(apiRes.data);
        await updateTable();
    }

    /*
     * set button to redirect to transfer page
     */
    function transfer() {
        window.location.href='/transfer';
    }

    /**
     * Make the card for listing transactions.
     * @param {TransactionDto} txDto
     */
    function makeTxCard(txDto) {
        let txMessage;
        let amountPrefix;
        var sender = idToUserTable[txDto.userId];
        var receiver = idToUserTable[txDto.toId];

        switch (txDto.transactionType) {
            case "Deposit":
                txMessage = <h5 className="card-title"><b>You deposited</b> mucho dineros</h5>;
                amountPrefix = "+";
                break;
            case "Transfer":
                if (txDto.toId === userId) {
                    txMessage = <h5 className="card-title"><b>{ sender }</b> paid <b>you</b></h5>;
                    amountPrefix = "+";
                    break;
                }
                txMessage = <h5 className="card-title"><b>You</b> paid <b>{receiver}</b></h5>;
                amountPrefix = "-";
                break;
            case "Withdraw":
                txMessage = <h5 className="card-title"><b>You withdrew</b> some money</h5>;
                amountPrefix = "-";
                break;
            default:
                // TODO: Deal with this shit
                txMessage = "";
                amountPrefix = "";
        }

        return (<div class="card">
            <div class="card-body row">
                <div class="col-md-8">
                    {txMessage}
                    <h6 class="card-subtitle mb-2 text-body-secondary">{new Date(txDto.timestamp).toString()}</h6>
                    <p>{txDto.transactionType}</p>
                </div>
                <div class="col-md-4 d-grid text-center">
                    <p class="fs-1">{amountPrefix}${txDto.amount}</p>
                </div>
            </div>
        </div>)
    }

    return (
        <div>
            <h1>Home Page</h1>

            <div class="card">
                <p class="card-header">Welcome back, <b>{userName}</b></p>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <p className="card-title">Wallet balance</p>
                            <b class="fs-1">${balance}</b>
                        </div>
                        <div class="col-md-4 d-grid text-center">
                            <button type="button" class="btn btn-primary" onClick={transfer}>Pay or Request</button>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input id="amount" type="number" class="form-control" onChange={updateAmount}></input>
                        <button type="button" class="btn btn-outline-primary" onClick={deposit}>Deposit</button>
                        <button type="button" class="btn btn-outline-primary" onClick={withdraw}>Withdraw</button>
                    </div>
                </div>
            </div>

            <div id="tx-feed" class="row px-4 gy-2">
                {transactions.map(tx => makeTxCard(tx))}
            </div>

        </div>
    );
}
