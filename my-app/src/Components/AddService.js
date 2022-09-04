import React, {useEffect, useState} from "react"
import {useDispatch} from "react-redux";
import {requestServices} from "../Redux/Action";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';

const AddService = () => {
    const [caseID, setCaseID] = useState("");
    const [clientID, setClientID] = useState("");
    const [service, setService] = useState("");
    const [date, setDate] = useState("");
    const [selectedDate, setSelectedDate] = useState(null);
    const [minutes, setMinutes] = useState({});
    const [message, setMessage] = useState("");
    const [cases, setCases] = useState([]);
    const [clients, setClients] = useState([]);
    const [attorneys, setAttorneys] = useState([]);
    const [selectedAttorneys, setSelectedAttorneys] = useState({});
    const [showAddService, setShowAddService] = useState(false);
    const [numberOfAttorneys, setNumberOfAttorneys] = useState("0");
    const [shouldPost, setShouldPost] = useState(true);

    const dispatch = useDispatch();
    let handleSubmit = async (e) => {
        e.preventDefault();
        if(shouldPost){
            try {
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
                    setMessage("Service was created successfully!");
                } else {
                    setMessage("❗ Error occurred while adding data into services");
                }
                dispatch(requestServices(''));
            } catch (err) {
                console.log("Error posting data into services: ", err);
            }
        }
    };

    useEffect(() => {
        fetch("/cases")
            .then(response => response.json())
            .then(json => {
                setCases(json);
            })
        
        fetch("/clients")
            .then(response => response.json())
            .then(json => {
                setClients(json);
            })

        fetch("/attorneys")
            .then(response => response.json())
            .then(json => {
                setAttorneys(json);
            })
    }, []);

    useEffect(() => {
        setDate(selectedDate?.year+'-'+
            selectedDate?.month.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false})+'-'
            +selectedDate?.day.toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false}))
    }, [selectedDate])

    const caseOptions = cases.map(eachCase => {
            return { label: eachCase.caseName, value: eachCase.caseId }
        });

    const clientsOptions = clients.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    let numberOfAttorneysList = [];
    for(let num=1; num<=attorneys?.length; num++) {
        numberOfAttorneysList.push(num);
    }

    const numberOfAttorneysOptions =
        numberOfAttorneysList.map(eachNumber => {
            return { label: eachNumber, value: eachNumber }
        });

    const  attorneysOptions  = attorneys.map(eachAttorney => {
        return { label:  eachAttorney.firstName + " " + eachAttorney.lastName, value:  eachAttorney.attorneyId,
            style: {fontSize: '85%', color:'black'}}
    });

    const handleChangeCases = (event) => {
        setCaseID(event.target.value);
    };

    const handleChangeClients = (event) => {
        setClientID(event.target.value);
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
            setMessage("❗ Please make sure you enter valid minutes for attorneys. They should be a multiple of 6.");
        } else {
            setShouldPost(true)
            setMessage("");
        }
    }

    return (
        <div className="add service">
            <p style={{fontSize:'20px', textAlign:'center', width:'100vw', 
                backgroundColor:'white', color:'red'}}>{message}</p>
            {showAddService ?
            <div className="add-service-span-container" style={{backgroundColor:'black', width:'12vw', margin:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px'}}>
                    ADD A SERVICE   <FontAwesomeIcon icon={faCaretSquareUp} />
                </span>
            </div>
            :
            <div className="add-service-span-container" style={{backgroundColor:'black', width:'12vw', margin:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px'}}>
                    ADD A SERVICE   <FontAwesomeIcon icon={faCaretSquareDown} />
                </span>
            </div>}
            {showAddService ?
                <form onSubmit={handleSubmit} style={{fontSize:15, padding:'1.5vh'}}>
                    <div className="case-client-name-container">
                        <select value={caseID} onChange={handleChangeCases} style={{width:'57.5vw', height:'4vh'}}>
                            <option key={"placeholder-case"} value={""} disabled={true}>
                                Select a case
                            </option>
                            {caseOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                        <select value={clientID} onChange={handleChangeClients} style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-client"} value={""} disabled={true}>
                                Select a client
                            </option>
                            {clientsOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="service-container">
                        <input
                            type="text"
                            value={service}
                            placeholder="Service"
                            onChange={(e) => setService(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <div className="date-container">
                        <DatePicker
                            value={selectedDate}
                            onChange={setSelectedDate}
                            inputPlaceholder="Select a date"
                            shouldHighlightWeekends
                            wrapperClassName={'DatePicker__input'}
                        />
                    </div>
                    <div className="number-of-attorneys-container">
                        <select value={numberOfAttorneys} onChange={handleNumberOfAttorneys} style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-number-of-attorneys"} value={"0"} disabled={true}>
                                Select number of attorneys
                            </option>
                            {numberOfAttorneysOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="attorneys-container">
                        {[...Array(parseInt(numberOfAttorneys))].map((e, i) => {
                            return(
                            <div className={"attorney-"+i}>
                                <select key={"attorney-name-selector-"+i} value={selectedAttorneys[i]} onChange={(event) =>
                                    handleChangeAttorneys(event, i)} style={{width:'39vw', height:'4vh'}}>
                                    <option key={"placeholder-attorneys-"+ i} value={""}>
                                        Select attorney
                                    </option>
                                    {attorneysOptions.map((option) => (
                                        <option key={option.value} value={option.value}>{option.label}</option>
                                    ))}
                                </select>
                                <input
                                    key={"attorney-minutes-input-"+i}
                                    type="number"
                                    value={minutes[i]}
                                    placeholder="Minutes"
                                    onChange={(e) =>
                                        setMinutes({...minutes, [i]: e.target.value})}
                                    style={{width:'10vw', height:'3.2vh'}}
                                    onBlur={validateMinutes}
                                />
                            </div>)
                        })}
                    </div>
                    <button type="submit" className="add-button-container"
                            style={{width:'5vw', height:'4vh', fontSize:'15px'}}>Add</button>
            </form>
                    :""}
        </div>
    );
}
export default AddService