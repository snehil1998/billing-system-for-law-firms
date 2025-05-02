import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import { addMessage } from "../../redux/message/MessageActions";
import { requestCases } from "../../redux/cases/CasesActions";
import { casesApi } from "../../services/api";
import "../common/AddForm.css";
import { getClientsData } from "../../redux/clients/ClientsSelectors";

const AddCase = (props) => {
  const [caseID, setCaseID] = useState("");
  const [caseName, setCaseName] = useState("");
  const [clientID, setClientID] = useState("");

  useEffect(() => {
    props.requestCases("");
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    window.scrollTo(0, 0);
    if (!caseID || !caseName || !clientID) {
      return props.addMessage("Please complete all fields to add a case.");
    }
    const client = props.clientsData.find(c => c.clientId === clientID);
    try {
      await casesApi.create({
        caseId: caseID,
        caseName: caseName,
        clientId: clientID,
        currencyCode: client ? client.currencyCode : undefined,
        disbursementsAmount: 0,
        servicesAmount: 0,
        amount: 0,
      });
      setCaseID("");
      setCaseName("");
      setClientID("");
      props.addMessage("Case was created successfully!");
      props.requestCases("");
    } catch (error) {
      if (error.status === 410) {
        props.addMessage(`Please use a different case ID. ${caseID} already exists.`);
      } else {
        props.addMessage("❗ Error occurred while adding data into cases.");
      }
    }
  };

  const handleClear = (e) => {
    e.preventDefault();
    setCaseID("");
    setCaseName("");
    setClientID("");
  };

  return (
    <div className="add-form-container">
      <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
        <div className="form-group">
          <label htmlFor="caseId" className="form-label">Case ID:</label>
          <input
            id="caseId"
            className="form-input"
            type="text"
            value={caseID}
            onChange={(e) => setCaseID(e.target.value)}
            placeholder="Enter case ID"
          />
        </div>
        <div className="form-group">
          <label htmlFor="caseName" className="form-label">Case Name:</label>
          <input
            id="caseName"
            className="form-input"
            type="text"
            value={caseName}
            onChange={(e) => setCaseName(e.target.value)}
            placeholder="Enter case name"
          />
        </div>
        <div className="form-group">
          <label htmlFor="clientId" className="form-label">Client:</label>
          <select
            id="clientId"
            className="form-input"
            value={clientID}
            onChange={(e) => setClientID(e.target.value)}
          >
            <option value="">Select a client</option>
            {props.clientsData && props.clientsData.map((client) => (
              <option key={client.clientId} value={client.clientId}>
                {client.clientName}
              </option>
            ))}
          </select>
        </div>        
        <div className="form-buttons">
          <button type="submit" className="form-submit-btn">
            Add Case
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
  requestCases: (caseID) => {
    dispatch(requestCases(caseID));
  },
  addMessage: (message) => {
    dispatch(addMessage(message));
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(AddCase);
