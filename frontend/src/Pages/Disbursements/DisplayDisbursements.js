import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {getCasesData} from "../../redux/cases/CasesSelectors";
import {requestDisbursements} from "../../redux/disbursements/DisbursementsActions";
import {getDisbursementsData, getDisbursementsIsLoading} from "../../redux/disbursements/DisbursementsSelectors";
import {clearMessage} from "../../redux/message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';
import { Box, Button, IconButton, Tooltip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { disbursementsApi } from "../../services/api";
import { handleExportRows } from "../../components/common/CsvExporter";
import FileDownloadIcon from '@mui/icons-material/FileDownload';

const DisplayServices = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestDisbursements(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
                {
                    header: 'Case Name',
                    accessorKey: 'casename',
                    size: 300,
                }, {
                    header: 'Client Name',
                    accessorKey: 'clientname',
                    size: 300,
                }, {
                    header: 'Disbursement',
                    accessorKey: 'disbursement',
                    size: 150,
                }, {
                    header: 'Date',
                    accessorKey: 'date',
                    size: 150,
                }, {
                    header: 'Currency Code',
                    accessorKey: 'currencycode',
                    size: 150
                }, {
                    header: 'Conversion Rate',
                    accessorKey: 'conversionrate',
                    size: 150,
                }, {
                    header: 'INR Amount',
                    accessorKey: 'inramount',
                    size: 150,
                }, {
                    header: 'Conversion Amount',
                    accessorKey: 'conversionamount',
                    size: 150,
                },
            ], []);

    let data = [];
    props.data.forEach((disbursement) => {
        const filterCases = Object.values(props.casesData).find(filteredCase => filteredCase.caseId === disbursement.caseId);
        const filterClients = Object.values(props.clientsData).find(filteredClient => filteredClient.clientId === disbursement.clientId);
        data.push(
            {
                disbursementid: disbursement.disbursementId,
                casename: filterCases?.caseName || 'N/A',
                clientname: filterClients?.clientName || 'N/A',
                disbursement: disbursement.disbursement,
                date: disbursement.date,
                currencycode: disbursement.currencyCode,
                conversionrate: disbursement.conversionRate?.toFixed(2),
                inramount: disbursement.inrAmount?.toFixed(2),
                conversionamount: disbursement.conversionAmount?.toFixed(2),
            })
    });

    useEffect(() => {
        setTableData(data);
     }, [data])

     const openDeleteConfirmModal = async (row) => {
        if (window.confirm(`Are you sure you want to delete disbursement: ${row.original.disbursement}?`)) {
            await disbursementsApi.delete(row.original.disbursementid);
            props.requestDisbursements('');
        }
    };

    const table = useMaterialReactTable({
        columns: columns,
        data: tableData,
        enableRowActions: true,
        renderRowActions: ({ row, _ }) => (
            row.depth === 0 ? (
              <Box sx={{ display: 'flex', gap: '1rem' }}>
                <Tooltip title="Delete">
                  <IconButton onClick={() => openDeleteConfirmModal(row)}>
                    <DeleteIcon sx={{ color: "#8B0000" }} />
                  </IconButton>
                </Tooltip>
              </Box>
            ) : null
        ),
        renderTopToolbarCustomActions: ({ table }) => (
            <Box
              sx={{
                display: 'flex',
                gap: '16px',
                padding: '8px',
                flexWrap: 'wrap',
              }}
            >
              <Button
                disabled={table.getRowModel().rows.length === 0}
                onClick={() => {
                    const visibleColumns = table.getVisibleLeafColumns();
                    const rows = table.getRowModel().rows;
                    handleExportRows(rows, visibleColumns);
                }}
                startIcon={<FileDownloadIcon sx={{ color: '#fff' }} />}
                sx={{
                  backgroundColor: '#8B0000',
                  color: '#fff',
                  '&:hover': {
                    backgroundColor: '#8B0000',
                  },
                  textTransform: 'uppercase',
                  fontWeight: 600,
                }}
              >
                Export to CSV
              </Button>
            </Box>
        ),
    });

    return (
        <div className={"display-disbursements-table-container"}>
            <MaterialReactTable table={table} />
        </div>
    );
};

DisplayServices.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
    clientsData: PropTypes.array.isRequired,
    casesData: PropTypes.array.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getDisbursementsData(state),
        isLoading: getDisbursementsIsLoading(state),
        clientsData: getClientsData(state),
        casesData: getCasesData(state),

    }
}

const mapDispatchToProps = (dispatch) => ({
    requestDisbursements: (disbursementID) => {
      dispatch(requestDisbursements(disbursementID));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(DisplayServices);