import {useTable, useSortBy, useFilters, useGlobalFilter, useExpanded} from "react-table";
import {requestServices} from "../Redux/Services/ServicesActions";
import {useDispatch} from "react-redux";
import {useMemo} from "react";
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
                defaultGlobalFilter(rows, columnIds, filterValue, filterOptions)
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        useExpanded,
    );

    const dispatch = useDispatch();
    const handleDelete = (rows) => {
        let id = '';
        if(props.type === 'services'){
            id = rows.serviceid;
        } else if(props.type === 'clients'){
            id = rows.clientid;
        } else if(props.type === 'attorneys'){
            id = rows.attorneyid;
        } else if(props.type === 'cases'){
            id = rows.caseid;
        }
        fetch("/" + props.type + "=" + id, { method: 'DELETE' })
                .then(async response => {
                    if (props.type === 'services') {
                        dispatch(requestServices(''));
                    }
                    if (props.type === 'clients') {
                        dispatch(requestClients(''));
                    }
                    if (props.type === 'attorneys') {
                        dispatch(requestAttorneys(''));
                    }
                    if (props.type === 'cases') {
                        dispatch(requestCases(''));
                    }
                    setTimeout(deleteAlert, 1000)
                    const data = await response.json();

                    if (!response.ok) {
                        const error = (data && data.message) || response.status;
                        return Promise.reject(error);
                    }
                })
    }

    function deleteAlert() {
        alert("Delete successful");
    }

    return (
        <>
            <div
                colSpan={visibleColumns.length}
                style={{
                    textAlign: "left"
                }}>
                {(props.type === 'services' || props.type === 'disbursements') && <Filters rows={rows}/>}
                <GlobalFilter
                    preGlobalFilteredRows={preGlobalFilteredRows}
                    globalFilter={state.globalFilter}
                    setGlobalFilter={setGlobalFilter} />
            </div>
            <MaUTable className={"table"} {...getTableProps()}>
                <TableHead className={"table head"}>
                {headerGroups.map(headerGroup => (
                    <TableRow className={"headers"} {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <TableCell className={"header columns"}
                                       {...column.getHeaderProps(column.getSortByToggleProps())} 
                                       style={{color:"white", cursor:'pointer'}}>
                                {column.render("Header")}
                                <span style={{color:'white'}}>{column.isSorted ? (column.isSortedDesc ? " 🔽" : " 🔼") : ""}</span>
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
                                return <TableCell {...cell.getCellProps()} style={{color:'white'}}>{cell.render("Cell")}</TableCell>;
                            })}
                            {!row.id.includes(".") && <TableCell><button onClick={() => handleDelete(row.original)}>X</button></TableCell>}
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
}

export default Table;