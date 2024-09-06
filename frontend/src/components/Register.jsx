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
            <div className="register-container">
                <div className="lhs register">
                    <img src="timetravel.jpg" alt="" />
                </div>

                <div className="rhs register">

                    <form className="register-form" onSubmit={register}>
                        <h2 className="register-heading">Register</h2>
                        <label>Username</label> 
                        <input type="text" value={username} onChange={val => setUsername(val.target.value)} /> <br />

                        <label>Email</label>
                        <input type="text" value={email} onChange={val => setEmail(val.target.value)} /> <br />

                        <label>Password</label>
                        <input type="password" value={password} onChange={val => setPassword(val.target.value)} /> <br />

                        <label>Confirm Password</label>
                        <input type="password" value={confirmPassword} onChange={val => setConfirmPassword(val.target.value)} /> <br />

                        <br />
                        <div>Already have an account? <b className="sign" onClick={() => navigate('/login')}>Sign in</b></div><br />

                        <button type="submit" >Register</button>
                    </form>
                    

                </div>
            </div>
        </>
    );
};

export default Register;
