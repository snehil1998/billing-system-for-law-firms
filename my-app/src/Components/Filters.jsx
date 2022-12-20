import React, {useEffect, useState} from "react";
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import {addFromSearchDate, addToSearchDate, requestServices} from "../Redux/Action";
import {connect} from "react-redux";
import ExportServicesPDF from "../CSVExporter/ExportServicesPDF";
import PropTypes from "prop-types";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCaretSquareDown, faCaretSquareUp} from "@fortawesome/free-solid-svg-icons";

function Filters(props) {
    const [selectedFromDate, setSelectedFromDate] = useState(null);
    const [selectedToDate, setSelectedToDate] = useState(null);
    const [clients, setClients] = useState([]);
    const [clientID, setClientID] = useState("");
    const [showFilter, setShowFilter] = useState(false);

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

    const handleChangeClients = (event) => {
        setClientID(event.target.value);
        props.requestServices(event.target.value);
    };

    const handleShowFilter = () => {
        if (showFilter) {
            setShowFilter(false)
        } else{
            setShowFilter(true)
        }
    }
    
    return (
      <div className={'filters-container'} style={{textAlign:'left', margin:'1vw'}}>
          {showFilter ?
              <div className="show-filter-span-container" style={{backgroundColor:'black', width:'23vw'}}>
                <span onClick={handleShowFilter} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                    FILTER AND GENERATE REPORT   <FontAwesomeIcon icon={faCaretSquareUp} />
                </span>
              </div>
              :
              <div className="add-service-span-container" style={{backgroundColor:'black', width:'23vw'}}>
                <span onClick={handleShowFilter} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                    FILTER AND GENERATE REPORT   <FontAwesomeIcon icon={faCaretSquareDown} />
                </span>
              </div>}
          {showFilter && <div className={'filter-form-container'} style={{backgroundColor: 'grey', height: '35vh', width: '22.9vw'}}>
              <div className={'date-from-container'} style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                  <div className={'from-translation'} style={{fontSize: '17px'}}>
                      {'From: '}
                  </div>
                  <DatePicker
                      value={selectedFromDate}
                      onChange={(fromDate) => {
                          setSelectedFromDate(fromDate);
                          props.addFromSearchDate(fromDate);
                      }}
                      inputPlaceholder="Select a date"
                      shouldHighlightWeekends
                      wrapperClassName={'DatePicker__input'}
                  />
              </div>
              <div className={'date-from-container'} style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                  <div className={'to-translation'} style={{fontSize: '17px'}}>
                      {'To: '}
                  </div>
                  <DatePicker
                      value={selectedToDate}
                      onChange={(toDate) => {
                          setSelectedToDate(toDate);
                          props.addToSearchDate(toDate);
                      }}
                      inputPlaceholder="Select a date"
                      shouldHighlightWeekends
                      wrapperClassName={'DatePicker__input'}
                  />
              </div>
              <div className={'client-name-container'} style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                  <div className={'client-translation'} style={{fontSize: '17px'}}>
                      {'Client: '}
                  </div>
                  <select value={clientID} onChange={handleChangeClients} style={{width:'12.6vw', height:'4vh'}}>
                      <option key={"placeholder-client"} value={""} disabled={true}>
                          Select a client
                      </option>
                      {clientsOptions.map((option) => (
                          <option key={option.value} value={option.value}>{option.label}</option>
                      ))}
                  </select>
              </div>
              <div className={'generate-report-button-container'} style={{marginLeft: '1vw', paddingTop: '2vh'}}>
                  <button style={{width:'9vw', height:'5vh', fontSize:'14px', cursor:'pointer'}} onClick={() => {
                      ExportServicesPDF({
                          rows: props.rows, fromDate: props.fromDate, toDate: props.toDate,
                          client: clientsOptions.find(client => (client.value === clientID)).label})}}>
                      Generate Report
                  </button>
              </div>
          </div>}
      </div>  
    );
}

Filters.propTypes = {
    rows: PropTypes.array.isRequired,
    fromDate: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
    toDate: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
}

const mapStateToProps = state => {
    return {
        fromDate: state.fromSearchDate !== null ? state.fromSearchDate : {day: '', month: '', year: ''},
        toDate: state.toSearchDate !== null ? state.toSearchDate : {day: '', month: '', year: ''},
    }
}

const mapDispatchToProps = dispatch => ({
    addFromSearchDate: (fromSearchDate) => {
        dispatch(addFromSearchDate(fromSearchDate));
    },
    addToSearchDate: (toSearchDate) => {
        dispatch(addToSearchDate(toSearchDate));
    },
    requestServices: (clientID) => {
        dispatch(requestServices(clientID));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(Filters);