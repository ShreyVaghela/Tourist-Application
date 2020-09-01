import React, { Component } from "react";
import { IndexLinkContainer } from "react-router-bootstrap";
import logo from "../../images/logo.png";
import { Auth } from "aws-amplify";

/* This component provides routing functionality between components.
Contains two childern component 'Athletes' and 'Upload Athletes' 
*/

class Home extends Component {
  handleLogOut = async event => {
    event.preventDefault();
    try {
      Auth.signOut();
      this.props.auth.setAuthStatus(false);
      this.props.auth.setUser(null);
    } catch (error) {
      console.log(error.message);
    }
  };

  render() {
    return (
      // Banner with logo
      <div className="masthead">
        <header className="container-fluid">
          <div className="p-4">
            <div className="logo float-left">
              <IndexLinkContainer to="/" className=" ">
                {/* Logo image */}
                <img alt="logo" src={logo}></img>
              </IndexLinkContainer>
            </div>
            <div className="float-right">
              {!this.props.auth.isAuthenticated && (
                <div>
                <IndexLinkContainer to="/SignUp">
                  <a className="nav-link" href="/">
                  <strong>Sign Up</strong>
                  </a>
                </IndexLinkContainer>

                <IndexLinkContainer to="/login">
                  <a className="nav-link" href="/">
                  <strong>Log In</strong>
                  </a>
                </IndexLinkContainer>

                  {/* <a href="/SignUp" className="button is-black">
                    <strong>Sign Up</strong>
                  </a> */}
                  {/* <a href="/login" className="button is-black">
                    <strong>Log In</strong>
                  </a> */}
                </div>
              )}
              {this.props.auth.isAuthenticated && (
                <a
                  href="/"
                  onClick={this.handleLogOut}
                  className="button is-black"
                >
                  <strong>Log Out</strong>
                </a>
              )}
            </div>

            <div className="clearfix"></div>
          </div>
        </header>
        {/* Navigation bar */}
        <nav className="navbar navbar-expand-sm navbar-light bg-light text-primary justify-content-between pd-5">
          <div className="navbar-header col-sm-12 col-lg-12 col-md-12">
            <ul className="navbar-nav">
              <li className="nav-item col-sm-1 col-lg-1 col-md-1 nav-link">
                {/* Routing */}
                <IndexLinkContainer to="/Search">
                  <a className="nav-link" href="/">
                    <b>Search</b>
                  </a>
                </IndexLinkContainer>
              </li>
              <li className="nav-item col-sm-1 col-lg-2 col-md-3 nav-link">
                <IndexLinkContainer to="/Orders">
                  <a className="nav-link" href="/">
                    <b>Order Details</b>
                  </a>
                </IndexLinkContainer>
              </li>
              <li className="nav-item col-sm-1 col-lg-1 col-md-1 nav-link">
                <IndexLinkContainer to="/Analytics">
                  <a className="nav-link" href="/">
                    <b>Analytics</b>
                  </a>
                </IndexLinkContainer>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    );
  }
}

export default Home;
