import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Form, Button, Header, Segment } from "semantic-ui-react";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const navigate = useNavigate();

    const handleContinue = () => {
        console.log("Submitted email:", email);
    };

    const handleCancel = () => {
        navigate("/login");
    };

    return (
        <div className="forgot-password-container">
            <Container textAlign="center">
                <Header as="h2" className="forgot-password-header">
                    Reset your password
                </Header>
                <Segment className="forgot-password-box">
                    <Form>
                        <Form.Input
                            fluid
                            placeholder="Enter your email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <Button color="blue" onClick={handleContinue}>
                            Continue
                        </Button>
                        <Button color="grey" onClick={handleCancel}>
                            Cancel
                        </Button>
                    </Form>
                </Segment>
            </Container>
        </div>
    );
};

export default ForgotPassword;
