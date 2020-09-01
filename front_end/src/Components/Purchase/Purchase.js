import React, { Component } from "react";
import "./Purchase.css";

class Purchase extends Component {
  constructor(props) {
    super(props);
    this.state = { cardNumber: "", cvv: "", date: "" };
  }
  render() {
    return (
      // Pyment module. Opens invoice page on successful payment
      <div className="container">
        <div className="">
          <div className="">
            <div className="panel panel-default">
              <div className="panel-heading">
                <h3 className="panel-title">Payment Details</h3>
              </div>
              <div className="panel-body">
                <form method="post" action="http://127.0.0.1:5005/makePayment">
                  {/* hidden fields to post required data  */}
                  <input
                    type="hidden"
                    name="source_id"
                    value={this.props.busInfo.data.src_id}
                  ></input>

                  <input
                    type="hidden"
                    name="dest_id"
                    value={this.props.busInfo.data.dest_id}
                  ></input>
                  <input
                    type="hidden"
                    name="bus_id"
                    value={this.props.busInfo.data.id}
                  ></input>
                  <input
                    type="hidden"
                    name="date"
                    value={this.props.date}
                  ></input>
                  <input
                    type="hidden"
                    name="numPass"
                    value={this.props.numPass}
                  ></input>
                  <input
                    type="hidden"
                    name="price"
                    value={this.props.busInfo.data.price}
                  ></input>
                  <input
                    type="hidden"
                    name="userId"
                    value={this.props.user}
                  ></input>
                  {/* card name in required format */}
                  <div className="form-group">
                    <div className="input-group">
                      <input
                        type="text"
                        className="form-control"
                        name="cardName"
                        placeholder="Name on Card"
                        required
                        autoFocus
                      />
                    </div>
                  </div>
                  {/* card number in required format */}
                  <div className="form-group">
                    <div className="input-group">
                      <input
                        type="text"
                        className="form-control"
                        name="cardNumber"
                        pattern="[\d| ]{16,22}"
                        placeholder="Card Number(16-22 digits)"
                        required
                        autoFocus
                      />
                    </div>
                  </div>
                  {/* card expiration in required format */}
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      name="expiryDate"
                      pattern="\d\d/\d\d"
                      placeholder="Valid Thru (MM/YY)"
                      required
                    />
                  </div>
                  {/* card cvv in required format */}
                  <div className="form-group">
                    <input
                      type="password"
                      className="form-control"
                      name="cvCode"
                      pattern="\d{3,4}"
                      placeholder="CVC (3 digits)"
                      required
                    />
                  </div>
                  <div>
                    <button
                      type="submit"
                      className="btn btn-success btn-lg btn-block"
                    >
                      Pay
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default Purchase;

// https://codesandbox.io/s/react-final-form-credit-card-example-ur4tl
