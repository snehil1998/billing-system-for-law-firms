import React, { useEffect, useState} from "react"
import {connect} from "react-redux";
import {requestAttorneys} from "../../redux/attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {addMessage} from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";
import "../common/AddForm.css";

const AddServicePricing = (props) => {
    const [selectedAttorney, setSelectedAttorney] = useState("");
    const [clientId, setClientId] = useState("");
    const [price, setPrice] = useState("");

    let handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        try {
            if(clientId === "" || price === "" || selectedAttorney === "") {
                return props.addMessage('Please complete all the fields to add service pricing.');
            }
            const servicePricingList = [{clientId: clientId, price: parseInt(price)}]
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
                setSelectedAttorney("");
                setClientId("");
                setPrice("");
                props.addMessage("Service pricing was added successfully!");
            } else if (res.status === 410) {
                props.addMessage("Client already exists in service pricing for the attorney.");
            } else {
                props.addMessage("❗ Error occurred while adding service pricing for attorney.");
            }
            props.requestAttorneys('');
        } catch (err) {
            console.log("Error adding service pricing for attorney: ", err);
            props.addMessage("❗ Error occurred while adding service pricing for attorney.");
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setSelectedAttorney("");
        setClientId("");
        setPrice("");
    }

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
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
        props.requestClients('');
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
                            let x = a.label.toLowerCase();
                            let y = b.label.toLowerCase();
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
                            let x = a.label.toLowerCase();
                            let y = b.label.toLowerCase();
                            return x < y ? -1 : x > y ? 1 : 0;
                        }).map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="price" className="form-label">Price:</label>
                    <input
                        id="price"
                        className="form-input"
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        placeholder="Enter price"
                    />
                </div>
                <div className="form-buttons">
                    <button type="submit" className="form-submit-btn">
                        Add Service Pricing
                    </button>
                    <button type="reset" className="form-clear-btn">
                        Clear
                    </button>
                </div>
            </form>
        </div>
    );
}

AddServicePricing.propTypes = {
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

export default connect(mapStateToProps, mapDispatchToProps)(AddServicePricing)