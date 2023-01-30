import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import {requestServices} from "../Redux/Services/ServicesActions";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import PropTypes from "prop-types";
import {getClientsData} from "../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../Redux/Cases/CasesSelectors";
import {getAttorneysData} from "../Redux/Attorneys/AttorneysSelectors";
import './AddService.css';
import {addMessage, clearMessage} from "../Redux/Message/MessageActions";

const AddService = (props) => {
    const [caseID, setCaseID] = useState("");
    const [clientID, setClientID] = useState("");
    const [service, setService] = useState("");
    const [date, setDate] = useState("");
    const [selectedDate, setSelectedDate] = useState(null);
    const [minutes, setMinutes] = useState({});
    const [selectedAttorneys, setSelectedAttorneys] = useState({});
    const [showAddService, setShowAddService] = useState(false);
    const [numberOfAttorneys, setNumberOfAttorneys] = useState("0");
    const [currencyCode, setCurrencyCode] = useState("");
    const [shouldPost, setShouldPost] = useState(true);

    let handleSubmit = async (e) => {
        e.preventDefault();
        if(shouldPost){
            try {
                if(selectedAttorneys === {} || caseID === '' || service === '' || date === 'undefined-undefined-undefined') {
                    return props.addMessage('❗ Please complete all fields to add a service')
                }
                let attorneysList = []
                Object.keys(selectedAttorneys).forEach( index => {
                    attorneysList.push({id: selectedAttorneys[index], minutes: parseInt(minutes[index])})
                })
                let res = await fetch("/services", {
                    method: "POST",
                    headers: {
                        'Accept': 'application/json, text/plain, */*',
                        'Content-Type': 'application/json; charset=utf-8'
                    },
                    body: JSON.stringify({
                        caseId: caseID,
                        clientId: clientID,
                        service: service,
                        date: date,
                        attorneys: attorneysList,
                    }),
                });
                if (res.status === 200 || res.status === 201) {
                    setCaseID("");
                    setClientID("");
                    setService("");
                    setDate("");
                    setSelectedDate(null);
                    setMinutes({});
                    setSelectedAttorneys({});
                    setNumberOfAttorneys("0");
                    props.addMessage("Service was created successfully!");
                } else {
                    props.addMessage("❗ Error occurred while adding data into services");
                }
                props.requestServices('');
            } catch (err) {
                console.log("Error posting data into services: ", err);
            }
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setCaseID("");
        setClientID("");
        setService("");
        setDate("");
        setSelectedDate(null);
        setMinutes({});
        setSelectedAttorneys({});
        setNumberOfAttorneys("0");
    }

    useEffect(() => {
        setDate(selectedDate?.year+'-'+
            selectedDate?.month.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false})+'-'
            +selectedDate?.day.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false}))
    }, [selectedDate])

    useEffect(() => {
        setClientID(props.casesData.find(data => data.caseId === caseID)?.clientId)
        setCurrencyCode(clientID === '' ? '' : props.clientsData.find(data => data.clientId === clientID)?.currencyCode);
    }, [caseID, clientID, props.casesData, props.clientsData])

    const caseOptions = props.casesData?.map(eachCase => {
            return { label: eachCase.caseName, value: eachCase.caseId }
        });

    const filteredAttorneysData = props.attorneysData
        .filter(attorney => attorney.servicePricing
        .find(servicePrice => servicePrice.clientId === clientID) !== undefined);


    let numberOfAttorneysList = [];
    for(let num=1; num<=filteredAttorneysData?.length; num++) {
        numberOfAttorneysList.push(num);
    }

    const numberOfAttorneysOptions =
        numberOfAttorneysList.map(eachNumber => {
            return { label: eachNumber, value: eachNumber }
        });

    const  attorneysOptions  = filteredAttorneysData?.map(eachAttorney => {
        return { label:  eachAttorney.firstName + " " + eachAttorney.lastName, value:  eachAttorney.attorneyId,
            style: {fontSize: '85%', color:'black'}}
    });

    const handleChangeCases = (event) => {
        setCaseID(event.target.value);
    };

    const  handleChangeAttorneys  =  (event, i)  => {
        setSelectedAttorneys({...selectedAttorneys, [i]: event.target.value})
    }

    const handleAddService = () => {
        if (showAddService) {
            setShowAddService(false)
        } else{
            setShowAddService(true)
        }
    }

    const handleNumberOfAttorneys = (event) => {
        setNumberOfAttorneys(event.target.value);
    };
    
    const validateMinutes = () => {
        let checkValidation = true;
        Object.keys(minutes).forEach(index => {
            if(parseInt(minutes[index]) % 6 !== 0) {
                checkValidation = false;
            }
        })
        if(!checkValidation){
            setShouldPost(false)
            props.addMessage("❗ Please make sure you enter valid minutes for attorneys. They should be a multiple of 6.");
        } else {
            setShouldPost(true)
            props.clearMessage();
        }
    }

    const faCaretSquare = () => {
        return showAddService ? <FontAwesomeIcon icon={faCaretSquareUp} /> : <FontAwesomeIcon icon={faCaretSquareDown} />
    }

    const getClientNameForSelectedCase = () => {
        return caseID === '' ? '' : props.clientsData.find(data => data.clientId === props.casesData
            .find(data => data.caseId === caseID)?.clientId)?.clientName
    }

    return (
        <div id="add-service-container" className={'dropdown-components-container'}>
            <div id="add-service-span-container" className={'dropdown-span-container'}>
                <span id={'add-service-span'} className={'dropdown-container-span'} onClick={handleAddService}>
                    ADD A SERVICE   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form id={'add-service-form-container'} className={'dropdown-form-container'} onSubmit={handleSubmit} onReset={handleClear}>
                    <div id="add-service-case-name-container" className={'dropdown-field-container'}>
                        <div id={'add-service-case-name-translation'} className={'dropdown-translation'}>
                            {'Case: '}
                        </div>
                        <select id={'add-service-case-name-select'} className={'dropdown-select'} value={caseID} onChange={handleChangeCases}>
                            <option key={"placeholder-case"} value={""} disabled={true}>
                                Select a case
                            </option>
                            {caseOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div id="add-service-client-name-container" className={'dropdown-field-container'}>
                        <div id={'add-service-client-name-translation'} className={'dropdown-translation'}>
                            {'Client: '}
                        </div>
                        <input
                            id={'add-service-client-name-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={getClientNameForSelectedCase()}
                            disabled={true}
                        />
                    </div>
                    <div id="add-service-service-container" className={'dropdown-field-container'}>
                        <div id={'add-service-service-translation'} className={'dropdown-translation'}>
                            {'Service: '}
                        </div>
                        <input
                            id={'add-service-service-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={service}
                            onChange={(e) => setService(e.target.value)}
                        />
                    </div>
                    <div id="add-service-date-container" className={'dropdown-field-container'}>
                        <div id={'add-service-date-translation'} className={'dropdown-translation'}>
                            {'Date: '}
                        </div>
                        <DatePicker
                            value={selectedDate}
                            onChange={setSelectedDate}
                            inputPlaceholder="Select a date"
                            shouldHighlightWeekends
                            className={'DatePicker__input'}
                        />
                    </div>
                    <div id="add-service-currency-code-container" className={'dropdown-field-container'}>
                        <div id={'add-service-currency-code-translation'} className={'dropdown-translation'}>
                            {'Currency code: '}
                        </div>
                        <input
                            id={'add-service-currency-code-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={currencyCode}
                            disabled={true}
                        />
                    </div>
                    <div id="add-service-number-of-attorneys-container" className={'dropdown-field-container'}>
                        <div id={'add-service-number-of-attorneys-translation'} className={'dropdown-translation'}>
                            {'Number of attorneys: '}
                        </div>
                        <select id={'add-service-number-of-attorneys-select'} className={'dropdown-select'} value={numberOfAttorneys} onChange={handleNumberOfAttorneys}>
                            {clientID === "" ?
                                <option key={"placeholder-number-of-attorneys"} value={"0"} disabled={true}>
                                    Select a client first
                                </option> :
                                <option key={"placeholder-number-of-attorneys"} value={"0"} disabled={true}>
                                Select number of attorneys
                                </option>
                            }
                            {numberOfAttorneysOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div id="add-service-attorneys-container" className={'dropdown-field-container'}>
                        {[...Array(parseInt(numberOfAttorneys))].map((e, i) => {
                            return(
                            <div className={"attorney-"+i}>
                                <div id={'attorney' + i + '-translation'} className={'dropdown-translation'}>
                                    {'Attorney ' + (i+1).toString() + ': '}
                                </div>
                                <select id={'add-service-attorney-name-selector'} className={'dropdown-select'}
                                        key={"attorney-name-selector-"+i} value={selectedAttorneys[i]} onChange={(event) =>
                                    handleChangeAttorneys(event, i)}>
                                    <option key={"placeholder-attorneys-"+ i} value={""}>
                                        Select attorney
                                    </option>
                                    {attorneysOptions.map((option) => (
                                        <option key={option.value} value={option.value}>{option.label}</option>
                                    ))}
                                </select>
                                <input
                                    key={"attorney-minutes-input-"+i}
                                    id={'add-service-attorney-minutes-input'}
                                    className={'dropdown-input-field'}
                                    type="number"
                                    value={minutes[i]}
                                    placeholder="Minutes"
                                    onChange={(e) =>
                                        setMinutes({...minutes, [i]: e.target.value})}
                                    onBlur={validateMinutes}
                                />
                            </div>)
                        })}
                    </div>
                <div id="add-service-button-container" className={'dropdown-button-container'}>
                    <button type="submit" id={'add-service-add-button'} className={'dropdown-button'}>
                        ADD
                    </button>
                    <button type="reset" id={'add-service-clear-button'} className={'dropdown-button'}>
                        CLEAR
                    </button>
                </div>
            </form>}
        </div>
    );
}

AddService.propTypes = {
    clientsData: PropTypes.array.isRequired,
    casesData: PropTypes.array.isRequired,
    attorneysData: PropTypes.array.isRequired,
    requestServices: PropTypes.func.isRequired,
}

const mapStateToProps = state => {
    return {
        clientsData: getClientsData(state),
        casesData: getCasesData(state),
        attorneysData: getAttorneysData(state),
    }
}

const mapDispatchToProps = dispatch => ({
    requestServices: (clientID) => {
        dispatch(requestServices(clientID));
    },
    addMessage: (message) => {
        dispatch(addMessage(message));
    },
    clearMessage: () => {
        dispatch(clearMessage());
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(AddService)