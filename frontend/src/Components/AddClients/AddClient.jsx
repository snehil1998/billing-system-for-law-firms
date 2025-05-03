import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import { addMessage } from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";
import { getClientsData } from "../../redux/clients/ClientsSelectors";
import { clientsApi } from "../../services/api";
import "../common/AddForm.css";
import { CURRENCY_API } from "../../constants/api";

const AddClient = (props) => {
  const [clientID, setClientID] = useState("");
  const [clientName, setClientName] = useState("");
  const [currencyCode, setCurrencyCode] = useState("");
  const [currencies, setCurrencies] = useState({});
  useEffect(() => {
    props.requestClients("");
    async function fetchData() {
      await fetch(CURRENCY_API)
          .then(response => response.json())
          .then(json => {
              setCurrencies(json['data']);
          }).catch(error => {
              props.addMessage("❗ Error occurred while fetching currency codes from currency api.");
              console.log("error fetching currency codes from currency api: " + error);
          })
    }
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    window.scrollTo(0, 0);
    if (!clientID || !clientName || !currencyCode) {
      return props.addMessage("Please complete all fields to add a client.");
    }
    try {
      await clientsApi.create({
        clientId: clientID,
        clientName: clientName,
        currencyCode: currencyCode,
        disbursementsAmount: 0,
        servicesAmount: 0,
        amount: 0,
      });
      setClientID("");
      setClientName("");
      setCurrencyCode("");
      props.addMessage("Client was created successfully!");
      props.requestClients("");
    } catch (error) {
      if (error.status === 410) {
        props.addMessage(`Please use a different client ID. ${clientID} already exists.`);
      } else {
        props.addMessage("❗ Error occurred while adding data into clients.");
      }
    }
  };

  const handleClear = (e) => {
    e.preventDefault();
    setClientID("");
    setClientName("");
    setCurrencyCode("");
  };

  return (
    <div className="add-form-container">
      <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
        <div className="form-group">
          <label htmlFor="clientId" className="form-label">
            Client ID:
          </label>
          <input
            id="clientId"
            className="form-input"
            type="text"
            value={clientID}
            onChange={(e) => setClientID(e.target.value)}
            placeholder="Enter client ID"
          />
        </div>
        <div className="form-group">
          <label htmlFor="clientName" className="form-label">
            Client Name:
          </label>
          <input
            id="clientName"
            className="form-input"
            type="text"
            value={clientName}
            onChange={(e) => setClientName(e.target.value)}
            placeholder="Enter client name"
          />
        </div>
        <div className="form-group">
          <label htmlFor="currencyCode" className="form-label">
            Currency Code:
          </label>
          <select
            id="currencyCode"
            className="form-input"
            value={currencyCode}
            onChange={(e) => setCurrencyCode(e.target.value)}
          >
            <option value="">Select currency</option>
            {Object.entries(currencies).map(([code, data]) => (
              <option key={code} value={code}>
                {code} - {data.name}
              </option>
            ))}
          </select>
        </div>
        <div className="form-buttons">
          <button type="submit" className="form-submit-btn">
            Add Client
          </button>
          <button type="reset" className="form-clear-btn">
            Clear
          </button>
        </div>
      </form>
    </div>
  );
};

const mapStateToProps = (state) => ({
  clientsData: getClientsData(state),
});

const mapDispatchToProps = (dispatch) => ({
  requestClients: (clientID) => {
    dispatch(requestClients(clientID));
  },
  addMessage: (message) => {
    dispatch(addMessage(message));
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(AddClient);