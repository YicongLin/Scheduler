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
            <div className="login-container">
                <div className="lhs login">
                    <img src="timetravel.jpg" alt="" />
                </div>

                <div className="rhs login">
                    <form className="register-form" onSubmit={login}>
                        <h2>Login</h2>

                        <label>Email</label>
                        <input type="text" value={email} onChange={val => setEmail(val.target.value)} /> <br />

                        <label>Password</label>
                        <input type="password" value={password} onChange={val => setPassword(val.target.value)} /> 
                
                        <label className="forgot">Forgot password?</label>

                        <br />
                        <div>Haven't got an account? <b className="sign" onClick={() => navigate('/register')}>Sign up</b></div><br />

                        <button type="submit" >Login</button>
                    </form>
                </div>
            </div>
        </>
    );
};

export default Login;
