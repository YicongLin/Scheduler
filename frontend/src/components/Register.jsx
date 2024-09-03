import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const navigate = useNavigate();

    // API call to register user
    const register = () => {

    }
    
    return (
        <>
            <h2>Register</h2>
            <div>Username</div>
                <input type="text" value={username} onChange={val => {setUsername(val.target.value)}}/><br />
            <div>Email</div>
                <input type="text" value={email} onChange={val => {setEmail(val.target.value)}} /><br />
            <div>Password</div>
                <input type="password" value={password} onChange={val => {setPassword(val.target.value)}}/><br />
            <div>Confirm Password</div>
                <input type="password" value={confirmPassword} onChange={val => {setConfirmPassword(val.target.value)}}/><br />
            
            <br />

            <div>Already have an account? <b onClick={() => navigate('/login')}>Sign in</b></div><br />

            <button type="button" onClick={register}>Register</button>
        </>
    );
};

export default Register;
