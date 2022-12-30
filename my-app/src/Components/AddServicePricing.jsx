import React, {useState} from "react"
import {connect} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import {requestAttorneys} from "../Redux/Attorneys/AttorneysActions";
import PropTypes from "prop-types";
import {getAttorneysData} from "../Redux/Attorneys/AttorneysSelectors";
import {getClientsData} from "../Redux/Clients/ClientsSelectors";

const AddServicePricing = (props) => {
    const [selectedAttorney, setSelectedAttorney] = useState({});
    const [clientId, setClientId] = useState("");
    const [price, setPrice] = useState("");
    const [showAddService, setShowAddService] = useState(false);
    const [message, setMessage] = useState("");

    let handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const servicePricingList = [{clientId: clientId, price: parseInt(price)}]
            const selectedAttorneyArray = selectedAttorney.split(',');
            let res = await fetch("/attorneys=" + selectedAttorneyArray[0], {
                method: "PUT",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    firstName: selectedAttorneyArray[1],
                    lastName: selectedAttorneyArray[2],
                    servicePricing: servicePricingList,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setSelectedAttorney({});
                setClientId("");
                setPrice("");
                setMessage("Attorney was updated successfully!");
            } else {
                setMessage("❗ Error occurred while updating data for attorney");
            }
            props.requestAttorneys('');
        } catch (err) {
            console.log("Error updating data for attorney: ", err);
        }
    };

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    const attorneysOptions = props.attorneysData.map(eachAttorney => {
        const attorneyDataArray = [eachAttorney.attorneyId, eachAttorney.firstName, eachAttorney.lastName,]
        return { label: eachAttorney.firstName + " " + eachAttorney.lastName, value: attorneyDataArray }
    });

    const handleChangeClients = (event) => {
        setClientId(event.target.value);
    };

    const handleChangeAttorneys = (event) => {
        setSelectedAttorney(event.target.value);
    };

    const handleAddService = () => {
        if (showAddService) {
            setShowAddService(false)
        } else{
            setShowAddService(true)
        }
    }

    const faCaretSquare = () => {
        return showAddService ? <FontAwesomeIcon icon={faCaretSquareUp} /> : <FontAwesomeIcon icon={faCaretSquareDown} />
    }

    return (
        <div className="add-service-pricing">
            <p style={{fontSize:'20px', textAlign:'center', width:'100vw',
                backgroundColor:'white', color:'red'}}>{message}</p>
            <div className="add-attorney-span-container" style={{backgroundColor:'black', width:'17.5vw', marginLeft:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px', paddingLeft:'1vw'}}>
                    ADD SERVICE PRICING   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form onSubmit={handleSubmit} style={{fontSize:'15px', marginLeft:'1vw',
                backgroundColor:'grey', height:'25vh', width:'98vw'}}>
                <div className="attorney-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    <div className={'first-name-translation'} style={{fontSize: '17px'}}>
                        {'Attorney: '}
                    </div>
                    <select value={selectedAttorney} onChange={handleChangeAttorneys} style={{width:'39vw', height:'4vh'}}>
                        <option key={"placeholder-selected-attorney"} value={{}} disabled={true}>
                            Select an attorney
                        </option>
                        {attorneysOptions.map((option, i) => (
                            <option key={option.value} value={option.value}>{option.label}</option>
                        ))}
                    </select>
                </div>
                <div className="service-pricing-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                    <div className={"service-pricing"}>
                        <div className={'service-pricing-translation'} style={{fontSize: '17px'}}>
                            {'Service Pricing: '}
                        </div>
                        <select key={"client-name-selector"} value={clientId} onChange={handleChangeClients}
                                style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-client"} value={""}>
                                Select a client
                            </option>
                            {clientsOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                        <input
                            key={"service-pricing-price-input"}
                            type="number"
                            value={price}
                            placeholder="Price"
                            onChange={(e) => setPrice(e.target.value)}
                            style={{width:'10vw', height:'3.2vh'}}
                        />
                    </div>
                </div>
                <button type="submit" className="add-button-container"
                        style={{width:'5vw', height:'4vh', fontSize:'15px', marginLeft: '1vw', marginTop: '2vh'}}>Add</button>
            </form>}
        </div>
    );
}

AddServicePricing.propTypes = {
    attorneysData: PropTypes.array.isRequired,
    clientsData: PropTypes.array.isRequired,
    requestAttorneys: PropTypes.func.isRequired,
}

const mapStateToProps = state => {
    return {
        attorneysData: getAttorneysData(state),
        clientsData: getClientsData(state),
    }
}

const mapDispatchToProps = dispatch => ({
    requestAttorneys: (attorneyID) => {
        dispatch(requestAttorneys(attorneyID))
    }
})

export default connect(mapStateToProps, mapDispatchToProps)(AddServicePricing)