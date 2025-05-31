import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Form, Button, Header, Segment } from "semantic-ui-react";
import axios from "axios";

const Redeem = () => {
    const [loading, setLoading] = useState(false);
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const navigate = useNavigate();
    const urlParams = new URLSearchParams(window.location.search);
    const resetToken = urlParams.get('token');

    if (!resetToken) {
        alert("Invalid reset link. Please request a new password reset.");
        return;
    }
    const redeem = async (e) => {
        e.preventDefault();
        
        if (loading) {
            return;
        }

        setLoading(true);

        if (!newPassword || !confirmPassword) {
            alert("Please fill in all fields");
            return;
        }

        if (newPassword !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        if (newPassword.length < 8) {
            alert("Password must be at least 8 characters long");
            return;
        }

        

        const request = {
            resetToken: resetToken,
            password: newPassword
        }

        try {
            const response = await axios.post("http://localhost:8080/auth/forgotPassword/redeem", request);
            if (response.data.success) {
                alert("Password successfully updated! You can now log in with your new password.");
                
                setNewPassword('');
                setConfirmPassword('');
                
                navigate('/login');
            } else {
                alert(response.data.message || "Failed to update password. Please try again.");
            }
        } catch (error) {
            console.error('Password reset error:', error);
    
            if (error.response) {
                // Server responded with error status
                const errorMessage = error.response.data?.message || "Failed to update password";
                alert(errorMessage);
            } else if (error.request) {
                // Request was made but no response received
                alert("Network error. Please check your connection and try again.");
            } else {
                // Something else happened
                alert("An unexpected error occurred. Please try again.");
            }
        }

        setLoading(false);
    }

    return (
        <Container text style={{ marginTop: "5em" }}>
            <Segment padded="very">
                <Header as="h2" textAlign="center">Reset Your Password</Header>
                <Form>
                    <Form.Input
                        type="password"
                        label="New Password"
                        placeholder="Enter new password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
                    <Form.Input
                        type="password"
                        label="Confirm Password"
                        placeholder="Re-enter new password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                    <Button primary fluid onClick={redeem}>Reset Password</Button>
                </Form>
            </Segment>
        </Container>
    );
};

export default Redeem;