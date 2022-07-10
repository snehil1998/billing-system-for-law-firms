import React, {useEffect, useState} from "react"
import {useDispatch} from "react-redux";
import {requestServices} from "../Redux/Action";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';

const AddService = () => {
    const [caseID, setCaseID] = useState("");
    const [clientID, setClientID] = useState("");
    const [service, setService] = useState("");
    const [description, setDescription] = useState("");
    const [date, setDate] = useState("");
    const [minutes, setMinutes] = useState({});
    const [message, setMessage] = useState("");
    const [cases, setCases] = useState([]);
    const [clients, setClients] = useState([]);
    const [attorneys, setAttorneys] = useState([]);
    const [selectedAttorneys, setSelectedAttorneys] = useState({});
    const [showAddService, setShowAddService] = useState(false);
    const [numberOfAttorneys, setNumberOfAttorneys] = useState("0");

    const dispatch = useDispatch();
    let handleSubmit = async (e) => {
        e.preventDefault();
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
                    description: description,
                    date: date,
                    attorneys: attorneysList,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setCaseID("");
                setClientID("");
                setService("");
                setDescription("");
                setDate("");
                setMinutes("");
                setSelectedAttorneys("");
                setMessage("Service created successfully");
            } else {
                setMessage("Some error occurred");
            }
            dispatch(requestServices(''));
            setTimeout(() => {
                alert(message);
            }, 2000);

        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        fetch("/cases")
            .then(response => response.json())
            .then(json => {
                setCases(json);
            })
    }, []);

    useEffect(() => {
        fetch("/clients")
            .then(response => response.json())
            .then(json => {
                setClients(json);
            })
    }, []);

    useEffect(() => {
        fetch("/attorneys")
            .then(response => response.json())
            .then(json => {
                setAttorneys(json);
            })
    }, []);

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

    return (
        <div className="add service">
            {showAddService ?
            <div className="add-service-span-container" style={{backgroundColor:'black', width:'12vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px'}}>
                    ADD A SERVICE   <FontAwesomeIcon icon={faCaretSquareUp} />
                </span>
            </div>
            :
            <div className="add-service-span-container" style={{backgroundColor:'black', width:'12vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px'}}>
                    ADD A SERVICE   <FontAwesomeIcon icon={faCaretSquareDown} />
                </span>
            </div>}
            {showAddService ?
                <form onSubmit={handleSubmit} style={{fontSize:15, padding:'1.5vh'}}>
                    <div className="case-client-name-container">
                        <select value={caseID} onChange={handleChangeCases} style={{width:'57.5vw', height:'4vh'}}>
                            <option key={"placeholder-case"} value={""}>Select a case</option>
                            {caseOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                        <select value={clientID} onChange={handleChangeClients} placeholder="Select an option"
                                style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-client"} value={""}>Select a client</option>
                            {clientsOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="service-description-container">
                        <input
                            type="text"
                            value={service}
                            placeholder="Service"
                            onChange={(e) => setService(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                        <input
                            type="text"
                            value={description}
                            placeholder="Description"
                            onChange={(e) => setDescription(e.target.value)}
                            style={{width:'60.5vw', height:'4vh'}}
                        />
                    </div>
                    <div className="date-minutes-container">
                        <input
                            type="text"
                            value={date}
                            placeholder="Date"
                            onChange={(e) => setDate(e.target.value)}
                            style={{width:'15vw', height:'4vh'}}
                        />
                    </div>
                    <div className="number-of-attorneys-container">
                        <select value={numberOfAttorneys} onChange={handleNumberOfAttorneys} placeholder="Select number of attorneys"
                                style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-number-of-attorneys"} value={"0"}>Select number of attorneys</option>
                            {numberOfAttorneysOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="attorneys-container">
                        {[...Array(parseInt(numberOfAttorneys))].map((e, i) => {
                            return(
                            <div className={"attorney-"+i}>
                                <select key={"attorney-name-selector-"+i} onChange={(event) =>
                                    handleChangeAttorneys(event, i)}
                                           style={{width:'39vw', height:'4vh'}}>
                                    <option key={"placeholder-attorneys-"+ i} value={""}>Select attorney</option>
                                    {attorneysOptions.map((option) => (
                                        <option key={option.value} value={option.value}>{option.label}</option>
                                    ))}
                                </select>
                                <input
                                    key={"attorney-minutes-input-"+i}
                                    type="number"
                                    value={minutes.i}
                                    placeholder="Minutes"
                                    onChange={(e) =>
                                        setMinutes({...minutes, [i]: e.target.value})}
                                    style={{width:'10vw', height:'4vh'}}
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