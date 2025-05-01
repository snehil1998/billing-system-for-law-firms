import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import PropTypes from "prop-types";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {getCasesData} from "../../redux/cases/CasesSelectors";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import {requestDisbursements} from "../../redux/disbursements/DisbursementsActions";
import {addMessage} from "../../redux/message/MessageActions";
import { requestCases } from "../../redux/cases/CasesActions";
import "../common/AddForm.css";

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
        <div className="add-form-container">
            <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
                <div className="form-group">
                    <label htmlFor="caseId" className="form-label">
                        Case:
                    </label>
                    <select
                        id="caseId"
                        className="form-input"
                        value={caseID}
                        onChange={handleChangeCases}
                    >
                        <option value="" disabled>Select a case</option>
                        {caseOptions?.sort((a, b) => {
                            let x = a.label.toLowerCase();
                            let y = b.label.toLowerCase();
                            if (x < y) {return -1;}
                            if (x > y) {return 1;}
                            return 0;
                        }).map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="clientName" className="form-label">
                        Client:
                    </label>
                    <input
                        id="clientName"
                        className="form-input"
                        type="text"
                        value={getClientNameForSelectedCase()}
                        disabled
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="date" className="form-label">
                        Date:
                    </label>
                    <DatePicker
                        value={selectedDate}
                        onChange={setSelectedDate}
                        inputPlaceholder="Select a date"
                        shouldHighlightWeekends
                        className="form-input"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="disbursement" className="form-label">
                        Disbursement:
                    </label>
                    <input
                        id="disbursement"
                        className="form-input"
                        type="text"
                        value={disbursement}
                        onChange={(e) => setDisbursement(e.target.value)}
                        placeholder="Enter disbursement details"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="currencyCode" className="form-label">
                        Currency Code:
                    </label>
                    <input
                        id="currencyCode"
                        className="form-input"
                        type="text"
                        value={currencyCode}
                        disabled
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="conversionRate" className="form-label">
                        Conversion Rate:
                    </label>
                    <input
                        id="conversionRate"
                        className="form-input"
                        type="number"
                        value={conversionRate}
                        disabled
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="inrAmount" className="form-label">
                        INR Amount:
                    </label>
                    <input
                        id="inrAmount"
                        className="form-input"
                        type="number"
                        value={inrAmount}
                        onChange={(e) => setInrAmount(e.target.value)}
                        placeholder="Enter amount in INR"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="conversionAmount" className="form-label">
                        Conversion Amount:
                    </label>
                    <input
                        id="conversionAmount"
                        className="form-input"
                        type="number"
                        value={conversionAmount}
                        disabled
                    />
                </div>
                <div className="form-buttons">
                    <button type="submit" className="form-submit-btn">
                        Add Disbursement
                    </button>
                    <button type="reset" className="form-clear-btn">
                        Clear
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