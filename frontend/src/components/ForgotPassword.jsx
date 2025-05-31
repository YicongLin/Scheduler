import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Form, Button, Header, Segment } from "semantic-ui-react";
import axios from "axios";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const forgotPassword = async (e) => {
        e.preventDefault();

        if (!email) {
            alert("Please enter your email address");
            return;
        }

        // Basic email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert("Please enter a valid email address");
            return;
        }

        setLoading(true);

        try {
            const response = await axios.post(
                `http://localhost:8080/auth/forgotPassword?email=${encodeURIComponent(email)}`,
                {},
                {
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );

            console.log(response.data);
            
            if (response.data.success) {
                alert("Password reset email sent! Please check your inbox.");
                navigate("/login");
            } else {
                alert(response.data.message || "Failed to send reset email");
            }
        } catch (error) {
            console.error("Forgot password error:", error.response?.data || error.message);
            alert(error.response?.data?.message || "Failed to send reset email. Please try again.");
        } finally {
            setLoading(false);
        }
    };

    const cancel = () => {
        navigate("/login");
    };

    return (
        <div className="forgot-password-container">
            <Container textAlign="center">
                <Header as="h2" className="forgot-password-header">
                    Reset your password
                </Header>
                <Segment className="forgot-password-box">
                    <Form onSubmit={forgotPassword}>
                        <Form.Input
                            fluid
                            placeholder="Enter your email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            disabled={loading}
                        />
                        <Button 
                            color="blue" 
                            type="submit"
                            loading={loading}
                            disabled={loading}
                        >
                            Continue
                        </Button>
                        <Button 
                            color="grey" 
                            onClick={cancel}
                            disabled={loading}
                        >
                            Cancel
                        </Button>
                    </Form>
                </Segment>
            </Container>
        </div>
    );
};

export default ForgotPassword;