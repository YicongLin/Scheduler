import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    // API call to register user
    const login = () => {

    }
    
    return (
        <>
            <h2>Login</h2>
        
            <div>Email</div>
                <input type="text" value={email} onChange={val => {setEmail(val.target.value)}} /><br />
            <div>Password</div>
                <input type="password" value={password} onChange={val => {setPassword(val.target.value)}}/><br />
            
            <br />

            <div>Haven't got an account? <b onClick={() => navigate('/register')}>Sign up</b></div><br />

            <button type="button" onClick={login}>Login</button>
        </>
    );
};

export default Login;
