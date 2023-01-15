import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import PropTypes from "prop-types";
import {getClientsData} from "../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../Redux/Cases/CasesSelectors";
import {getAttorneysData} from "../Redux/Attorneys/AttorneysSelectors";
import {requestDisbursements} from "../Redux/Disbursements/DisbursementsActions";

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
    const [message, setMessage] = useState("");
    const [showAddDisbursement, setShowAddDisbursement] = useState(false);
    const [shouldPost, setShouldPost] = useState(true);

    let handleSubmit = async (e) => {
        e.preventDefault();
        if (shouldPost) {
            try {
                let res = await fetch("/disbursements", {
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
                    setMessage("Disbursement was created successfully!");
                } else {
                    setMessage("❗ Error occurred while adding data into disbursements");
                }
                props.requestDisbursements('');
            } catch (err) {
                console.log("Error posting data into services: ", err);
            }
        }
    };

    useEffect(() => {
        setDate(selectedDate?.year + '-' +
            selectedDate?.month.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}) + '-'
            + selectedDate?.day.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping: false}))
    }, [selectedDate])

    const caseOptions = props.casesData?.map(eachCase => {
        return {label: eachCase.caseName, value: eachCase.caseId}
    });

    const clientsOptions = props.clientsData?.map(eachClient => {
        return {label: eachClient.clientName, value: eachClient.clientId}
    });


    const handleChangeCases = (event) => {
        setCaseID(event.target.value);
    };

    const handleChangeClients = (event) => {
        setClientID(event.target.value);
    };

    const handleAddDisbursement = () => {
        if (showAddDisbursement) {
            setShowAddDisbursement(false)
        } else {
            setShowAddDisbursement(true)
        }
    }

    const faCaretSquare = () => {
        return showAddDisbursement ? <FontAwesomeIcon icon={faCaretSquareUp}/> :
            <FontAwesomeIcon icon={faCaretSquareDown}/>
    }

    useEffect(() => {
        setCurrencyCode(props.clientsData.find(data => data.clientId === clientID)?.currencyCode);
        async function fetchData() {
            const endpoint = "https://api.exchangerate.host/";
            if (currencyCode !== "" && currencyCode !== undefined && date !== "") {
                await fetch(`${endpoint}${date}?base=${currencyCode}`)
                    .then(response => response.json())
                    .then(json => {
                        setConversionRate(json['rates']['INR'].toFixed(2));
                        const amount = (parseFloat(inrAmount) / conversionRate);
                        setConversionAmount(amount.toFixed(2));
                    }).catch(error => console.log("error fetching data from currency api: " + error))
            }
        }
        fetchData();
    }, [currencyCode, clientID, props.clientsData, date, conversionRate, inrAmount])

    return (
        <div className="add-disbursement">
            <p style={{
                fontSize: '20px', textAlign: 'center', width: '100vw',
                backgroundColor: 'white', color: 'red'
            }}>{message}</p>
            <div className="add-disbursement-span-container"
                 style={{backgroundColor: 'black', width: '18vw', marginLeft: '1vw'}}>
                <span onClick={handleAddDisbursement} style={{cursor: 'pointer', fontSize: '20px', marginLeft: '1vw'}}>
                    ADD A DISBURSEMENT {faCaretSquare()}
                </span>
            </div>
            {showAddDisbursement && <form onSubmit={handleSubmit} style={{
                fontSize: '15px', marginLeft: '1vw',
                backgroundColor: 'grey', height: '75vh', width: '98vw'
            }}>
                <div className="case-name-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'case-name-translation'} style={{fontSize: '17px'}}>
                        {'Case: '}
                    </div>
                    <select value={caseID} onChange={handleChangeCases} style={{width: '57.5vw', height: '4vh'}}>
                        <option key={"placeholder-case"} value={""} disabled={true}>
                            Select a case
                        </option>
                        {caseOptions.map((option) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div className="client-name-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'client-name-translation'} style={{fontSize: '17px'}}>
                        {'Client: '}
                    </div>
                    <select value={clientID} onChange={handleChangeClients} style={{width: '39vw', height: '4vh'}}>
                        <option key={"placeholder-client"} value={""} disabled={true}>
                            Select a client
                        </option>
                        {clientsOptions.map((option) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div className="disbursement-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'disbursement-translation'} style={{fontSize: '17px'}}>
                        {'Disbursement: '}
                    </div>
                    <input
                        type="text"
                        value={disbursement}
                        placeholder="Disbursement"
                        onChange={(e) => setDisbursement(e.target.value)}
                        style={{width: '35vw', height: '4vh'}}
                    />
                </div>
                <div className="date-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'date-translation'} style={{fontSize: '17px'}}>
                        {'Date: '}
                    </div>
                    <DatePicker
                        value={selectedDate}
                        onChange={setSelectedDate}
                        inputPlaceholder="Select a date"
                        shouldHighlightWeekends
                        wrapperClassName={'DatePicker__input'}
                    />
                </div>
                <div className="currency-code-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'currency-code-translation'} style={{fontSize: '17px'}}>
                        {'Currency Code: '}
                    </div>
                    <input
                        type="text"
                        value={currencyCode}
                        placeholder={"Currency code"}
                        onChange={(e) => setCurrencyCode(e.target.value)}
                        disabled={true}
                        style={{width: '35vw', height: '4vh'}}
                    />
                </div>
                <div className="conversion-rate-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'conversion-rate-translation'} style={{fontSize: '17px'}}>
                        {'Conversion Rate: '}
                    </div>
                    <input
                        type="number"
                        value={conversionRate}
                        placeholder={"Conversion rate"}
                        style={{width: '35vw', height: '4vh'}}
                        disabled={true}
                    />
                </div>
                <div className="inr-amount-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'inr-amount-translation'} style={{fontSize: '17px'}}>
                        {'INR Amount: '}
                    </div>
                    <input
                        type="number"
                        value={inrAmount}
                        placeholder={"INR amount"}
                        onChange={(e) => setInrAmount(e.target.value)}
                        style={{width: '35vw', height: '4vh'}}
                    />
                </div>
                <div className="conversion-amount-container" style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                    <div className={'conversion-amount-translation'} style={{fontSize: '17px'}}>
                        {'Conversion Amount: '}
                    </div>
                    <input
                        type="number"
                        value={conversionAmount}
                        placeholder={"Conversion amount"}
                        style={{width: '35vw', height: '4vh'}}
                        disabled={true}
                    />
                </div>
                <button type="submit" className="add-button-container"
                        style={{width: '5vw', height: '4vh', fontSize: '15px', marginLeft: '1vw', marginTop: '2vh'}}>Add
                </button>
            </form>}
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
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(AddDisbursement)