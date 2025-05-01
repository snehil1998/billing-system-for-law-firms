import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import {requestAttorneys} from "../../redux/attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {addMessage} from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";
import "../common/AddForm.css";

const DeleteServicePricing = (props) => {
    const [selectedAttorney, setSelectedAttorney] = useState('');
    const [clientId, setClientId] = useState("");

    let handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        try {
            if(clientId === "" || selectedAttorney === '') {
                return props.addMessage('Please complete all the fields to delete service pricing.');
            }
            const servicePricingList = [{clientId: clientId, price: -1}]
            const selectedAttorneyArray = selectedAttorney.split(',');
            let res = await fetch("/backend/attorneys=" + selectedAttorneyArray[0], {
                method: "PUT",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    firstName: selectedAttorneyArray[1],
                    lastName: selectedAttorneyArray[2],
                    servicePricing: servicePricingList,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setSelectedAttorney('');
                setClientId("");
                props.addMessage("Service pricing was deleted successfully!");
            } else {
                props.addMessage("❗ Error occurred while deleting service pricing for attorney.");
            }
            props.requestAttorneys('');
        } catch (err) {
            props.addMessage("❗ Error occurred while deleting service pricing for attorney.");
            console.log("Error deleting service pricing for attorney: ", err);
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setSelectedAttorney('');
        setClientId("");
    }

    const clientsOptions = props.attorneysData.find(attorney => attorney.attorneyId === selectedAttorney?.split(',')[0])?.servicePricing.map(pricing => {
        return { label: props.clientsData.find(client => client.clientId === pricing.clientId)?.clientName || 'N/A', value: pricing.clientId }
    });

    const attorneysOptions = props.attorneysData.map(eachAttorney => {
        const attorneyDataArray = [eachAttorney.attorneyId, eachAttorney.firstName, eachAttorney.lastName,]
        return { label: eachAttorney.firstName + " " + eachAttorney.lastName, value: attorneyDataArray }
    });

    const handleChangeClients = (event) => {
        setClientId(event.target.value);
    };

    const handleChangeAttorneys = (event) => {
        setSelectedAttorney(event.target.value);
    };

    useEffect(() => {
        props.requestAttorneys('');
    }, [])

    return (
        <div className="add-form-container">
            <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
                <div className="form-group">
                    <label htmlFor="attorney" className="form-label">Attorney:</label>
                    <select
                        id="attorney"
                        className="form-input"
                        value={selectedAttorney}
                        onChange={handleChangeAttorneys}
                    >
                        <option value="" disabled>Select an attorney</option>
                        {attorneysOptions?.sort((a, b) => {
                            let x = a.label?.toLowerCase();
                            let y = b.label?.toLowerCase();
                            return x < y ? -1 : x > y ? 1 : 0;
                        }).map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="client" className="form-label">Client:</label>
                    <select
                        id="client"
                        className="form-input"
                        value={clientId}
                        onChange={handleChangeClients}
                    >
                        <option value="">Select a client</option>
                        {clientsOptions?.sort((a, b) => {
                            let x = a.label?.toLowerCase();
                            let y = b.label?.toLowerCase();
                            return x < y ? -1 : x > y ? 1 : 0;
                        }).map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-buttons">
                    <button type="submit" className="form-submit-btn">
                        Delete Service Pricing
                    </button>
                    <button type="reset" className="form-clear-btn">
                        Clear
                    </button>
                </div>
            </form>
        </div>
    );
}

DeleteServicePricing.propTypes = {
    attorneysData: PropTypes.array.isRequired,
    clientsData: PropTypes.array.isRequired,
    requestAttorneys: PropTypes.func.isRequired,
}

const mapStateToProps = state => {
    return {
        attorneysData: getAttorneysData(state),
        clientsData: getClientsData(state),
    }
}

const mapDispatchToProps = dispatch => ({
    requestAttorneys: (attorneyID) => {
        dispatch(requestAttorneys(attorneyID))
    },
    requestClients: (clientID) => {
        dispatch(requestClients(clientID))
    },
    addMessage: (message) => {
        dispatch(addMessage(message))
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(DeleteServicePricing)