import React, {useState} from "react";
import '@amir04lm26/react-modern-calendar-date-picker/lib/DatePicker.css';
import DatePicker from '@amir04lm26/react-modern-calendar-date-picker';
import {connect, useDispatch} from "react-redux";
import ExportServicesPDF from "../CSVExporter/ExportServicesPDF";
import PropTypes from "prop-types";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCaretSquareDown, faCaretSquareUp} from "@fortawesome/free-solid-svg-icons";
import {getClientsData} from "../Redux/Clients/ClientsSelectors";
import ServicesFilterCheckboxes from "./ServicesFilterCheckboxes";
import {getFilterCheckboxesForServices} from "../Redux/Services/ServicesSelectors";
import DisbursementsFilterCheckboxes from "./DisbursementsFilterCheckboxes";
import ExportDisbursementsPDF from "../CSVExporter/ExportDisbursementsPDF";
import {getFilterCheckboxesForDisbursements} from "../Redux/Disbursements/DisbursementsSelectors";

function Filters(props) {
    const dispatch = useDispatch();
    const [selectedFromDate, setSelectedFromDate] = useState(null);
    const [selectedToDate, setSelectedToDate] = useState(null);
    const [clientID, setClientID] = useState("");
    const [showFilter, setShowFilter] = useState(false);
    const [title, setTitle] = useState("");

    const clientsOptions = props.clientsData.map(eachClient => {
        return { label: eachClient.clientName, value: eachClient.clientId }
    });

    const handleChangeClients = (event) => {
        setClientID(event.target.value);
        dispatch(props.requestData(event.target.value));
    };

    const handleShowFilter = () => {
        if (showFilter) {
            setShowFilter(false)
        } else{
            setShowFilter(true)
        }
    }

    const caCaretSquare = () => {
        return showFilter ? <FontAwesomeIcon icon={faCaretSquareUp} /> : <FontAwesomeIcon icon={faCaretSquareDown} />
    }

    const pdfConverter = () => {
        if(props.type === 'services') {
            return ExportServicesPDF({
                rows: props.rows, fromDate: props.fromDate, toDate: props.toDate,
                client: clientsOptions.find(client => (client.value === clientID))?.label,
                title: title,
                filterCheckboxes: props.servicesFilterCheckboxes})
        }
        if(props.type === 'disbursements') {
            return ExportDisbursementsPDF({
                rows: props.rows, fromDate: props.fromDate, toDate: props.toDate,
                client: clientsOptions.find(client => (client.value === clientID))?.label,
                title: title,
                filterCheckboxes: props.disbursementsFilterCheckboxes})
        }
    }

    return (
      <div className={'filters-container'} style={{textAlign:'left', margin:'1vw'}}>
          <div className="show-filter-span-container" style={{backgroundColor:'black', width:'23vw'}}>
            <span onClick={handleShowFilter} style={{cursor:'pointer', fontSize:'20px', marginLeft:'1vw'}}>
                FILTER AND GENERATE REPORT   {caCaretSquare()}
            </span>
          </div>
          {showFilter && <div className={'filter-form-container'} style={{backgroundColor: 'grey', height: '40vh', width: '98vw'}}>
              <div className={'title-container'} style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                  <div className={'title-translation'} style={{fontSize: '17px'}}>
                      {'Title: '}
                  </div>
                  <input
                      type="text"
                      value={title}
                      placeholder="Title"
                      onChange={(e) => setTitle(e.target.value)}
                      style={{width:'35vw', height:'4vh'}}
                  />
              </div>
              <div className={'date-container'} style={{marginLeft: '1vw', paddingTop: '1vh', display:'flex'}}>
                  <div className={'from-date-container'}>
                      <div className={'from-translation'} style={{fontSize: '17px'}}>
                          {'From: '}
                      </div>
                      <DatePicker
                          value={selectedFromDate}
                          onChange={(fromDate) => {
                              setSelectedFromDate(fromDate);
                              dispatch(props.addFromSearchDate(fromDate));
                          }}
                          inputPlaceholder="Select a date"
                          shouldHighlightWeekends
                          wrapperClassName={'DatePicker__input'}
                      />
                  </div>
                  <div className={'to-date-container'} style={{paddingLeft: '2vw'}}>
                      <div className={'to-translation'} style={{fontSize: '17px'}}>
                          {'To: '}
                      </div>
                      <DatePicker
                          value={selectedToDate}
                          onChange={(toDate) => {
                              setSelectedToDate(toDate);
                              dispatch(props.addToSearchDate(toDate));
                          }}
                          inputPlaceholder="Select a date"
                          shouldHighlightWeekends
                          wrapperClassName={'DatePicker__input'}
                      />
                  </div>
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
              <div className={'services-filter-checkboxes-container'} style={{marginLeft: '1vw', paddingTop: '1vh'}}>
                  {props.type === 'services' && <ServicesFilterCheckboxes />}
                  {props.type === 'disbursements' && <DisbursementsFilterCheckboxes />}
              </div>
              <div className={'generate-report-button-container'} style={{marginLeft: '1vw', paddingTop: '2vh'}}>
                  <button style={{width:'9vw', height:'5vh', fontSize:'14px', cursor: props.fromDate === null
                          || props.toDate === null || clientID === "" || (props.disbursementsFilterCheckboxes.length === 0 && props.servicesFilterCheckboxes.length === 0) || title === "" ? 'default' : 'pointer'}}
                          disabled={props.fromDate === null || props.toDate === null || clientID === "" || (props.disbursementsFilterCheckboxes.length === 0 && props.servicesFilterCheckboxes.length === 0) || title === ""}
                          onClick={() => pdfConverter()}>
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
    addFromSearchDate: PropTypes.func.isRequired,
    addToSearchDate: PropTypes.func.isRequired,
    requestData: PropTypes.func.isRequired,
    clientsData: PropTypes.array.isRequired,
    servicesFilterCheckboxes: PropTypes.array.isRequired,
    type: PropTypes.string.isRequired,
}

const mapStateToProps = state => {
    return {
        clientsData: getClientsData(state),
        servicesFilterCheckboxes: getFilterCheckboxesForServices(state),
        disbursementsFilterCheckboxes: getFilterCheckboxesForDisbursements(state),
    }
}

export default connect(mapStateToProps, null)(Filters);