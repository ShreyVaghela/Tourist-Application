import React, { Component } from "react";
import "./Orders.css";
import Service from "../../Service";

class Orders extends Component {
  constructor(props) {
    super(props);
    this.state = { isLoading: false, data: [] };
    this.service = new Service();
  }

  // componentWillMount(){}
  componentDidMount() {
    this.service
      .getOrderDeatils(this.props.auth.email)
      .then(res => {
        if (res.data.items.length === 0) {
          alert("No orders found");
        } else this.setState({ isLoading: false, data: res.data.items });
      })
      //Error handling
      .catch(error => {
        //data is empty in case of error.
        this.setState({
          isLoading: false,
          data: []
        });
      });
  }

  render() {
    var start = 1;
    return (
      <div className="container">
        {this.props.auth.isAuthenticated && (
          <div>
            <h1 className="h1 float-left mt-5">Order Details</h1>
            <table className="table table-striped mt-5">
              <thead>
                <tr>
                  <th>Order Number</th>
                  <th>Date</th>
                  <th>Source</th>
                  <th>Destination</th>
                  <th>Number of Passengers</th>
                </tr>
              </thead>
              <tbody>
                {this.state.data.map((row, index) => (
                  <tr key={row.id}>
                    <td>{index + start}</td>
                    <td>{row.date}</td>
                    <td>{row.source_id}</td>
                    <td>{row.dest_id}</td>
                    <td>{row.num_passengers}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {!this.props.auth.isAuthenticated && (
          <p>please login to see your order details</p>
        )}
      </div>
    );
  }
}

export default Orders;
