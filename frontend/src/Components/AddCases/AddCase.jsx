import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import  '../MultiselectDropdown.css'
import {requestCases} from "../../redux/cases/CasesActions";
import PropTypes from "prop-types";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import './AddCase.css';
import {addMessage} from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";

const AddCase = (props) => {
    const [caseID, setCaseID] = useState("");
    const [caseName, setCaseName] = useState("");
    const [clientId, setClientId] = useState("");

    let handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        if(caseID === '' || caseName === '' || clientId === '') {
            return props.addMessage('Please enter all fields to add a case.')
        }
        try {
            let res = await fetch("/backend/cases", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    caseId: caseID,
                    caseName: caseName,
                    clientId: clientId,
                    currencyCode: props.clientsData.find(data => data.clientId === clientId)?.currencyCode,
                    disbursementsAmount: 0,
                    servicesAmount: 0,
                    amount: 0,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setCaseID("");
                setCaseName("");
                setClientId("");
                props.addMessage("Case was created successfully!");
            } else if(res.status === 410) {
                props.addMessage(`Please use a different case ID. ${caseID} already exists.`);
            } else {
                props.addMessage("❗ Error occurred while adding data into cases.");
            }
            props.requestCases('');
        } catch (err) {
            console.log("Error posting data into cases: ", err);
            props.addMessage("❗ Error occurred while adding data into cases.");
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setCaseID("");
        setCaseName("");
        setClientId("");
    }

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    useEffect(() => {
        props.requestClients('');
    }, [])

    return (
        <div id="add-case-container" className={'dropdown-components-container'}>
            <form onSubmit={handleSubmit} onReset={handleClear} id={'add-case-form-container'} className={'dropdown-form-container'}>
                <div id="add-case-case-id-container" className={'dropdown-field-container'}>
                    <div id={'add-case-case-id-translation'} className={'dropdown-translation'}>
                        {'Case ID: '}
                    </div>
                    <input
                        id={'add-case-case-id-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={caseID}
                        onChange={(e) => setCaseID(e.target.value)}
                    />
                </div>
                <div id="add-case-case-name-container" className={'dropdown-field-container'}>
                    <div id={'add-case-case-name-translation'} className={'dropdown-translation'}>
                        {'Case Name: '}
                    </div>
                    <input
                        id={'add-case-case-name-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={caseName}
                        onChange={(e) => setCaseName(e.target.value)}
                    />
                </div>
                <div id="add-case-client-name-container" className={'dropdown-field-container'}>
                    <div id={'add-case-client-name-translation'} className={'dropdown-translation'}>
                        {'Client Name: '}
                    </div>
                    <select key={"client-name-selector"} id={'add-case-client-name-selector'} className={'dropdown-select'}
                            value={clientId} onChange={(event) =>
                        setClientId(event.target.value)}>
                        <option key={"placeholder-client"} value={""}>
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
                </div>
                <div id="add-case-currency-code-container" className={'dropdown-field-container'}>
                    <div id={'add-case-currency-code-translation'} className={'dropdown-translation'}>
                        {'Currency Code: '}
                    </div>
                    <input
                        id={'add-case-currency-code-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={clientId === '' ? '' : props.clientsData.find(data => data.clientId === clientId)?.currencyCode}
                        disabled={true}
                    />
                </div>
                <div id="add-case-add-button-container" className={'dropdown-button-container'}>
                    <button type="submit" id="add-case-add-button" className={'dropdown-button'}>
                        ADD
                    </button>
                    <button type="reset" id="add-case-clear-button" className={'dropdown-button'}>
                        CLEAR
                    </button>
                </div>
            </form>
        </div>
    );
}

AddCase.propTypes = {
    requestCases: PropTypes.func.isRequired,
    clientsData: PropTypes.array.isRequired,
}

const mapStateToProps = state => ({
    clientsData: getClientsData(state),
})

const mapDispatchToProps = dispatch => ({
    requestCases: (caseID) => {
        dispatch(requestCases(caseID))
    },
    requestClients: (clientID) => {
        dispatch(requestClients(clientID))
    },
    addMessage: (message) => {
        dispatch(addMessage(message))
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(AddCase)