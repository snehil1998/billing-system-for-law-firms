import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import { addMessage } from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";
import { getClientsData } from "../../redux/clients/ClientsSelectors";
import { clientsApi } from "../../services/api";
import "./AddClient.css";

const AddClient = (props) => {
  const [clientID, setClientID] = useState("");
  const [clientName, setClientName] = useState("");
  const [currencyCode, setCurrencyCode] = useState("");

  useEffect(() => {
    props.requestClients("");
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
    <div id="add-client-container" className="dropdown-components-container">
      <form onSubmit={handleSubmit} onReset={handleClear} id="add-client-form-container" className="dropdown-form-container">
        <div id="add-client-client-id-container" className="dropdown-field-container">
          <div id="add-client-client-id-translation" className="dropdown-translation">
            {"Client ID: "}
          </div>
          <input
            id="add-client-client-id-input-field"
            className="dropdown-input-field"
            type="text"
            value={clientID}
            onChange={(e) => setClientID(e.target.value)}
          />
        </div>
        <div id="add-client-client-name-container" className="dropdown-field-container">
          <div id="add-client-client-name-translation" className="dropdown-translation">
            {"Client Name: "}
          </div>
          <input
            id="add-client-client-name-input-field"
            className="dropdown-input-field"
            type="text"
            value={clientName}
            onChange={(e) => setClientName(e.target.value)}
          />
        </div>
        <div id="add-client-currency-code-container" className="dropdown-field-container">
          <div id="add-client-currency-code-translation" className="dropdown-translation">
            {"Currency Code: "}
          </div>
          <input
            id="add-client-currency-code-input-field"
            className="dropdown-input-field"
            type="text"
            value={currencyCode}
            onChange={(e) => setCurrencyCode(e.target.value)}
          />
        </div>
        <div id="add-client-add-button-container" className="dropdown-button-container">
          <button type="submit" id="add-client-add-button" className="dropdown-button">
            ADD
          </button>
          <button type="reset" id="add-client-clear-button" className="dropdown-button">
            CLEAR
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