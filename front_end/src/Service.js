import axios from "axios";

/*This file contains all the rest api requests */

// const base_url = "http://127.0.0.1:5000/";
const base_url = "https://dbe6st6u2k.execute-api.us-east-1.amazonaws.com/dev/";

class Service {
  getLocationData = location => {
    return axios.get(base_url + "search/" + location);
    // return axios.get(search_url + "search/" + location);
  };
  getOrderDeatils = id => {
    return axios.get(base_url + "orderdetails/" + id);
    // return axios.get(orders_url + id);
  };
  getAnalytics = () => {
    return axios.get(base_url + "analytics");
    // return axios.get(analytics_url);
  };
  getSources = destId => {
    return axios.get(base_url + "getSources/" + destId);
  };
  getBuses = (sourceId, destId) => {
    return axios.get(base_url + "getBuses/" + sourceId + "/" + destId);
  };
  makePayment = data => {
    return axios.post(base_url + "makePayment", data);
  };
  postUserDetails = user_data => {
     return axios.post(base_url + "registration/", user_data);
  };
}
export default Service;
