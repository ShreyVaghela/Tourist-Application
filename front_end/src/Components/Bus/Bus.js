import React, { Component } from "react";
import "./Bus.css";
import Purchase from "../Purchase/Purchase";
import "../../css/style.css";

class Bus extends Component {
  constructor(props) {
    super(props);
    this.state = { numPass: 1, active: false, activeItem: "" };
  }

  //take users to payment page
  buyTickets = (e, index) => {
    e.preventDefault();
    this.props.isActive(index);
    this.toggleClass(index);
  };

  //close and open payment page
  toggleClass = index => {
    const currentState = this.state.active;
    this.setState({ active: !currentState, activeItem: index });
  };
  // set number of passengers
  handleChange = e => {
    this.setState({
      numPass: e.target.value
    });
  };

  render() {
    return (
      <li
        // populate bus data
        key={this.props.data.id}
        className="list-group-item  m-3"
        // {
        //   this.state.active && this.props.index === this.props.activeIndex
        //     ? "list-group-item  m-3 active "
        //     : "list-group-item  m-3"
        // }
      >
        <form
          className=" m-1 "
          onSubmit={e => this.buyTickets(e, this.props.index)}
        >
          <div className="clearfix">
            <div className="float-left">
              <h4 className="h4 text-left">
                Bus Number: {this.props.data.bus_no}
              </h4>

              <span className=" text-left h4">
                {this.props.data.src} <i>({this.props.data.dep_time})</i>
                ->
                {this.props.data.dest} <i>({this.props.data.arr_time})</i>{" "}
              </span>
              {/* choose number of passengers */}
              <p className="text-left">
                <label htmlFor="num_passenger">Number of Passengers: </label>
                <input
                  type="number"
                  name="num_passenger"
                  disabled={
                    this.state.active &&
                    this.props.index === this.props.activeIndex
                  }
                  min="1"
                  max={this.props.data.seats}
                  onChange={this.handleChange}
                  required
                />
              </p>
            </div>

            <div className="float-right">
              <p className="h3">
                Price:{" "}
                <var>
                  <abbr title="CAD">$</abbr>
                  {this.props.data.price * this.state.numPass}
                </var>
              </p>
              <button type="submit" className="btn btn-outline-primary">
                Buy Tickets
              </button>
            </div>
          </div>
        </form>

        <div
          className={
            this.state.active && this.props.index === this.props.activeIndex
              ? "fadeIn"
              : "fadeOut"
          }
        >
          {/* payment page, passing required data  */}
          <Purchase
            user={this.props.user}
            busInfo={this.props}
            date={this.props.date}
            numPass={this.state.numPass}
          ></Purchase>
        </div>
      </li>
    );
  }
}

export default Bus;
