import React, { Component } from "react";
import "./Analytics.css";
import Service from "../../Service";

class Analytics extends Component {
  constructor(props) {
    super(props);
    this.state = { isLoading: true, data: [] };
    this.service = new Service();
  }

  componentDidMount() {
    // hit the analytics api once the component is mounted
    this.service
      .getAnalytics(1)
      .then(res => {
        this.setState({ isLoading: false, data: res.data });
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
    return (
      <frameset scrolling="no" rows="50%,*,10%" cols="25%,*,25%">
        {/* <frame src="https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev/analytics" /> */}
        <frame src="http://localhost:5003/" />
        <noframes>
          Your browser does not support frames. To wiew this page please use
          supporting br
        </noframes>
      </frameset>
    );
  }
}

export default Analytics;
