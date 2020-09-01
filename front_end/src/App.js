import React, { Component } from "react";
import "./App.css";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";
import Home from "./Components/Home/Home";
import Search from "./Components/Search/Search";
import Analytics from "./Components/Analytics/Analytics";
import Orders from "./Components/Orders/Orders";
import Book from "./Components/Book/Book";
import Login from "./Components/Login/Login";
import SignUp from "./Components/SignUp/SignUp";
import SignupConfirmation from "./Components/SignUp/SignupConfirmation";
import ForgotPassword from "./Components/ForgotPassword/ForgotPassword";
import ForgotPasswordVerification from "./Components/ForgotPassword/ForgotPasswordVerification";
import PasswordChangeConfirmation from "./Components/ForgotPassword/PasswordChangeConfirmation";
import LoginConfirmation from "./Components/Login/LoginConfirmation";
import { Auth } from "aws-amplify";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faEdit } from "@fortawesome/free-solid-svg-icons";
library.add(faEdit);

class App extends Component {
  state = {
    isAuthenticated: false,
    isAuthenticating: true,
    user: null,
    email: ""
  };

  setAuthStatus = authenticated => {
    this.setState({ isAuthenticated: authenticated });
  };

  setUser = user => {
    this.setState({ user: user });
  };

  setEmail = email => {
    this.setState({ email: email });
  };

  async componentDidMount() {
    try {
      const user_info = await Auth.currentUserInfo();
      const email = user_info.attributes.email;
      this.setEmail(email);
      this.setAuthStatus(true);
      const user = await Auth.currentAuthenticatedUser();
      this.setUser(user);
    } catch (error) {
      console.log(error);
    }
    this.setState({ isAuthenticating: false });
  }
  render() {
    const authprops = {
      isAuthenticated: this.state.isAuthenticated,
      user: this.state.user,
      email: this.state.email,
      setAuthStatus: this.setAuthStatus,
      setUser: this.setUser,
      setEmail: this.setEmail
    };
    return (
      // Navigation between different components.
      !this.state.isAuthenticating && (
        <div className="App">
          {/* Routing between different pages/tabs */}
          <Router>
            <div className="App">
              <Home auth={authprops} />
              <Switch>
                <Redirect exact from="/" to="/Search" />
                <Route
                  exact
                  path="/Search"
                  render={props => <Search {...props} auth={authprops} />}
                ></Route>
                <Route path="/Analytics" component={Analytics}></Route>
                <Route
                  exact
                  path="/Orders"
                  render={props => <Orders {...props} auth={authprops} />}
                ></Route>
                <Route
                  exact
                  path="/Book"
                  render={props => <Book {...props} auth={authprops} />}
                ></Route>
                <Route
                  exact
                  path="/Login"
                  render={props => <Login {...props} auth={authprops} />}
                ></Route>
                <Route
                  exact
                  path="/SignUp"
                  render={props => <SignUp {...props} auth={authprops} />}
                ></Route>
                <Route
                  exact
                  path="/ForgotPassword"
                  render={props => (
                    <ForgotPassword {...props} auth={authprops} />
                  )}
                />
                <Route
                  exact
                  path="/ForgotPasswordVerification"
                  render={props => (
                    <ForgotPasswordVerification {...props} auth={authprops} />
                  )}
                />
                <Route
                  exact
                  path="/SignupConfirmation"
                  render={props => (
                    <SignupConfirmation {...props} auth={authprops} />
                  )}
                />
                <Route
                  exact
                  path="/PasswordChangeConfirmation"
                  render={props => (
                    <PasswordChangeConfirmation {...props} auth={authprops} />
                  )}
                />
                <Route
                  exact
                  path="/LoginConfirmation"
                  render={props => (
                    <LoginConfirmation {...props} auth={authprops} />
                  )}
                />
              </Switch>
            </div>
          </Router>
        </div>
      )
    );
  }
}

export default App;
