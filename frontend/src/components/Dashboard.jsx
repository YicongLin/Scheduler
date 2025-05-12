import axios from "axios";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
    const navigate = useNavigate();
    const [token, setToken] = useState(null);

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        setToken(storedToken);
    }, [navigate]);
    // API call to register user
    // const dashboard = () => {

    // }
    if (!token) {
        return (
            <>
                <h1>Unauthorised Access</h1>
            </>
        );
    }
    console.log(localStorage.getItem('token'));
    const logout = async () => {
        try {
            const authHeader = {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
            }
            if (token) {
                const response = await axios.post("http://localhost:8080/auth/logout", {}, authHeader);
                console.log(response.data);

            }
        } catch (error) {
            
        }
        
        localStorage.removeItem('token');
        navigate('/login');
    }
    
    return (
        <>
            <div className="dashboard-container">
                <div className="top-bar">
                    <h2>Welcome to Dashboard!</h2>
                    <button onClick={logout}>logout</button>
                </div>

                <div className="side-bar">
                    <div className="shared-channel">
                        Shared Channel
                    </div>
                </div>

                <div className="main">
                    Calendar
                </div>
            </div>
        </>
    );
};

export default Dashboard;
