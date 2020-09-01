import React, { Component } from "react";

class PasswordChangeConfirmation extends Component {
  render() {
    return (
      <section className="section main">
        <div className="div container">
          <p>Your password has been successfully updated!</p>
          <p>Please Login to your account with new password</p>
        </div>
      </section>
    );
  }
}

export default PasswordChangeConfirmation;