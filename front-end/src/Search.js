import React from 'react';

export default function SearchUsers() {
    const [searchText, setSearchText] = React.useState('');

    // function updateSearchText(event) {
    //     // setSearchText = event.target.value;
    // }

    const search = async (event) => {
        setSearchText = event.target.value;
            
            const response = await fetch("/search", {
                method : 'GET',
                headers : {
                    q : searchText
                },
            }).then((res) => res.json())
            .then((apiRes) => {
                console.log(apiRes);
            })
            .catch((error) =>{
                console.log(error);
            })
    }

    return (
        <div>
            <input value={searchText} onChange={search} type="text" placeholder='Search User'></input>
            <button type='submit'>Submit</button> 
        </div>
    )
}