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
            <input value={searchText} onChange={updateSearchText} type="text" placeholder='Search User'></input>
            <button type='button' onClick={search}>Submit</button> 
            <table>
                <thead>
                     <th>Users Found</th>
                </thead>
                <tbody>
                    {searchResults.map(result => (
                        <tr>
                            <td>{result.userName}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}