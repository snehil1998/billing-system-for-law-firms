import React, {useEffect, useState} from "react"
import {connect} from "react-redux";
import {requestServices} from "../../redux/services/ServicesActions";
import {requestCases} from "../../redux/cases/CasesActions";
import {getCasesData} from "../../redux/cases/CasesSelectors";
import {servicesApi} from "../../services/api";
import "../common/AddForm.css";
import {addMessage} from "../../redux/message/MessageActions";
import {clearMessage} from "../../redux/message/MessageActions";
import {requestAttorneys} from "../../redux/attorneys/AttorneysActions";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {getAttorneysData} from "../../redux/attorneys/AttorneysSelectors";
import './AddService.css';
import DynamicInputList from "../common/DynamicInputList";

const AddService = (props) => {
    const [serviceID, setServiceID] = useState("");
    const [serviceName, setServiceName] = useState("");
    const [caseID, setCaseID] = useState("");
    const [description, setDescription] = useState("");
    const [date, setDate] = useState("");
    const [clientID, setClientID] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [numberOfAttorneys, setNumberOfAttorneys] = useState(0);
    const [attorneySelections, setAttorneySelections] = useState([]);

    useEffect(() => {
        props.requestCases("");
        props.requestAttorneys('');
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        if (!serviceID || !serviceName || !caseID || !description || !date) {
            return props.addMessage("Please complete all fields to add a service.");
        }
        if (!validateMinutes()) {
            return props.addMessage("❗ Please make sure you enter valid minutes for attorneys. They should be a multiple of 6.");
        }
        if (attorneySelections.some(sel => !sel.attorneyId)) {
            return props.addMessage("Please select an attorney for each row.");
        }
        try {
            await servicesApi.create({
                serviceId: serviceID,
                caseId: caseID,
                clientId: clientID,
                service: serviceName,
                date: date,
                attorneys: attorneySelections.map(sel => ({ id: sel.attorneyId, minutes: parseInt(sel.minutes) })),
            });
            setServiceID("");
            setServiceName("");
            setCaseID("");
            setDescription("");
            setDate("");
            setNumberOfAttorneys(0);
            setAttorneySelections([]);
            setClientID("");
            setCurrencyCode("");
            props.addMessage("Service was created successfully!");
            props.requestServices("");
        } catch (error) {
            if (error.status === 410) {
                props.addMessage(`Please use a different service ID. ${serviceID} already exists.`);
            } else {
                props.addMessage("❗ Error occurred while adding data into services.");
            }
        }
    };

    const handleClear = (e) => {
        e.preventDefault();
        setServiceID("");
        setServiceName("");
        setCaseID("");
        setClientID("");
        setCurrencyCode("");
        setDescription("");
        setDate("");
        setNumberOfAttorneys(0);
        setAttorneySelections([]);
    };

    useEffect(() => {
        setClientID(props.casesData.find(data => data.caseId === caseID)?.clientId)
        setCurrencyCode(clientID === '' ? '' : props.clientsData.find(data => data.clientId === clientID)?.currencyCode);
    }, [caseID, clientID, props.casesData, props.clientsData])

    const casesOptions = props.casesData.map((eachCase) => ({
        label: eachCase.caseName,
        value: eachCase.caseId,
    }));

    const filteredAttorneysData = props.attorneysData
        .filter(attorney => attorney.servicePricing
        ?.find(servicePrice => servicePrice.clientId === clientID) !== undefined);

    const handleNumberOfAttorneysChange = (e) => {
        const num = parseInt(e.target.value, 10);
        setNumberOfAttorneys(num);
        setAttorneySelections(prev => {
            const arr = [...prev];
            if (arr.length < num) {
                // Add new empty selections
                return arr.concat(Array(num - arr.length).fill({ attorneyId: '', minutes: '' }));
            } else {
                // Truncate
                return arr.slice(0, num);
            }
        });
    };

    const handleAttorneyChange = (idx, value) => {
        setAttorneySelections((prev) => prev.map((sel, i) => i === idx ? { ...sel, attorneyId: value } : sel));
    };

    const handleMinutesChange = (idx, value) => {
        setAttorneySelections((prev) => prev.map((sel, i) => i === idx ? { ...sel, minutes: value } : sel));
    };

    const validateMinutes = () => {
        return attorneySelections.every(sel => sel.minutes && parseInt(sel.minutes, 10) % 6 === 0);
    };

    const getClientNameForSelectedCase = () => {
        return caseID === '' ? '' : props.clientsData.find(data => data.clientId === props.casesData
            .find(data => data.caseId === caseID)?.clientId)?.clientName
    }

    return (
        <div className="add-form-container">
            <form onSubmit={handleSubmit} onReset={handleClear} className="add-form">
                <div className="form-group">
                    <label htmlFor="serviceId" className="form-label">
                        Service ID:
                    </label>
                    <input
                        id="serviceId"
                        className="form-input"
                        type="text"
                        value={serviceID}
                        onChange={(e) => setServiceID(e.target.value)}
                        placeholder="Enter service ID"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="serviceName" className="form-label">
                        Service Name:
                    </label>
                    <input
                        id="serviceName"
                        className="form-input"
                        type="text"
                        value={serviceName}
                        onChange={(e) => setServiceName(e.target.value)}
                        placeholder="Enter service name"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="caseId" className="form-label">
                        Case:
                    </label>
                    <select
                        id="caseId"
                        className="form-input"
                        value={caseID}
                        onChange={e => setCaseID(e.target.value)}
                    >
                        <option value="">Select a case</option>
                        {casesOptions
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
                </div>
                <div className="form-group">
                    <label htmlFor="clientName" className="form-label">
                        Client Name:
                    </label>
                    <input
                        id="clientName"
                        className="form-input"
                        type="text"
                        value={getClientNameForSelectedCase() || ""}
                        disabled
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
                    <label htmlFor="description" className="form-label">
                        Description:
                    </label>
                    <textarea
                        id="description"
                        className="form-input"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter service description"
                        rows="4"
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
                    <label htmlFor="numberOfAttorneys" className="form-label">
                        Number of Attorneys:
                    </label>
                    <select
                        id="numberOfAttorneys"
                        className="form-input"
                        value={numberOfAttorneys}
                        onChange={e => handleNumberOfAttorneysChange(e)}
                    >
                        {[...Array(filteredAttorneysData.length + 1).keys()].map(i => (
                            <option key={i} value={i}>{i}</option>
                        ))}
                    </select>
                </div>
                <DynamicInputList
                    count={numberOfAttorneys}
                    labelPrefix="Attorney"
                    dropdownOptions={filteredAttorneysData.map(attorney => ({
                        label: `${attorney.firstName} ${attorney.lastName}`,
                        value: attorney.attorneyId
                    }))}
                    dropdownValues={attorneySelections.map(sel => sel.attorneyId)}
                    inputValues={attorneySelections.map(sel => sel.minutes)}
                    onDropdownChange={handleAttorneyChange}
                    onInputChange={handleMinutesChange}
                    dropdownPlaceholder="Select attorney"
                    inputPlaceholder="Minutes (multiple of 6)"
                    inputType="number"
                />
                <div className="form-buttons">
                    <button type="submit" className="form-submit-btn">
                        Add Service
                    </button>
                    <button type="reset" className="form-clear-btn">
                        Clear
                    </button>
                </div>
            </form>
        </div>
    );
}

const mapStateToProps = state => {
    return {
        clientsData: getClientsData(state),
        casesData: getCasesData(state),
        attorneysData: getAttorneysData(state),
    }
}

const mapDispatchToProps = dispatch => ({
    requestServices: (serviceID) => {
        dispatch(requestServices(serviceID));
    },
    requestCases: (caseID) => {
        dispatch(requestCases(caseID));
    },
    requestAttorneys: (attorneyID) => {
        dispatch(requestAttorneys(attorneyID));
    },
    addMessage: (message) => {
        dispatch(addMessage(message));
    },
    clearMessage: () => {
        dispatch(clearMessage());
    },
});

export default connect(mapStateToProps, mapDispatchToProps)(AddService)