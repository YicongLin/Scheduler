import React from "react";
// import { useNavigate } from "react-router-dom";

const Dashboard = () => {

    // API call to register user
    // const dashboard = () => {

    // }
    
    return (
        <>
            <div className="dashboard-container">
                <div className="top-bar">
                    <h2>Welcome to Dashboard, username</h2>
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
