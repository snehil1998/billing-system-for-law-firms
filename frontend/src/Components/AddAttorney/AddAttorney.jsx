import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import  '../MultiselectDropdown.css'
import {requestAttorneys} from "../../Redux/Attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import './AddAttorney.css';
import {addMessage} from "../../Redux/Message/MessageActions";
import { requestClients } from "../../Redux/Clients/ClientsActions";

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
        <div id="add-attorney-container" className={'dropdown-components-container'}>
            <form id={'add-attorney-form-container'} className={'dropdown-form-container'} onSubmit={handleSubmit} onReset={handleClear}>
                <div id="add-attorney-attorney-id-container" className={'dropdown-field-container'}>
                    <div id={'add-attorney-attorney-id-translation'} className={'dropdown-translation'}>
                        {'Attorney ID: '}
                    </div>
                    <input
                        id={'add-attorney-attorney-id-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={attorneyID}
                        onChange={(e) => setAttorneyID(e.target.value)}
                    />
                </div>
                <div id="add-attorney-first-name-container" className={'dropdown-field-container'}>
                    <div id={'add-attorney-first-name-translation'} className={'dropdown-translation'}>
                        {'First Name: '}
                    </div>
                    <input
                        id={'add-attorney-first-name-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                    />
                </div>
                <div id="add-attorney-last-name-container" className={'dropdown-field-container'}>
                    <div id={'add-attorney-first-name-translation'} className={'dropdown-translation'}>
                        {'Last Name: '}
                    </div>
                    <input
                        id={'add-attorney-last-name-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                    />
                </div>
                <div id="add-attorney-number-of-service-pricing-container" className={'dropdown-field-container'}>
                    <div id={'add-attorney-number-of-service-pricing-translation'} className={'dropdown-translation'}>
                        {'Number of Service Pricing: '}
                    </div>
                    <select id={'add-attorney-number-of-service-pricing-select'} className={'dropdown-input-field'}
                            value={numberOfServicePricing} onChange={handleNumberOfServicePricing}>
                        <option key={"placeholder-number-of-attorneys"} value={"0"} disabled={true}>
                            0
                        </option>
                        {numberOfServicePricingOptions.map((option) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div id="add-attorney-service-pricing-container" className={'dropdown-field-container'}>
                    {[...Array(parseInt(numberOfServicePricing))].map((_, i) => {
                        return(
                            <div id={"add-attorney-service-pricing-"+i} className={"dropdown-field-container"}>
                                <div id={'add-attorney-service-pricing' + i + '-translation'} className={'dropdown-translation'}>
                                    {'Service Pricing ' + (i+1).toString() + ': '}
                                </div>
                                <select key={"add-attorney-client-name-selector-"+i} className={'dropdown-select'} value={clientIdList[i]} onChange={(event) =>
                                    handleChangeClients(event, i)}>
                                    <option key={"placeholder-client-"+ i} value={""}>
                                        Select a client
                                    </option>
                                    {clientsOptions?.sort(
                                        function(a, b){
                                            let x = a.label.toLowerCase();
                                            let y = b.label.toLowerCase();
                                            if (x < y) {return -1;}
                                            if (x > y) {return 1;}
                                            return 0;
                                        }).map((option) => (
                                        <option key={option.value} value={option.value}>{option.label}</option>
                                    ))}
                                </select>
                                <input
                                    key={"service-pricing-price-input-"+i}
                                    id={'add-attorney-service-pricing-price-input-field'}
                                    className={'dropdown-input-field'}
                                    type="number"
                                    value={priceList[i]}
                                    placeholder="Price"
                                    onChange={(e) =>
                                        setPriceList({...priceList, [i]: e.target.value})}
                                />
                            </div>)
                    })}
                </div>
                <div id={'add-attorney-button-container'} className={'dropdown-button-container'}>
                    <button type="submit" id="add-attorney-add-button" className={'dropdown-button'}>
                        ADD
                    </button>
                    <button type="reset" id="add-attorney-clear-button" className={'dropdown-button'}>
                        CLEAR
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