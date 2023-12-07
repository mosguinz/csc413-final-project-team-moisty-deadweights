import React from 'react';

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

    const [ammount, setAmmount] = React.useState('');
    const [transactions, setTransactions] = React.useState([]);

    function updateAmmount(event) {
        const numberValue = Number(event.target.value);
        console.log(numberValue)
        if (isNaN(numberValue)) {
            return;
        }
        setAmmount(event.target.value);
    }

    function deposit() {
        const transactionDto = {
            amount: Number(ammount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        fetch('/createDeposit', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setAmmount('');
                fetchTransaction();
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    async function withdraw() {
        const transactionDto = {
            amount: Number(ammount)
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        const result = await fetch('/withdraw', options);
        const apiRes = await result.json();
        setAmmount('');
        fetchTransaction();
    }

    React.useEffect(() => {
        // triggers when componenet mounds
        // https://react.dev/reference/react/useEffect
        // fetching data
        // https://developer.mozilla.org/en-US/docs/Web/API/fetch
        fetchTransaction();
    }, []);

    function fetchTransaction() {
        fetch('/getTransactions')
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                // will see transactions here as they come
                setTransactions(apiRes.data);
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    return (
        <div>
            <h1>Home Page</h1>

            <div class="card">
                <p class="card-header">Welcome back, <b>username</b></p>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <p className="card-title">Wallet balance</p>
                            <b class="fs-1">$500.23</b>
                        </div>
                        <div class="col-md-4 d-grid text-center">
                            <button type="button" class="btn btn-primary">Pay or Request</button>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input id="amount" type="number" class="form-control"></input>
                        <button type="button" class="btn btn-outline-primary">Deposit</button>
                        <button type="button" class="btn btn-outline-primary">Withdraw</button>
                    </div>
                </div>
            </div>

            $<input value={ammount} onChange={updateAmmount} />
            <button onClick={deposit}>Deposit</button>
            <button onClick={withdraw}>Withdraw</button>

            <table>
                <thead>
                    <tr>
                        <th>
                            Type
                        </th>
                        <th>
                            Amount
                        </th>
                        <th>
                            ID
                        </th>
                        <th>
                            From user
                        </th>
                        <th>
                            To user
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {transactions.map(transaction => (
                        <tr>
                            <td>{transaction.transactionType}</td>
                            <td>{transaction.amount}</td>
                            <td>{transaction.uniqueId}</td>
                            <td>{transaction.userId}</td>
                            <td>{transaction.toId}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
