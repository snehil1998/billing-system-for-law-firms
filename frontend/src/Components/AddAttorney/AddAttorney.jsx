import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import "../common/AddForm.css";
import {requestAttorneys} from "../../redux/attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {addMessage} from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";

const AddAttorney = (props) => {
    const [attorneyID, setAttorneyID] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [clientIdList, setClientIdList] = useState({});
    const [priceList, setPriceList] = useState({});
    const [numberOfServicePricing, setNumberOfServicePricing] = useState("0");

    const handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        try {
            let servicePricingList = []
            Object.keys(clientIdList).forEach( index => {
                servicePricingList.push({clientId: clientIdList[index], price: parseInt(priceList[index])})
            })
            if(attorneyID === "" || firstName === "" || lastName === "" || servicePricingList.length === 0) {
                return props.addMessage('Please complete all fields to add an attorney.')
            }
            let res = await fetch("/backend/attorneys", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    attorneyId: attorneyID,
                    firstName: firstName,
                    lastName: lastName,
                    servicePricing: servicePricingList,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setAttorneyID("");
                setFirstName("");
                setLastName("");
                setNumberOfServicePricing("0");
                setClientIdList({});
                setPriceList({});
                props.addMessage('Attorney was created successfully!');
            } else if(res.status === 410) {
                props.addMessage(`Please use a different attorney ID. ${attorneyID} already exists.`);
            } else {
                props.addMessage("❗ Error occurred while adding data into attorneys.");
            }
            props.requestAttorneys('');
        } catch (err) {
            props.addMessage("❗ Error occurred while adding data into attorneys.");
            console.log("Error posting data into attorneys: ", err);
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setAttorneyID("");
        setFirstName("");
        setLastName("");
        setNumberOfServicePricing("0");
        setClientIdList({});
        setPriceList({});
    }

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    let numberOfServicePricingList = [];
    for(let num=1; num<=props.clientsData.length; num++) {
        numberOfServicePricingList.push(num);
    }

    const numberOfServicePricingOptions =
        numberOfServicePricingList.map(eachNumber => {
            return { label: eachNumber, value: eachNumber }
        });

    const handleChangeClients = (event, i) => {
        setClientIdList({...clientIdList, [i]: event.target.value});
    };

    const handleNumberOfServicePricing = (event) => {
        setNumberOfServicePricing(event.target.value);
    };

    useEffect(() => {
        props.requestClients('')
    }, [])

    return (
        <div className="add-form-container">
            <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
                <div className="form-group">
                    <label htmlFor="attorneyId" className="form-label">
                        Attorney ID:
                    </label>
                    <input
                        id="attorneyId"
                        className="form-input"
                        type="text"
                        value={attorneyID}
                        onChange={(e) => setAttorneyID(e.target.value)}
                        placeholder="Enter attorney ID"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="firstName" className="form-label">
                        First Name:
                    </label>
                    <input
                        id="firstName"
                        className="form-input"
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        placeholder="Enter first name"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName" className="form-label">
                        Last Name:
                    </label>
                    <input
                        id="lastName"
                        className="form-input"
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        placeholder="Enter last name"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="numberOfServicePricing" className="form-label">
                        Number of Clients:
                    </label>
                    <select
                        id="numberOfServicePricing"
                        className="form-input"
                        value={numberOfServicePricing}
                        onChange={handleNumberOfServicePricing}
                    >
                        <option value="0" disabled>
                            0
                        </option>
                        {numberOfServicePricingOptions.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="service-pricing-list">
                    {[...Array(parseInt(numberOfServicePricing))].map((_, i) => (
                        <div key={i} className="form-group service-pricing-item">
                            <label className="form-label">Service Pricing {i + 1}:</label>
                            <div className="service-pricing-inputs">
                                <select
                                    className="form-input"
                                    value={clientIdList[i] || ""}
                                    onChange={(event) => handleChangeClients(event, i)}
                                >
                                    <option value="">Select a client</option>
                                    {clientsOptions
                                        ?.sort((a, b) => {
                                            let x = a.label.toLowerCase();
                                            let y = b.label.toLowerCase();
                                            return x < y ? -1 : x > y ? 1 : 0;
                                        })
                                        .map((option) => (
                                            <option key={option.value} value={option.value}>
                                                {option.label}
                                            </option>
                                        ))}
                                </select>
                                <input
                                    className="form-input"
                                    type="number"
                                    value={priceList[i] || ""}
                                    placeholder="Price"
                                    onChange={(e) =>
                                        setPriceList({...priceList, [i]: e.target.value})}
                                />
                            </div>
                        </div>
                    ))}
                </div>
                <div className="form-buttons">
                    <button type="submit" className="form-submit-btn">
                        Add Attorney
                    </button>
                    <button type="reset" className="form-clear-btn">
                        Clear
                    </button>
                </div>
            </form>
        </div>
    );
}

AddAttorney.propTypes = {
    clientsData: PropTypes.array.isRequired,
    requestAttorneys: PropTypes.func.isRequired,
}

const mapStateToProps = state => ({
    clientsData: getClientsData(state),
})

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

export default connect(mapStateToProps, mapDispatchToProps)(AddAttorney)