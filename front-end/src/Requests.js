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

export default function RequestsPage() {

    const [requests, setRequests] = React.useState([]);

    async function Accept(){
        const transferRequestDto = {
            status: "Accepted"
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transferRequestDto),
            credentials: 'include',
        }
        const result = await fetch('/resolveRequest', options);
        console.log(result);
    }

    async function Decline(){
        const transferRequestDto = {
            status: "Rejected"
        };
        const options = {
            method: 'POST',
            body: JSON.stringify(transferRequestDto),
            credentials: 'include',
        }
        const result = await fetch('/resolveRequest', options);
        console.log(result);
    }

    React.useEffect(() => {
        // triggers when componenet mounds
        // https://react.dev/reference/react/useEffect
        // fetching data
        // https://developer.mozilla.org/en-US/docs/Web/API/fetch
        fetchRequests();
    }, []);

    function fetchRequests(){
        fetch('/getRequests')
            .then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
                // will see transactions here as they come
                setRequests(apiRes.data);
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    return (
        <div>
            <h1>Request Page</h1>

            <table>
                <thead>
                    <tr>
                        <th>
                            To user
                        </th>
                        <th>
                            Amount
                        </th>
                        <th>
                            Accept
                        </th>
                        <th>
                            Decline
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {requests.map(transaction => (
                        <tr>
                            <td>{transaction.fromUserName}</td>
                            <td>{transaction.amount}</td>
                            <td>
                                <button onClick={Accept}>Accept</button>
                            </td>
                            <td>
                                <button onClick={Decline}>Decline</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
