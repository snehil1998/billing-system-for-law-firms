import React, {useEffect, useState} from "react"
import {useDispatch} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import {requestAttorneys} from "../Redux/Attorneys/AttorneysActions";

const AddAttorney = () => {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [clientIdList, setClientIdList] = useState({});
    const [clients, setClients] = useState([]);
    const [priceList, setPriceList] = useState({});
    const [showAddService, setShowAddService] = useState(false);
    const [numberOfServicePricing, setNumberOfServicePricing] = useState("0");
    const [message, setMessage] = useState("");

    const dispatch = useDispatch();
    let handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let servicePricingList = []
            Object.keys(clientIdList).forEach( index => {
                servicePricingList.push({clientId: clientIdList[index], price: parseInt(priceList[index])})
            })
            let res = await fetch("/attorneys", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    servicePricing: servicePricingList,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setFirstName("");
                setLastName("");
                setNumberOfServicePricing("0");
                setClientIdList({});
                setPriceList({});
                setMessage("Attorney was created successfully!");
            } else {
                setMessage("❗ Error occurred while adding data into attorneys");
            }
            dispatch(requestAttorneys(''));
        } catch (err) {
            console.log("Error posting data into attorneys: ", err);
        }
    };

    useEffect(() => {
        fetch("/clients")
            .then(response => response.json())
            .then(json => {
                setClients(json);
            })
    }, []);

    const clientsOptions = clients.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    let numberOfServicePricingList = [];
    for(let num=1; num<=clients.length; num++) {
        numberOfServicePricingList.push(num);
    }

    const numberOfServicePricingOptions =
        numberOfServicePricingList.map(eachNumber => {
            return { label: eachNumber, value: eachNumber }
        });

    const handleChangeClients = (event, i) => {
        setClientIdList({...clientIdList, [i]: event.target.value});
    };

    const handleAddService = () => {
        if (showAddService) {
            setShowAddService(false)
        } else{
            setShowAddService(true)
        }
    }

    const handleNumberOfServicePricing = (event) => {
        setNumberOfServicePricing(event.target.value);
    };

    const faCaretSquare = () => {
        return showAddService ? <FontAwesomeIcon icon={faCaretSquareUp} /> : <FontAwesomeIcon icon={faCaretSquareDown} />
    }

    return (
        <div className="add-attorney">
            <p style={{fontSize:'20px', textAlign:'center', width:'100vw',
                backgroundColor:'white', color:'red'}}>{message}</p>
            <div className="add-attorney-span-container" style={{backgroundColor:'black', width:'13vw', marginLeft:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                    ADD ATTORNEY   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form onSubmit={handleSubmit} style={{fontSize:'15px', marginLeft:'1vw',
                backgroundColor:'grey', height:(35+(parseInt(numberOfServicePricing)*6.5)).toString()+'vh', width:'98vw'}}>
                <div className="first-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    <div className={'first-name-translation'} style={{fontSize: '17px'}}>
                        {'First Name: '}
                    </div>
                    <input
                        type="text"
                        value={firstName}
                        placeholder="First name"
                        onChange={(e) => setFirstName(e.target.value)}
                        style={{width:'35vw', height:'4vh'}}
                    />
                </div>
                <div className="last-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    <div className={'first-name-translation'} style={{fontSize: '17px'}}>
                        {'Last Name: '}
                    </div>
                    <input
                        type="text"
                        value={lastName}
                        placeholder="Last name"
                        onChange={(e) => setLastName(e.target.value)}
                        style={{width:'35vw', height:'4vh'}}
                    />
                </div>
                <div className="number-of-service-pricing-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    <div className={'number-of-service-pricing-translation'} style={{fontSize: '17px'}}>
                        {'Number of Service Pricing: '}
                    </div>
                    <select value={numberOfServicePricing} onChange={handleNumberOfServicePricing} style={{width:'39vw', height:'4vh'}}>
                        <option key={"placeholder-number-of-attorneys"} value={"0"} disabled={true}>
                            Select number of service pricing
                        </option>
                        {numberOfServicePricingOptions.map((option) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div className="service-pricing-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    {[...Array(parseInt(numberOfServicePricing))].map((e, i) => {
                        return(
                            <div className={"service-pricing-"+i}>
                                <div className={'service-pricing' + i + '-translation'} style={{fontSize: '17px'}}>
                                    {'Service Pricing ' + (i+1).toString() + ': '}
                                </div>
                                <select key={"client-name-selector-"+i} value={clientIdList[i]} onChange={(event) =>
                                    handleChangeClients(event, i)} style={{width:'39vw', height:'4vh'}}>
                                    <option key={"placeholder-client-"+ i} value={""}>
                                        Select a client
                                    </option>
                                    {clientsOptions.map((option) => (
                                        <option key={option.value} value={option.value}>{option.label}</option>
                                    ))}
                                </select>
                                <input
                                    key={"service-pricing-price-input-"+i}
                                    type="number"
                                    value={priceList[i]}
                                    placeholder="Price"
                                    onChange={(e) =>
                                        setPriceList({...priceList, [i]: e.target.value})}
                                    style={{width:'10vw', height:'3.2vh'}}
                                />
                            </div>)
                    })}
                </div>
                <button type="submit" className="add-button-container"
                        style={{width:'5vw', height:'4vh', fontSize:'15px', marginLeft: '1vw', marginTop: '2vh'}}>Add</button>
            </form>}
        </div>
    );
}
export default AddAttorney