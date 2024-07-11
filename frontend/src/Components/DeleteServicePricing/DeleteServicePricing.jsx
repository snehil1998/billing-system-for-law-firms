import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import  '../MultiselectDropdown.css'
import {requestAttorneys} from "../../Redux/Attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getAttorneysData} from "../../Redux/Attorneys/AttorneysSelectors";
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import './DeleteServicePricing.css';
import {addMessage} from "../../Redux/Message/MessageActions";
import { requestClients } from "../../Redux/Clients/ClientsActions";

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
        <div id="delete-service-pricing-container" className={'dropdown-components-container'}>
            <form id={'delete-service-pricing-form-container'} className={'dropdown-form-container'} onSubmit={handleSubmit} onReset={handleClear}>
                <div id="delete-service-pricing-attorney-name-container" className={'dropdown-field-container'}>
                    <div id={'delete-service-pricing-first-name-translation'} className={'dropdown-translation'}>
                        {'Attorney: '}
                    </div>
                    <select id={'delete-service-pricing-first-name-select'} className={'dropdown-select'} value={selectedAttorney} onChange={handleChangeAttorneys}>
                        <option key={"placeholder-selected-attorney"} value={''} disabled={true}>
                            Select an attorney
                        </option>
                        {attorneysOptions?.sort(
                            function(a, b){
                                let x = a.label?.toLowerCase();
                                let y = b.label?.toLowerCase();
                                if (x < y) {return -1;}
                                if (x > y) {return 1;}
                                return 0;
                            }).map((option, i) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div id="delete-service-pricing-service-pricing-container" className={'dropdown-field-container'}>
                    <div>
                        <div id={'delete-service-pricing-service-pricing-translation'} className={'dropdown-translation'}>
                            {'Client: '}
                        </div>
                        <select id={'delete-service-pricing-service-pricing-select'} className={'dropdown-select'}
                                key={"delete-service-pricing-service-pricing-selector"} value={clientId} onChange={handleChangeClients}>
                            <option key={"placeholder-client"} value={""}>
                                Select a client
                            </option>
                            {clientsOptions?.sort(
                                function(a, b){
                                    let x = a.label?.toLowerCase();
                                    let y = b.label?.toLowerCase();
                                    if (x < y) {return -1;}
                                    if (x > y) {return 1;}
                                    return 0;
                                }).map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                </div>
                <div id={'delete-service-pricing-add-button-container'} className={'dropdown-button-container'}>
                    <button type="submit" id="delete-service-pricing-add-button" className={'dropdown-button'}>
                        DELETE
                    </button>
                    <button type="reset" id="delete-service-pricing-clear-button" className={'dropdown-button'}>
                        CLEAR
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