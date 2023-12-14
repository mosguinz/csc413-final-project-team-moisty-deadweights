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

    async function Accept(transferRequestDto) {
        console.log(transferRequestDto);
        transferRequestDto["status"] = "Accepted"
        const options = {
            method: 'POST',
            body: JSON.stringify(transferRequestDto),
            credentials: 'include',
        }
        const result = await fetch('/resolveRequest', options);
        console.log("results", result);
    }

    async function Decline(transferRequestDto) {
        console.log(transferRequestDto);
        transferRequestDto["status"] = "Rejected"
        const options = {
            method: 'POST',
            body: JSON.stringify(transferRequestDto),
            credentials: 'include',
        }
        const result = await fetch('/resolveRequest', options);
        console.log(await result.json());
    }

    function fetchRequests() {
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

    React.useEffect(() => {
        // triggers when componenet mounds
        // https://react.dev/reference/react/useEffect
        // fetching data
        // https://developer.mozilla.org/en-US/docs/Web/API/fetch
        fetchRequests();
    }, []);

    function makeReqCard(reqDto) {
        return(<div class="row">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">{reqDto.fromUserName} requests</h5>
                        <p class="fs-1 card-text">${reqDto.amount}</p>
                        <a href="#" class="btn btn-outline-primary" onClick={Decline.bind(null, reqDto)}>Decline</a>
                        <a href="#" class="btn btn-primary" onClick={Accept.bind(null, reqDto)}>Pay</a>
                    </div>
                </div>
            </div>)
    }




    return (
        <div>
            <h1>Request Page</h1>

            {/* TODO: Wrap each card in loop to display transactions. */}
            <div class="col-sm-6 mb-3 mb-sm-0">
                { requests.map( (req) => makeReqCard(req)) }
            </div>
            {/* End of bootstrap components */}
        </div>
    );
}
