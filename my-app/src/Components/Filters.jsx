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
import './Filters.css';
import '../App.css';

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
      <div id={'filters-container'} className={'dropdown-components-container'}>
          <div id="filters-span-container" className={'dropdown-span-container'}>
            <span id="filters-span" className={'dropdown-container-span'} onClick={handleShowFilter}>
                FILTER AND GENERATE REPORT   {caCaretSquare()}
            </span>
          </div>
          {showFilter && <div id={'filters-form-container'} className={'dropdown-form-container'}>
              <div id={'filters-title-container'} className={'dropdown-field-container'}>
                  <div id={'filters-title-translation'} className={'dropdown-translation'}>
                      {'Title: '}
                  </div>
                  <input
                      id={'filters-title-input-field'}
                      className={'dropdown-input-field'}
                      type="text"
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                  />
              </div>
              <div id={'filters-date-container'} className={'dropdown-field-container'}>
                  <div id={'filters-from-date-container'} className={'from-date-container'}>
                      <div id={'filters-from-translation'} className={'dropdown-translation'}>
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
                          className={'DatePicker__input'}
                      />
                  </div>
                  <div id={'filters-to-date-container'} className={'to-date-container'}>
                      <div id={'filters-to-translation'} className={'dropdown-translation'}>
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
                          className={'DatePicker__input'}
                      />
                  </div>
              </div>
              <div id={'filters-client-name-container'} className={'dropdown-field-container'}>
                  <div id={'filters-client-translation'} className={'dropdown-translation'}>
                      {'Client: '}
                  </div>
                  <select id={'filters-client-name-select'} className={'dropdown-select'} value={clientID} onChange={handleChangeClients}>
                      <option key={"placeholder-client"} value={""} disabled={true}>
                          Select a client
                      </option>
                      {clientsOptions.map((option) => (
                          <option key={option.value} value={option.value}>{option.label}</option>
                      ))}
                  </select>
              </div>
              <div id={'filters-filter-checkboxes-container'} className={'dropdown-field-container'}>
                  {props.type === 'services' && <ServicesFilterCheckboxes />}
                  {props.type === 'disbursements' && <DisbursementsFilterCheckboxes />}
              </div>
              <div id={'filters-generate-report-button-container'} className={'dropdown-button-container'}>
                  <button id={'filters-generate-report-button'} className={'dropdown-button'} style={{cursor: props.fromDate === null
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