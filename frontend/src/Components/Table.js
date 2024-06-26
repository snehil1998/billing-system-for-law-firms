import {useTable, useSortBy, useFilters, useGlobalFilter, useExpanded} from "react-table";
import {
    addFromSearchDateServices,
    addToSearchDateServices,
    requestServices
} from "../Redux/Services/ServicesActions";
import {connect} from "react-redux";
import React, {useMemo} from "react";
import {DefaultColumnFilter} from "./DefaultColumnFilter";
import {defaultGlobalFilter} from "./DefaultGlobalFilter";
import {GlobalFilter} from "./GlobalFilter";
import MaUTable from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Filters from "./Filters"
import PropTypes from "prop-types";
import {requestClients} from "../Redux/Clients/ClientsActions";
import {requestAttorneys} from "../Redux/Attorneys/AttorneysActions";
import {requestCases} from "../Redux/Cases/CasesActions";
import {
    addFromSearchDateDisbursements,
    addToSearchDateDisbursements,
    requestDisbursements
} from "../Redux/Disbursements/DisbursementsActions";
import {getFromSearchDateForServices, getToSearchDateForServices} from "../Redux/Services/ServicesSelectors";
import {getFromSearchDateForDisbursements, getToSearchDateForDisbursements} from "../Redux/Disbursements/DisbursementsSelectors";
import './Table.css';
import {addMessage} from "../Redux/Message/MessageActions";
import {Page} from "./PagesEnum";
import { confirmAlert } from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css';
import {faTrash} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome"; // Import css

function Table(props) {

    const filterTypes = useMemo(
        () => ({
            text: (rows, id, filterValue) => {
                return rows.filter(row => {
                    const rowValue = row.values[id];
                    return rowValue !== undefined
                        ? String(rowValue)
                            .toLowerCase()
                            .startsWith(String(filterValue).toLowerCase())
                        : true;
                });
            }
        }),
        []
    );

    const defaultColumn = useMemo(
        () => ({
            Filter: DefaultColumnFilter
        }),
        []
    );

    const filterOptions = { filteredIds: [] };

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
        state,
        visibleColumns,
        preGlobalFilteredRows,
        setGlobalFilter,
    } = useTable(
        {
            columns: props.columns,
            data: props.data,
            defaultColumn,
            filterTypes,
            getSubRows: (row) => row.subRows,
            globalFilter: (rows, columnIds, filterValue) =>
                defaultGlobalFilter(rows, columnIds, filterValue, filterOptions),
            initialState: {
                sortBy: [
                    {
                        id: props.filterByColumn,
                        desc: props.isDescending,
                    }
                ]
            }
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        useExpanded,
    );

    const handleDelete = (rows) => {
        window.scrollTo(0, 0);
        let id = '';
        if(props.type === Page.SERVICES){
            id = rows.serviceid;
        } else if(props.type === Page.CLIENTS){
            id = rows.clientid;
        } else if(props.type === Page.ATTORNEYS){
            id = rows.attorneyid;
        } else if(props.type === Page.CASES){
            id = rows.caseid;
        } else if(props.type === Page.DISBURSEMENTS){
            id = rows.disbursementid;
        }
        fetch("/backend/" + props.type + "=" + id, { method: 'DELETE' })
                .then(async response => {
                    if (props.type === Page.SERVICES) {
                        props.requestServices('');
                    }
                    if (props.type === Page.CLIENTS) {
                        props.requestClients('');
                    }
                    if (props.type === Page.ATTORNEYS) {
                        props.requestAttorneys('');
                    }
                    if (props.type === Page.CASES) {
                        props.requestCases('');
                    }
                    if (props.type === Page.DISBURSEMENTS) {
                        props.requestDisbursements('');
                    }
                    props.addMessage("Row was deleted successfully from " + props.type);

                    if (!response.ok) {
                        return props.addMessage("There was a problem deleting the row.");
                    }
                })
            .catch(() => props.addMessage("There was a problem deleting the row."))
    }

    const submitDelete = (rows) => {
        confirmAlert({
            title: 'Confirm to delete',
            message: 'Are you sure you want to delete the row?',
            buttons: [
                {
                    label: 'Yes',
                    onClick: () => handleDelete(rows)
                },
                {
                    label: 'No',
                }
            ]
        });
    }

    return (
        <>
            <div className={'table-container'} colSpan={visibleColumns.length}>
                {props.type === Page.SERVICES
                && <Filters rows={rows} requestData={requestServices} addFromSearchDate={addFromSearchDateServices} addToSearchDate={addToSearchDateServices}
                fromDate={props.fromDateServices} toDate={props.toDateServices} type={props.type}/>}
                {props.type === Page.DISBURSEMENTS
                && <Filters rows={rows} requestData={requestDisbursements} addFromSearchDate={addFromSearchDateDisbursements} addToSearchDate={addToSearchDateDisbursements}
                            fromDate={props.fromDateDisbursements} toDate={props.toDateDisbursements} type={props.type}/>}
                <GlobalFilter
                    preGlobalFilteredRows={preGlobalFilteredRows}
                    globalFilter={state.globalFilter}
                    setGlobalFilter={setGlobalFilter} />
            </div>
            <MaUTable className={'table'} {...getTableProps()}>
                <TableHead className={"table head"}>
                {headerGroups.map(headerGroup => (
                    <TableRow className={"headers"} {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <TableCell className={"header-columns"}
                                       {...column.getHeaderProps(column.getSortByToggleProps())}>
                                {column.render("Header")}
                                <span className={'header-columns-span'}>{column.isSorted ? column.isSortedDesc ? " 🔽" : "" : ""}</span>
                            </TableCell>
                        ))}
                        <TableCell/>
                    </TableRow>
                ))}
                </TableHead>
                <TableBody {...getTableBodyProps()}>
                {rows.map((row, i) => {
                    prepareRow(row);
                    return (
                        <TableRow className={"data"} {...row.getRowProps()}>
                            {row.cells.map(cell => {
                                return <TableCell className={'table-cell'} {...cell.getCellProps()}>{cell.render("Cell")}</TableCell>;
                            })}
                            {!row.id.includes(".") && <TableCell>
                                <button id={'table-delete-button'} className={'dropdown-button'} onClick={() => submitDelete(row.original)}>
                                    <FontAwesomeIcon icon={faTrash} style={{color: 'maroon'}} />
                                </button>
                            </TableCell>}
                        </TableRow>
                    );
                })}
                </TableBody>
            </MaUTable>
        </>
    );
}

Table.propTypes = {
    columns: PropTypes.number.isRequired,
    data: PropTypes.array.isRequired,
    type: PropTypes.string.isRequired,
    filterByColumn: PropTypes.string.isRequired,
    isDescending: PropTypes.bool.isRequired,
    fromDateServices: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
    toDateServices: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
    fromDateDisbursements: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
    toDateDisbursements: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
}

const mapStateToProps = state => ({
    fromDateServices: getFromSearchDateForServices(state),
    toDateServices: getToSearchDateForServices(state),
    fromDateDisbursements: getFromSearchDateForDisbursements(state),
    toDateDisbursements: getToSearchDateForDisbursements(state),
});

const mapDispatchToProps = dispatch => ({
    requestServices: (clientID) => {
        dispatch(requestServices(clientID))
    },
    requestClients: (clientID) => {
        dispatch(requestClients(clientID))
    },
    requestCases: (caseID) => {
        dispatch(requestCases(caseID))
    },
    requestAttorneys: (attorneyID) => {
        dispatch(requestAttorneys(attorneyID))
    },
    requestDisbursements: (disbusementID) => {
        dispatch(requestDisbursements(disbusementID))
    },
    addMessage: (message) => {
        dispatch(addMessage(message))
    }
})


export default connect(mapStateToProps, mapDispatchToProps)(Table);