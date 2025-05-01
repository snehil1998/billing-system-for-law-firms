import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import  '../MultiselectDropdown.css'
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import PropTypes from "prop-types";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {getCasesData} from "../../redux/cases/CasesSelectors";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import {requestDisbursements} from "../../redux/disbursements/DisbursementsActions";
import './AddDisbursement.css';
import {addMessage} from "../../redux/message/MessageActions";
import { requestCases } from "../../redux/cases/CasesActions";

const AddDisbursement = (props) => {
    const [caseID, setCaseID] = useState("");
    const [clientID, setClientID] = useState("");
    const [disbursement, setDisbursement] = useState("");
    const [date, setDate] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [conversionRate, setConversionRate] = useState(0);
    const [inrAmount, setInrAmount] = useState("0");
    const [conversionAmount, setConversionAmount] = useState(0);
    const [selectedDate, setSelectedDate] = useState(null);

    let handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        try {
            if(caseID === '' || disbursement === '' || date === 'undefined-undefined-undefined' || inrAmount === '0') {
                return props.addMessage('❗ Please complete all fields to add a disbursement.')
            }
            let res = await fetch("/backend/disbursements", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    caseId: caseID,
                    clientId: clientID,
                    disbursement: disbursement,
                    date: date,
                    currencyCode: currencyCode,
                    conversionRate: conversionRate,
                    inrAmount: parseFloat(inrAmount),
                    conversionAmount: conversionAmount,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setCaseID("");
                setClientID("");
                setDisbursement("");
                setDate("");
                setSelectedDate(null);
                setCurrencyCode("");
                setConversionRate(0);
                setInrAmount("0");
                setConversionAmount(0);
                props.addMessage("Disbursement was created successfully!");
            } else {
                props.addMessage("❗ Error occurred while adding data into disbursements.");
            }
            props.requestDisbursements('');
        } catch (err) {
            props.addMessage("❗ Error occurred while adding data into disbursements.");
            console.log("Error posting data into services: ", err);
        }
    };

    const handleClear = async (e) => {
        e.preventDefault();
        setCaseID("");
        setClientID("");
        setDisbursement("");
        setDate("");
        setSelectedDate(null);
        setCurrencyCode("");
        setConversionRate(0);
        setInrAmount("0");
        setConversionAmount(0);
    }

    useEffect(() => {
        setDate(selectedDate?.year + '-' +
            selectedDate?.month.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}) + '-'
            + selectedDate?.day.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}))
    }, [selectedDate])

    const caseOptions = props.casesData?.map(eachCase => {
        return {label: eachCase.caseName, value: eachCase.caseId}
    });

    const handleChangeCases = (event) => {
        setCaseID(event.target.value);
    };

    useEffect(() => {
        async function fetchData() {
            const endpoint = "https://api.exchangerate.host/";
            if (currencyCode !== "" && currencyCode !== undefined && date !== "") {
                await fetch(`${endpoint}${date}?base=${currencyCode}`)
                    .then(response => response.json())
                    .then(json => {
                        setConversionRate(json['rates']['INR'].toFixed(2));
                        const amount = (parseFloat(inrAmount) / conversionRate);
                        setConversionAmount(amount.toFixed(2));
                    }).catch(error => {
                        props.addMessage("❗ Error occurred while fetching data from currency api.");
                        console.log("error fetching data from currency api: " + error);
                    })
            }
        }
        fetchData();
    }, [currencyCode, date, conversionRate, inrAmount])

    useEffect(() => {
        setClientID(props.casesData.find(data => data.caseId === caseID)?.clientId)
        setCurrencyCode(props.clientsData.find(data => data.clientId === clientID)?.currencyCode);
    }, [caseID, clientID, props.casesData, props.clientsData])

    const getClientNameForSelectedCase = () => {
        return caseID === '' ? '' : props.clientsData.find(data => data.clientId === props.casesData
            .find(data => data.caseId === caseID)?.clientId)?.clientName
    }

    useEffect(() => {
        props.requestCases('');
    }, [])

    return (
        <div id="add-disbursement" className={'dropdown-components-container'}>
            <form id={'add-disbursement-form-container'} className={'dropdown-form-container'} onSubmit={handleSubmit} onReset={handleClear}>
                <div id="add-disbursement-case-name-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-case-name-translation'} className={'dropdown-translation'}>
                        {'Case: '}
                    </div>
                    <select id={'add-disbursement-select'} className={'dropdown-select'} value={caseID} onChange={handleChangeCases}>
                        <option key={"placeholder-case"} value={""} disabled={true}>
                            Select a case
                        </option>
                        {caseOptions?.sort(
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
                <div id="add-disbursement-client-name-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-client-name-translation'} className={'dropdown-translation'}>
                        {'Client: '}
                    </div>
                    <input
                        id={'add-disbursement-client-name-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={getClientNameForSelectedCase()}
                        disabled={true}
                    />
                </div>
                <div id="add-disbursement-date-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-date-translation'} className={'dropdown-translation'}>
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
                <div id="add-disbursement-disbursement-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-disbursement-translation'} className={'dropdown-translation'}>
                        {'Disbursement: '}
                    </div>
                    <input
                        id={'add-disbursement-disbursement-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={disbursement}
                        onChange={(e) => setDisbursement(e.target.value)}
                    />
                </div>
                <div id="add-disbursement-currency-code-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-currency-code-translation'} className={'dropdown-translation'}>
                        {'Currency Code: '}
                    </div>
                    <input
                        id={'add-disbursement-currency-code-input-field'}
                        className={'dropdown-input-field'}
                        type="text"
                        value={currencyCode}
                        onChange={(e) => setCurrencyCode(e.target.value)}
                        disabled={true}
                    />
                </div>
                <div id="add-disbursement-conversion-rate-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-conversion-rate-translation'} className={'dropdown-translation'}>
                        {'Conversion Rate: '}
                    </div>
                    <input
                        id={'add-disbursement-conversion-rate-input-field'}
                        className={'dropdown-input-field'}
                        type="number"
                        value={conversionRate}
                        disabled={true}
                    />
                </div>
                <div id="add-disbursement-inr-amount-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-inr-amount-translation'} className={'dropdown-translation'}>
                        {'INR Amount: '}
                    </div>
                    <input
                        id={'add-disbursement-inr-amount-input-field'}
                        className={'dropdown-input-field'}
                        type="number"
                        value={inrAmount}
                        onChange={(e) => setInrAmount(e.target.value)}
                    />
                </div>
                <div id="add-disbursement-conversion-amount-container" className={'dropdown-field-container'}>
                    <div id={'add-disbursement-conversion-amount-translation'} className={'dropdown-translation'}>
                        {'Conversion Amount: '}
                    </div>
                    <input
                        id={'add-disbursement-conversion-amount-input-field'}
                        className={'dropdown-input-field'}
                        type="number"
                        value={conversionAmount}
                        disabled={true}
                    />
                </div>
                <div id={'add-disbursement-button-container'} className={'dropdown-button-container'}>
                    <button type="submit" id="add-disbursement-add-button" className={'dropdown-button'}>
                        ADD
                    </button>
                    <button type="reset" id="add-disbursement-clear-button" className={'dropdown-button'}>
                        CLEAR
                    </button>
                </div>
            </form>
        </div>
    );
}

AddDisbursement.propTypes = {
    clientsData: PropTypes.array.isRequired,
    casesData: PropTypes.array.isRequired,
    attorneysData: PropTypes.array.isRequired,
    requestDisbursements: PropTypes.func.isRequired,
}

const mapStateToProps = state => {
    return {
        clientsData: getClientsData(state),
        casesData: getCasesData(state),
        attorneysData: getAttorneysData(state),
    }
}

const mapDispatchToProps = dispatch => ({
    requestDisbursements: (clientID) => {
        dispatch(requestDisbursements(clientID));
    },
    requestCases: (caseID) => {
        dispatch(requestCases(caseID));
    },
    addMessage: (message) => {
        dispatch(addMessage(message));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(AddDisbursement)