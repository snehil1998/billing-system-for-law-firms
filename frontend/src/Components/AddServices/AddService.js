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

const AddService = (props) => {
    const [serviceID, setServiceID] = useState("");
    const [serviceName, setServiceName] = useState("");
    const [caseID, setCaseID] = useState("");
    const [attorneyID, setAttorneyID] = useState("");
    const [description, setDescription] = useState("");
    const [date, setDate] = useState("");
    const [hours, setHours] = useState("");
    const [clientID, setClientID] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [selectedDate, setSelectedDate] = useState(null);
    const [minutes, setMinutes] = useState({});
    const [selectedAttorneys, setSelectedAttorneys] = useState({});
    const [numberOfAttorneys, setNumberOfAttorneys] = useState("0");
    const [shouldPost, setShouldPost] = useState(true);

    useEffect(() => {
        props.requestCases("");
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        window.scrollTo(0, 0);
        if (!serviceID || !serviceName || !caseID || !attorneyID || !description || !date || !hours) {
            return props.addMessage("Please complete all fields to add a service.");
        }
        try {
            await servicesApi.create({
                serviceId: serviceID,
                serviceName: serviceName,
                caseId: caseID,
                attorneyId: attorneyID,
                description: description,
                date: date,
                hours: parseFloat(hours),
            });
            setServiceID("");
            setServiceName("");
            setCaseID("");
            setAttorneyID("");
            setDescription("");
            setDate("");
            setHours("");
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
        setAttorneyID("");
        setDescription("");
        setDate("");
        setHours("");
    };

    useEffect(() => {
        setDate(selectedDate?.year+'-'+
            selectedDate?.month.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false})+'-'
            +selectedDate?.day.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false}))
    }, [selectedDate])

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

    const getClientNameForSelectedCase = () => {
        return caseID === '' ? '' : props.clientsData.find(data => data.clientId === props.casesData
            .find(data => data.caseId === caseID)?.clientId)?.clientName
    }

    useEffect(() => {
        props.requestAttorneys('');
    }, [])

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
                        onChange={handleChangeCases}
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
                    <label htmlFor="hours" className="form-label">
                        Hours:
                    </label>
                    <input
                        id="hours"
                        className="form-input"
                        type="number"
                        step="0.1"
                        min="0"
                        value={hours}
                        onChange={(e) => setHours(e.target.value)}
                        placeholder="Enter hours"
                    />
                </div>
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