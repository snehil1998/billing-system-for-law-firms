import React, {useState} from "react"
import {connect} from "react-redux";
import  './MultiselectDropdown.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCaretSquareDown, faCaretSquareUp } from '@fortawesome/free-solid-svg-icons';
import {requestClients} from "../Redux/Clients/ClientsActions";
import PropTypes from "prop-types";
import './AddClient.css';

const AddClient = (props) => {
    const [clientID, setClientID] = useState("");
    const [clientName, setClientName] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [showAddService, setShowAddService] = useState(false);
    const [message, setMessage] = useState("");

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
                    clientId: clientID,
                    clientName: clientName,
                    currencyCode: currencyCode,
                    disbursementsAmount: 0,
                    servicesAmount: 0,
                    amount: 0,
                }),
            });
            if (res.status === 200 || res.status === 201) {
                setClientID("");
                setClientName("");
                setCurrencyCode("");
                setMessage("Client was created successfully!");
            } else {
                setMessage("❗ Error occurred while adding data into clients");
            }
            props.requestClients('');
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
        <div id="add-client-container" className={'dropdown-components-container'}>
            <div id="add-client-span-container" className={'dropdown-span-container'}>
                <span id={'add-client-container-span'} className={'dropdown-container-span'} onClick={handleAddService}>
                    ADD A CLIENT   {faCaretSquare()}
                </span>
            </div>
            {showAddService && <form onSubmit={handleSubmit} id={'add-client-form-container'} className={'dropdown-form-container'}>
                    <div id="add-client-client-id-container" className={'dropdown-field-container'}>
                        <div id={'add-client-client-id-translation'} className={'dropdown-translation'}>
                            {'Client ID: '}
                        </div>
                        <input
                            id={'add-client-client-id-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={clientID}
                            onChange={(e) => setClientID(e.target.value)}
                        />
                    </div>
                    <div id="add-client-client-name-container" className={'dropdown-field-container'}>
                        <div id={'add-client-client-name-translation'} className={'dropdown-translation'}>
                            {'Client Name: '}
                        </div>
                        <input
                            id={'add-client-client-name-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={clientName}
                            onChange={(e) => setClientName(e.target.value)}
                        />
                    </div>
                    <div id="add-client-currency-code-container" className={'dropdown-field-container'}>
                        <div id={'add-client-currency-code-translation'} className={'dropdown-translation'}>
                            {'Currency Code: '}
                        </div>
                        <input
                            id={'add-client-currency-code-input-field'}
                            className={'dropdown-input-field'}
                            type="text"
                            value={currencyCode}
                            onChange={(e) => setCurrencyCode(e.target.value)}
                        />
                    </div>
                    <div id={'add-client-add-button-container'} className={'dropdown-button-container'}>
                        <button type="submit" id="add-client-add-button" className={'dropdown-button'}>
                            ADD
                        </button>
                    </div>
            </form>}
        </div>
    );
}

AddClient.propTypes = {
    requestClients: PropTypes.func.isRequired,
}

const mapDispatchToProps = dispatch => ({
    requestClients: (clientID) => {
        dispatch(requestClients(clientID))
    }
});

export default connect(null, mapDispatchToProps)(AddClient)