import React, { useState } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { addMessage } from "../../Redux/Message/MessageActions";
import { requestClients } from "../../Redux/Clients/ClientsActions";
import { clientsApi } from "../../services/api";
import FormField from "../Common/FormField";
import { DEFAULT_VALUES } from "../../constants/api";
import './AddClient.css';

const AddClient = ({ addMessage, requestClients }) => {
    const [clientID, setClientID] = useState("");
    const [clientName, setClientName] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);

        if (!clientID || !clientName || !currencyCode) {
            return addMessage('Please complete all fields to add a client.');
        }

        try {
            await clientsApi.create({
                clientId: clientID,
                clientName: clientName,
                currencyCode: currencyCode,
                disbursementsAmount: DEFAULT_VALUES.INITIAL_AMOUNT,
                servicesAmount: DEFAULT_VALUES.INITIAL_AMOUNT,
                amount: DEFAULT_VALUES.INITIAL_AMOUNT,
            });

            setClientID("");
            setClientName("");
            setCurrencyCode("");
            addMessage("Client was created successfully!");
            requestClients('');
        } catch (error) {
            if (error.status === 410) {
                addMessage(`Please use a different client ID. ${clientID} already exists.`);
            } else {
                addMessage("❗ Error occurred while adding data into clients.");
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
        <div className="add-client-container">
            <form onSubmit={handleSubmit} onReset={handleClear} className="add-client-form">
                <FormField
                    id="client-id"
                    label="Client ID"
                    value={clientID}
                    onChange={(e) => setClientID(e.target.value)}
                    required
                />
                <FormField
                    id="client-name"
                    label="Client Name"
                    value={clientName}
                    onChange={(e) => setClientName(e.target.value)}
                    required
                />
                <FormField
                    id="currency-code"
                    label="Currency Code"
                    value={currencyCode}
                    onChange={(e) => setCurrencyCode(e.target.value)}
                    required
                />
                <div className="form-buttons">
                    <button type="submit" className="submit-button">
                        Add Client
                    </button>
                    <button type="reset" className="reset-button">
                        Clear
                    </button>
                </div>
            </form>
        </div>
    );
};

AddClient.propTypes = {
    addMessage: PropTypes.func.isRequired,
    requestClients: PropTypes.func.isRequired,
};

export default connect(null, { addMessage, requestClients })(AddClient);