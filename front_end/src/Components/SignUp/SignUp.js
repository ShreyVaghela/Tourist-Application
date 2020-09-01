import React, { Component } from "react";
import FormErrors from "../../FormErrors";
import Validate from "../../FormValidation";
import { Auth } from "aws-amplify";
import "react-datepicker/dist/react-datepicker.css";
import Service from "../../Service";

class SignUp extends Component {
  state = {
    email: "",
    password: "",
    name: "",
    dob: "",
    sex: "",
    phone: "",
    confirmpassword: "",
    errors: {
      cognito: null,
      blankfield: false,
      passwordmatch: false
    }
  };

  constructor() {
    super();
    this.service = new Service();
  }

  RemoveErrorState = () => {
    this.setState({
      errors: {
        cognito: null,
        blankfield: false,
        passwordmatch: false
      }
    });
  };

  SubmitAction = async event => {
    event.preventDefault();
    console.log(this.state.dob);
    // Form validation
    this.RemoveErrorState();
    const error = Validate(event, this.state);
    if (error) {
      this.setState({
        errors: { ...this.state.errors, ...error }
      });
      return;
    }

    // AWS Cognito integration here

    const { email, password, name, dob, sex, phone } = this.state;

    try {
      const signUpResponse = await Auth.signUp({
        username: email,
        password,
        attributes: {
          name: name,
          "custom:Birthday": dob,
          gender: sex,
          phone_number: phone
        }
      });
      this.props.history.push("/SignupConfirmation");
      /////////Sending user data to server
      
      const user_data = {
        email: email,
        password: password,
        name: name,
        dob: dob,
        sex: sex,
        phone: phone
      };
      this.service
        .postUserDetails(user_data)
        .then(res => {
          console.log(res.data.response);
        })
        //Error handling
        .catch(error => {
          //data is empty in case of error.
          this.setState({
            isLoading: false,
            data: []
          });
        });
      /////////
    } catch (error) {
      let err = null;
      !error.message ? (err = { message: error }) : (err = error);
      this.setState({
        errors: {
          ...this.state.errors,
          cognito: err
        }
      });
    }
  };

  onInputChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
    document.getElementById(event.target.id).classList.remove("is-danger");
  };

  handleChange = date => {
    console.log(date);
    this.setState({
      dob: date
    });
  };

  render() {
    return (
      <section className="section main">
        <div className="container">
          <FormErrors formerrors={this.state.errors} />

          <form onSubmit={this.SubmitAction}>
            <div className="field">
              <p className="control">
                <input
                  className="input"
                  type="text"
                  id="name"
                  aria-describedby="NameHelper"
                  placeholder="Please Enter the Complete Name"
                  value={this.state.name}
                  onChange={this.onInputChange}
                />
              </p>
            </div>

            <div className="div field ">
              <label htmlFor="sex" className="float-left">
                Please select the Gender
              </label>
              <select
                id="sex"
                value={this.state.sex}
                onChange={this.onInputChange}
                className="form-control"
              >
                <option value="0"></option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            {/* <p> Please Enter the date of birth</p> */}

            <label htmlFor="dob" className="float-left">
              Please enter the date of birth
            </label>
            <div className="div field ">
              {/* <DatePicker
                id="dob"
                className="input form-control"
                dateFormat="yyyy-mm-dd"
                maxDate={new Date()}
                selected={this.state.dob}
                onChange={this.handleChange}
              /> */}
              <input
                id="dob"
                name="dob"
                max={new Date()}
                className="form-control"
                type="date"
                required
                dateformat="yyyy-mm-dd"
                selected={this.state.dob}
                onChange={e => this.setState({ dob: e.target.value })}
              />
            </div>

            <div className="field">
              <p className="control">
                <input
                  className="input"
                  type="text"
                  id="phone"
                  aria-describedby="Helper"
                  placeholder="Please Enter the mobile number along with country code"
                  value={this.state.phone}
                  onChange={this.onInputChange}
                />
              </p>
            </div>

            <div className="div field">
              <p className="control has-icons-left has-icons-right">
                <input
                  className="input"
                  type="email"
                  id="email"
                  aria-describedby="emailHelp"
                  placeholder="Please Enter email address"
                  value={this.state.email}
                  onChange={this.onInputChange}
                />
                <span className="icon is-small is-left">
                  <i className="fas fa-envelope"></i>
                </span>
              </p>
            </div>
            <div className="div field">
              <p className="control has-icons-left">
                <input
                  className="input"
                  type="password"
                  id="password"
                  placeholder="Please Enter Password"
                  value={this.state.password}
                  onChange={this.onInputChange}
                />
                <span className="icon is-small is-left">
                  <i className="fas fa-lock"></i>
                </span>
              </p>
            </div>
            <div className="div field">
              <p className="control has-icons-left">
                <input
                  className="input"
                  type="password"
                  id="confirmpassword"
                  placeholder="Please Confirm the password"
                  value={this.state.confirmpassword}
                  onChange={this.onInputChange}
                />
                <span className="icon is-small is-left">
                  <i className="fas fa-lock"></i>
                </span>
              </p>
            </div>
            <div className="div field">
              <p className="control">
                <button className="button is-primary">Sign Up</button>
              </p>
            </div>
          </form>
        </div>
      </section>
    );
  }
}

export default SignUp;
