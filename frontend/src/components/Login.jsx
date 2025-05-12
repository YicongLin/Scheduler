import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { v4 as uuidv4 } from 'uuid';

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    // API call to register user
    const login = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            alert("Please fill in all fields");
            return;
        }

        if (password.length < 8) {
            alert("Password must be at least 8 characters long");
            return;
        }
        
        const deviceId = uuidv4();
        const loginData = {
            "email": email,
            "password": password,
            "deviceId": deviceId
        }

        try {
            const response = await axios.post("http://localhost:8080/auth/login", loginData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
            console.log(response.data);
            if (!response.data.token) {
                alert(response.data.message);
                return;
            }
            localStorage.setItem('token', response.data.token);
            navigate('/dashboard');
        } catch (error) {
            console.error("Login error:", error.response?.data || error.message);
            alert("Login failed");
        }
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
