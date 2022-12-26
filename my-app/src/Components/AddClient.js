import React, {useState} from "react"
import {useDispatch} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import {requestClients} from "../Redux/Clients/ClientsActions";

const AddClient = () => {
    const [clientName, setClientName] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [amount, setAmount] = useState(0);
    const [showAddService, setShowAddService] = useState(false);
    const [message, setMessage] = useState("");

    const dispatch = useDispatch();
    let handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let res = await fetch("/clients", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    clientName: clientName,
                    currencyCode: currencyCode,
                    servicePricing: 0,
                    amount: amount,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setClientName("");
                setCurrencyCode("");
                setAmount(0);
                setMessage("Client was created successfully!");
            } else {
                setMessage("❗ Error occurred while adding data into clients");
            }
            dispatch(requestClients(''));
        } catch (err) {
            console.log("Error posting data into clients: ", err);
        }
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
        <div className="add service">
            <p style={{fontSize:'20px', textAlign:'center', width:'100vw', 
                backgroundColor:'white', color:'red'}}>{message}</p>
            <div className="add-client-span-container" style={{backgroundColor:'black', width:'13vw', marginLeft:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                    ADD A CLIENT   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form onSubmit={handleSubmit} style={{fontSize:'15px', marginLeft:'1vw',
                backgroundColor:'grey', height:'34vh', width:'98vw'}}>
                    <div className="client-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'client-name-translation'} style={{fontSize: '17px'}}>
                            {'Client Name: '}
                        </div>
                        <input
                            type="text"
                            value={clientName}
                            placeholder="Client name"
                            onChange={(e) => setClientName(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <div className="currency-code-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'currency-code-translation'} style={{fontSize: '17px'}}>
                            {'Currency Code: '}
                        </div>
                        <input
                            type="text"
                            value={currencyCode}
                            placeholder="Currency code"
                            onChange={(e) => setCurrencyCode(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <div className="amount-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'amount-translation'} style={{fontSize: '17px'}}>
                            {'Amount: '}
                        </div>
                        <input
                            type="number"
                            value={amount}
                            placeholder="Amount"
                            onChange={(e) => setAmount(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <button type="submit" className="add-button-container"
                            style={{width:'5vw', height:'4vh', fontSize:'15px', marginLeft: '1vw', marginTop: '2vh'}}>Add</button>
            </form>}
        </div>
    );
}
export default AddClient