import React, {useState} from "react"
import {connect} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import {requestCases} from "../Redux/Cases/CasesActions";
import PropTypes from "prop-types";
import {getClientsData} from "../Redux/Clients/ClientsSelectors";

const AddCase = (props) => {
    const [caseID, setCaseID] = useState("");
    const [caseName, setCaseName] = useState("");
    const [amount, setAmount] = useState(0);
    const [showAddService, setShowAddService] = useState(false);
    const [message, setMessage] = useState("");
    const [clientId, setClientId] = useState("");

    let handleSubmit = async (e) => {
        e.preventDefault();
        try {
            let res = await fetch("/cases", {
                method: "POST",
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify({
                    caseId: caseID,
                    caseName: caseName,
                    clientId: clientId,
                    currencyCode: props.clientsData.find(data => data.clientId === clientId)?.currencyCode,
                    amount: amount,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setCaseID("");
                setCaseName("");
                setClientId("");
                setAmount(0);
                setMessage("Case was created successfully!");
            } else {
                setMessage("❗ Error occurred while adding data into cases");
            }
            props.requestCases('');
        } catch (err) {
            console.log("Error posting data into cases: ", err);
        }
    };

    const handleAddService = () => {
        if (showAddService) {
            setShowAddService(false)
        } else{
            setShowAddService(true)
        }
    }

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    const faCaretSquare = () => {
        return showAddService ? <FontAwesomeIcon icon={faCaretSquareUp} /> : <FontAwesomeIcon icon={faCaretSquareDown} />
    }

    return (
        <div className="add-case">
            <p style={{fontSize:'20px', textAlign:'center', width:'100vw', 
                backgroundColor:'white', color:'red'}}>{message}</p>
            <div className="add-case-span-container" style={{backgroundColor:'black', width:'11vw', marginLeft:'1vw'}}>
                <span onClick={handleAddService} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                    ADD A CASE   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form onSubmit={handleSubmit} style={{fontSize:'15px', marginLeft:'1vw',
                backgroundColor:'grey', height:'50vh', width:'98vw'}}>
                    <div className="case-id-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'case-id-translation'} style={{fontSize: '17px'}}>
                            {'Case ID: '}
                        </div>
                        <input
                            type="text"
                            value={caseID}
                            placeholder="Case ID"
                            onChange={(e) => setCaseID(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <div className="case-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'case-name-translation'} style={{fontSize: '17px'}}>
                            {'Case Name: '}
                        </div>
                        <input
                            type="text"
                            value={caseName}
                            placeholder="Case name"
                            onChange={(e) => setCaseName(e.target.value)}
                            style={{width:'35vw', height:'4vh'}}
                        />
                    </div>
                    <div className="client-name-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'client-name-translation'} style={{fontSize: '17px'}}>
                            {'Client Name: '}
                        </div>
                        <select key={"client-name-selector"} value={clientId} onChange={(event) =>
                            setClientId(event.target.value)} style={{width:'39vw', height:'4vh'}}>
                            <option key={"placeholder-client"} value={""}>
                                Select a client
                            </option>
                            {clientsOptions.map((option) => (
                                <option key={option.value} value={option.value}>{option.label}</option>
                            ))}
                        </select>
                    </div>
                    <div className="currency-code-container" style={{marginLeft:'1vw', paddingTop:'1vh'}}>
                        <div className={'currency-code-translation'} style={{fontSize: '17px'}}>
                            {'Currency Code: '}
                        </div>
                        <input
                            type="text"
                            value={props.clientsData.find(data => data.clientId === clientId)?.currencyCode}
                            placeholder="Currency code"
                            style={{width:'35vw', height:'4vh'}}
                            disabled={true}
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

AddCase.propTypes = {
    requestCases: PropTypes.func.isRequired,
    clientsData: PropTypes.array.isRequired,
}

const mapStateToProps = state => ({
    clientsData: getClientsData(state),
})

const mapDispatchToProps = dispatch => ({
    requestCases: (caseID) => {
        dispatch(requestCases(caseID))
    }
})

export default connect(mapStateToProps, mapDispatchToProps)(AddCase)