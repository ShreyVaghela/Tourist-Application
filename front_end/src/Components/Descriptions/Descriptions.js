import React, { Component } from "react";
import "./Descriptions.css";

class Descriptions extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  // componentWillMount(){}
  // componentDidMount(){}
  // componentWillUnmount(){}

  // componentWillReceiveProps(){}
  // shouldComponentUpdate(){}
  // componentWillUpdate(){}
  // componentDidUpdate(){}

  render() {
    return (
      <li
        key={this.props.data.id}
        className="list-group-item container mt-5  col-lg-8 shadow p-3 mb-5 bg-white rounded"
      >
        <h2 className="h2 m-1">{this.props.data.name}</h2>
        <img
          className="m-1"
          src={this.props.data.image}
          alt={this.props.name}
        ></img>
        <p>{this.props.data.description}</p>
        <div className="m-4">
          <h4 className="m-1 h4">Highlights</h4>
          <p className="">
            <i>{this.props.data.highlights}</i>
          </p>
        </div>
        <h4 className="h4 m-3">Estimated trip cost: {this.props.data.price}</h4>

        <button
          className="btn btn-info btn-lg m-1 btn-lg"
          onClick={() => this.props.onBook(this.props.data.address_id)}
        >
          Book
        </button>
      </li>
    );
  }
}

export default Descriptions;
