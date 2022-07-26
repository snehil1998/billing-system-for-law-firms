import {useTable, useSortBy, useFilters, useGlobalFilter, useExpanded} from "react-table";
import {requestServices} from "../Redux/Action";
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
import jsPDF from "jspdf";
import "jspdf-autotable";

export default function Table({ columns, data, type }) {

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
            columns,
            data,
            defaultColumn,
            filterTypes,
            getSubRows: (row: any) => row.subRows,
            globalFilter: (rows, columnIds, filterValue) =>
                defaultGlobalFilter(rows, columnIds, filterValue, filterOptions)
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        useExpanded,
    );

    const dispatch = useDispatch();
    const handleDelete = (serviceID) => {
        fetch("/services="+serviceID, { method: 'DELETE' })
                .then(async response => {
                    if (type === 'services') {
                        dispatch(requestServices(''));
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
    
    const exportPDF = () => {
        let csvRows = []
        rows.forEach((row, index) => {
            let csvRow = {}
            csvRow["Sno"] = index+1
            csvRow["Case"] = row.original.casename
            csvRow["Client"] = row.original.clientname
            csvRow["Service"] = row.original.service
            csvRow["Description"] = row.original.description
            csvRow["Date"] = row.original.date
            csvRow["Amount"] = row.original.amount
            if(row.canExpand){
                csvRow["Attorneys"] = row.original?.subRows[0].attorneys
                csvRow["Minutes"] = row.original?.subRows[0].minutes
                csvRow["Hours"] = row.original?.subRows[0].hours
                csvRow["Pricing"] = row.original?.subRows[0].pricing
                csvRow["Total"] = row.original?.subRows[0].total
            } else {
                csvRow["Attorneys"] = row.original.attorneys
                csvRow["Minutes"] = row.original.minutes
                csvRow["Hours"] = row.original.hours
                csvRow["Pricing"] = row.original.pricing
                csvRow["Total"] = row.original.total
            }
            csvRows.push(csvRow)
            csvRow = {}
            row.original?.subRows?.forEach((subRow, index) => {
                if (index > 0){
                    csvRow["Sno"] = ""
                    csvRow["Case"] = subRow.casename
                    csvRow["Client"] = subRow.clientname
                    csvRow["Service"] = subRow.service
                    csvRow["Description"] = subRow.description
                    csvRow["Date"] = subRow.date
                    csvRow["Amount"] = subRow.amount
                    csvRow["Attorneys"] = subRow.attorneys
                    csvRow["Minutes"] = subRow.minutes
                    csvRow["Hours"] = subRow.hours
                    csvRow["Pricing"] = subRow.pricing
                    csvRow["Total"] = subRow.total
                    csvRows.push(csvRow)
                    csvRow = {}
                }
            })
        })
        const unit = "pt";
        const size = "A4";
        const orientation = "landscape";

        const marginLeft = 40;
        const doc = new jsPDF(orientation, unit, size);

        doc.setFontSize(15);

        const title = "Services Report";
        const report = "Report"
        const client = "Client: " + csvRows[0]['Client']
        const period = "Period: " + "Date to date"
        const headers = [["S.No.", "Case", "Client", "Service", "Description", "Date", "Service Fee", "Attorney(s)",
        "Time spent (in minutes)", "Time spent (in hours)", "Rate per hour", "Amount for Attorney"]];

        const data = csvRows.map(row=> [row.Sno, row.Case, row.Client, row.Service, row.Description, row.Date,
        row.Amount, row.Attorneys, row.Minutes, row.Hours, row.Pricing, row.Total]);

        let content = {
            startY: 150,
            head: headers,
            body: data
        };

        doc.text(title, marginLeft, 40);
        doc.text(report, marginLeft, 70);
        doc.text(client, marginLeft, 100);
        doc.text(period, marginLeft, 130);
        doc.autoTable(content);
        doc.save("report.pdf")
    }

    return (
        <>
            <div
                colSpan={visibleColumns.length}
                style={{
                    textAlign: "left"
                }}>
                <GlobalFilter
                    preGlobalFilteredRows={preGlobalFilteredRows}
                    globalFilter={state.globalFilter}
                    setGlobalFilter={setGlobalFilter} />
            </div>
            <div style={{textAlign:'right', width:'99vw'}}>
                <button style={{width:'9vw', height:'5vh', fontSize:'14px', cursor:'pointer'}} onClick={() => exportPDF()}>Generate Service Report</button>
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
                            {!row.id.includes(".") && <TableCell><button onClick={() => handleDelete(row.original.serviceid)}>X</button></TableCell>}
                        </TableRow>
                    );
                })}
                </TableBody>
            </MaUTable>
        </>
    );
}