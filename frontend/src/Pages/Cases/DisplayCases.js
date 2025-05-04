import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {requestCases} from "../../redux/cases/CasesActions";
import {getCasesData, getCasesIsLoading} from "../../redux/cases/CasesSelectors";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {clearMessage} from "../../redux/message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';
import { Box, Button, IconButton, Tooltip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { casesApi } from "../../services/api";
import { handleExportRows } from "../../components/common/CsvExporter";
import FileDownloadIcon from '@mui/icons-material/FileDownload';

const DisplayCases = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    let data = [];
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestCases(''));
    }, [dispatch]);

        const newcols = useMemo(
            () => [
                    {
                        header: 'Case ID',
                        accessorKey: 'caseid',
                        size: 150
                    },
                    {
                        header: 'Case Name',
                        accessorKey: 'casename',
                        size: 300
                    }, {
                        header: 'Client Name',
                        accessorKey: 'clientname',
                        size: 200
                    }, {
                        header: 'Currency Code',
                        accessorKey: 'currencycode',
                        size: 150
                    }, {
                        header: 'Disbursements Amount',
                        accessorKey: 'disbursementsamount',
                        size: 150
                    }, {
                        header: 'Services Amount',
                        accessorKey: 'servicesamount',
                        size: 150
                    }, {
                        header: 'Amount',
                        accessorKey: 'amount',
                        size: 150
                    },
                ], []);

    props.data.forEach((Case) => {
        data.push(
            {
                caseid: Case.caseId,
                casename: Case.caseName,
                clientname: props.clientsData.find(data => data.clientId === Case.clientId)?.clientName || 'N/A',
                currencycode: Case.currencyCode,
                disbursementsamount: Case.disbursementsAmount?.toFixed(2),
                servicesamount: Case.servicesAmount?.toFixed(2),
                amount: Case.amount?.toFixed(2),
            })
    });

    useEffect(() => {
        setTableData(data);
     }, [data])

    const openDeleteConfirmModal = async (row) => {
        if (window.confirm(`Are you sure you want to delete case: ${row.original.casename}?`)) {
            await casesApi.delete(row.original.caseid);
            props.requestCases('');
        }
    };

    const table = useMaterialReactTable({
        columns: newcols,
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
        <div className={"display-cases-table-container"}>
            <MaterialReactTable table={table} />
        </div>
    );
};

DisplayCases.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
    clientsData: PropTypes.array.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getCasesData(state),
        isLoading: getCasesIsLoading(state),
        clientsData: getClientsData(state),
    }
}

const mapDispatchToProps = (dispatch) => ({
    requestCases: (caseID) => {
      dispatch(requestCases(caseID));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(DisplayCases);