import React, { Component } from 'react';
import FormErrors from "../../FormErrors";
import Validate from "../../FormValidation";
import {Auth} from "aws-amplify";

class LoginConfirmation extends Component {

    state = {
        verificationcode: "",
        errors: {
          cognito: null,
          blankfield: false
        }
      };

      clearErrorState = () => {
        this.setState({
          errors: {
            cognito: null,
            blankfield: false
          }
        });
      };

      VerificationCodeHandler = async event => {
        event.preventDefault();
    
        // Form validation
        this.clearErrorState();
        const error = Validate(event, this.state);
        if (error) {
          this.setState({
            errors: { ...this.state.errors, ...error }
          });
        }
    
        // AWS Cognito integration here
        try{
            await Auth.confirmSignIn(
                this.props.auth.user,
              this.state.verificationcode
            );
            this.props.auth.setAuthStatus(true);
            this.props.history.push('/');
          }catch(error){
            console.log(error);
          }
        };

        onInputChange = event => {
            this.setState({
              [event.target.id]: event.target.value
            });
            document.getElementById(event.target.id).classList.remove("is-danger");
          };

        render() {
            return (
              <section className="section main">
                <div className="container">
                  <h1>verifying the user</h1>
                  <p>
                    Please enter the verification code sent to to registered mobile number.
                  </p>
                  <FormErrors formerrors={this.state.errors} />
                  <form onSubmit={this.VerificationCodeHandler}>
                    <div className="field">
                      <p className="control">
                        <input
                          type="text"
                          className="input"
                          id="verificationcode"
                          aria-describedby="verificationCodeHelp"
                          placeholder="Enter verification code"
                          value={this.state.verificationcode}
                          onChange={this.onInputChange}
                        />
                      </p>
                    </div>
                    
                    <div className="field">
                      <p className="control">
                        <button className="button is-success">
                          Submit
                        </button>
                      </p>
                    </div>
                  </form>
                </div>
              </section>
            );
          }
}

export default LoginConfirmation;