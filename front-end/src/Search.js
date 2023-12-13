import React from 'react';

export default function SearchUsers() {
    const [searchText, setSearchText] = React.useState('');
    const [searchResults, setSearchResults] = React.useState([]);

    function updateSearchText(event) {
        setSearchText(event.target.value);
        search(searchText);
    }

    async function search(searchText) {
        // updateSearchText(event);
        console.log(searchText);
            
        const response = await fetch("/search", {
            method : 'GET',
            headers : {
                q : searchText
            },
        }).then((res) => res.json())
        .then((apiRes) => {
            console.log(apiRes);
            setSearchResults(apiRes.data);
        })
        .catch((error) =>{
            console.log(error);
        })
    }

    return (
        
        <div>
            <h1>Search Users: </h1>
            <div class="input-group">
                <input value={searchText} class="form-control" onChange={updateSearchText} type="text" placeholder='Search User'></input>
                <button type='button' class="btn btn-primary" onClick={search}>Submit</button> 
            </div>
                {searchResults.map(result => (    
                    <div class="col-md-4 d-grid text-center" style={{margin:3}}>
                        <button type="button" class="btn btn-outline-primary">{result.userName}</button> 
                    </div>
                ))}
        </div>
    )
}