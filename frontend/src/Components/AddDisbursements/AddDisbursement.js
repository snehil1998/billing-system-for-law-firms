import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import dayjs from 'dayjs';
import PropTypes from "prop-types";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {getCasesData} from "../../redux/cases/CasesSelectors";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import {requestDisbursements} from "../../redux/disbursements/DisbursementsActions";
import {addMessage} from "../../redux/message/MessageActions";
import { requestCases } from "../../redux/cases/CasesActions";
import "../common/AddForm.css";
import { EXCHANGE_RATE_API, EXCHANGE_RATE_API_LATEST } from "../../constants/api";

const AddDisbursement = (props) => {
    const today = dayjs();
    const [caseID, setCaseID] = useState("");
    const [clientID, setClientID] = useState("");
    const [disbursement, setDisbursement] = useState("");
    const [date, setDate] = useState(today.format('YYYY-MM-DD'));
    const [currencyCode, setCurrencyCode] = useState("");
    const [conversionRate, setConversionRate] = useState(0);
    const [inrAmount, setInrAmount] = useState("0");
    const [conversionAmount, setConversionAmount] = useState(0);

    let handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        try {
            if(caseID === '' || disbursement === '' || !date || inrAmount === '0') {
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
                setDate(today.format('YYYY-MM-DD'));
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
        setDate(today.format('YYYY-MM-DD'));
        setCurrencyCode("");
        setConversionRate(0);
        setInrAmount("0");
        setConversionAmount(0);
    }

    const caseOptions = props.casesData?.map(eachCase => {
        return {label: eachCase.caseName, value: eachCase.caseId}
    });

    const handleChangeCases = (event) => {
        setCaseID(event.target.value);
    };

    useEffect(() => {
        async function fetchData() {
            if (currencyCode !== "" && currencyCode !== undefined && date !== "") {
                let endpoint = `${EXCHANGE_RATE_API}&base_currency=${currencyCode}&date=${date}`;
                const dateSplit = date.split('-');
                if (parseInt(dateSplit[0]) === today.year() && parseInt(dateSplit[1]) === today.month()+1 && parseInt(dateSplit[2]) === today.date()) {
                    endpoint = `${EXCHANGE_RATE_API_LATEST}&base_currency=${currencyCode}`;
                }
                await fetch(endpoint)
                    .then(response => response.json())
                    .then(json => {
                        const rate = json['data'][date] ? json['data'][date]['INR'].toFixed(2) : json['data']['INR'].toFixed(2)
                        setConversionRate(rate);
                    }).catch(error => {
                        props.addMessage("❗ Error occurred while fetching data from currency api.");
                        console.log("error fetching data from currency api: " + error);
                    })
            }
        }
        fetchData();
    }, [currencyCode, date, conversionRate, inrAmount, today])

    useEffect(() => {
        if (conversionRate !== undefined && conversionRate !== null) {
            const amount = (parseFloat(inrAmount) / conversionRate);
            setConversionAmount(amount.toFixed(2));
        }
    }, [conversionRate, inrAmount])

    useEffect(() => {
        setClientID(props.casesData.find(data => data.caseId === caseID)?.clientId)
        setCurrencyCode(props.clientsData.find(data => data.clientId === clientID)?.currencyCode);
    }, [caseID, clientID, props.casesData, props.clientsData])

    useEffect(() => {
        props.requestCases('');
    }, [])

    const getClientNameForSelectedCase = () => {
        return caseID === '' ? '' : props.clientsData.find(data => data.clientId === props.casesData
            .find(data => data.caseId === caseID)?.clientId)?.clientName
    }

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
                     <input
                        id="date"
                        className="form-input"
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
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
                        value={disbursement || ""}
                        onChange={(e) => setDisbursement(e.target.value || "")}
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
                        value={currencyCode || ""}
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
                        value={conversionRate !== undefined && conversionRate !== null ? conversionRate : 0}
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
                        value={inrAmount || "0"}
                        onChange={(e) => setInrAmount(e.target.value || "0")}
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
                        value={conversionAmount !== undefined && conversionAmount !== null ? conversionAmount : 0}
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