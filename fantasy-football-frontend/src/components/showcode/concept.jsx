import React, { useState } from 'react';

function Counter(props) {
    const [count, setCount] = useState(0);


    const increment = () => {
        setCount(count + 1);
    };

    return (
        <div>
            <h1>Hello: {props.name}!</h1>
            <h1>Count: {count}</h1>
            <button onClick={increment}>Increase</button>
        </div>
    );
}

export default Counter;
