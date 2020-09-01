import React, { Component } from "react";
import "./Search.css";
import Descriptions from "../Descriptions/Descriptions";
import Service from "../../Service";
import Book from "../Book/Book";

class Search extends Component {
  constructor(props) {
    super(props);
    this.state = { data: [], noData: false, showBook: false, id: "" };
    this.service = new Service();
  }

  handleSubmit = event => {
    // fetch all the location data
    event.preventDefault();
    this.service
      .getLocationData(event.target.location.value)
      .then(res => {
        if (res.data.items.length === 0) alert("No data found");
        this.setState({
          data: res.data.items
        });
      })
      //Error handling
      .catch(error => {
        //data is empty in case of error.
        this.setState({
          isLoading: false,
          data: []
        });
      });
  };
  // navigate back from bokking module to search module
  handleBack = () => {
    this.setState({
      showBook: false
    });
  };

  //  navigating to booking module
  bookTicket = destId => {
    this.setState({
      showBook: true,
      destId: destId
    });
  };

  render() {
    return (
      <div className="container">
        {!this.state.showBook && (
          <div>
            <div className="mt-5">
              <form
                className="form-inline mr-auto d-flex justify-content-center"
                onSubmit={this.handleSubmit}
              >
                <input
                  className="form-control col-lg-9 col-md-9"
                  type="text"
                  placeholder="Search"
                  aria-label="Search"
                  name="location"
                  required
                />
                <button
                  className="btn btn-dark btn-rounded  my-0 ml-sm-2 col-lg-2 col-md-2"
                  type="submit"
                >
                  Search
                </button>
              </form>
            </div>
            <div>
              <ul className="list-group d-flex justify-content-center">
                {/* Details of locations  */}

                {this.state.data.map((row, index) => (
                  <Descriptions
                    // pass properties to child component
                    key={row.id}
                    data={row}
                    onBook={this.bookTicket}
                  />
                ))}
              </ul>
            </div>
          </div>
        )}
        {/* show booking module when user clicks on book */}
        {this.state.showBook && this.props.auth.isAuthenticated && (
          <Book
            // pass properties to child component
            destId={this.state.destId}
            onBack={this.handleBack}
            user={this.props.auth.email}
          />
        )}

        {this.state.showBook &&
          !this.props.auth.isAuthenticated &&
          this.props.history.push("/Login")}
      </div>
    );
  }
}

export default Search;
