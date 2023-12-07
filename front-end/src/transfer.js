import React from 'react';
import { Navigate } from 'react-router-dom';

export default function Transfer() {
    const [userName, setUserName] = React.useState('');
    const [amount, setAmount] = React.useState('');
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [message, setMessage] = React.useState('');

    function updateUserName(event) {
        setUserName(event.target.value); // updating react state variable
    }

    function updateAmount(event) {
        setAmount(event.target.value);
    }
    // redirect
    if (shouldRedirect) {
        return <Navigate to="/home" replace={true} />;
    }

    function transfer() {
        const transactionDto = {
            amount: Number(amount),
            toId: userName
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transactionDto),
            credentials: 'include',
        };
        fetch('/transfer', options)
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                setAmount('');
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    return (
    <div class="container mt-5">
        <h1 class="text-center">Pay or Request Funds</h1>

        <div class="row mt-4">
            <div class="col-md-6 offset-md-3">
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="Search User" aria-label="Search here"
                        aria-describedby="basic-addon2" onChange={updateUserName}/>
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" type="button" >Search</button>
                    </div>
                </div>

                <div class="input-group mb-3">
                    <input type="number" class="form-control" placeholder="$" aria-label="Enter numbers"
                        aria-describedby="basic-addon2" onChange={updateAmount}/>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-md-6 offset-md-3 text-center">
                <button type="button" class="btn btn-primary mr-2" onClick={transfer}>Transfer</button>
                <button type="button" class="btn btn-secondary" >Request</button>
            </div>
        </div>
    </div>
    );
}
