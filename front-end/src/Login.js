import React from 'react';
import { Navigate } from 'react-router-dom';

export default function Login() {
    const [userName, setUserName] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [shouldRedirect, setShouldRedirect] = React.useState(false);
    const [message, setMessage] = React.useState('');

    function updateUserName(event) {
        setUserName(event.target.value); // updating react state variable
    }

    function updatePassword(event) {
        setPassword(event.target.value);
    }

    function register() {
        setMessage('');
        console.log('Registering ' + userName + ' ' + password);
        // send request to back end
        const userDto = {
            userName: userName,
            password: password
        };
        console.log(userDto);
        const options = {
            method: 'POST',
            body: JSON.stringify(userDto)
        };
        fetch('/createUser', options) // network call = lag
            .then((res) => res.json()) // it worked, parse result
            .then((apiRes) => {
                console.log(apiRes); // RestApiAppResponse
                if (apiRes.status) { // at the app layer, tell if worked or not
                    setUserName('');
                    setPassword('');
                    setMessage('Your account has been created!');
                } else {
                    setMessage(apiRes.message); // tell end user why?
                }
            })
            .catch((error) => {
                console.log(error);
            }) // it did not work
    }

    function logIn() {
        setMessage('');
        console.log('Loging in ' + userName + ' ' + password);
        // send request to back end
        const userDto = {
            userName: userName,
            password: password
        };
        console.log(userDto);
        const options = {
            method: 'POST',
            body: JSON.stringify(userDto)
        };
        fetch('/login', options) // network call = lag
            //.then((res) => res.json()) // it worked, parse result
            .then((apiRes) => {
                console.log(apiRes);
                if (apiRes.ok) {
                    console.log('Login worked');
                    setShouldRedirect(true);
                } else {
                    setMessage('Failed to log in');
                }
                console.log('Worked'); // RestApiAppResponse

            })
            .catch((error) => {
                console.log(error);
                setMessage('Failed to log in');
            }) // it did not work
    }

    // redirect
    if (shouldRedirect) {
        return <Navigate to="/home" replace={true} />;
    }

    return (
        <div>
            <h1>Login Page</h1>
            {message}
            <form>
                <div class="row mb-3">
                    <label for="inputUsername" class="col-sm-2 col-form-label">Username</label>
                    <div class="col-sm-10">
                        <input value={userName} onChange={updateUserName} type="text" class="form-control" id="inputUsername" />
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
                    <div class="col-sm-10">
                        <input value={password} onChange={updatePassword} type="password" class="form-control" id="inputPassword" />
                    </div>
                </div>
                <button onClick={register} type="submit" class="btn btn-primary">Register</button>
                <button onClick={logIn} type="submit" class="btn btn-primary">Log in</button>
            </form>
        </div>
    );
}
