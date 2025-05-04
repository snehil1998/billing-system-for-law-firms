import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {requestClients} from "../../redux/clients/ClientsActions";
import {getClientsData, getClientsIsLoading} from "../../redux/clients/ClientsSelectors";
import {clearMessage} from "../../redux/message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';
import { Box, Button, IconButton, Tooltip } from '@mui/material';
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import DeleteIcon from '@mui/icons-material/Delete';
import { clientsApi } from "../../services/api";
import { handleExportRows } from "../../components/common/CsvExporter";
const DisplayClients = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    let data = [];
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestClients(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
                {
                    header: 'Client ID',
                    accessorKey: 'clientid',
                    size: 150,
                },
                {
                    header: 'Client Name',
                    accessorKey: 'clientname',
                    size: 300,
                }, {
                    header: 'Currency Code',
                    accessorKey: 'currencycode',
                    size: 150,
                }, {
                    header: 'Disbursements Amount',
                    accessorKey: 'disbursementsamount',
                    size: 200,
                }, {
                    header: 'Services Amount',
                    accessorKey: 'servicesamount',
                    size: 200,
                }, {
                    header: 'Amount',
                    accessorKey: 'amount',
                    size: 200,
                },
            ], []);

    props.data.forEach((client) => {
        data.push(
            {
                clientid: client.clientId,
                clientname: client.clientName,
                currencycode: client.currencyCode,
                disbursementsamount: client.disbursementsAmount?.toFixed(2),
                servicesamount: client.servicesAmount?.toFixed(2),
                amount: client.amount?.toFixed(2),
            })
    });

    useEffect(() => {
        setTableData(data);
     }, [data])

    const openDeleteConfirmModal = async (row) => {
        if (window.confirm(`Are you sure you want to delete client: ${row.original.clientname}?`)) {
            await clientsApi.delete(row.original.clientid);
            props.requestClients('');
        }
    };

    const table = useMaterialReactTable({
        columns: columns,
        data: tableData,
        enableRowActions: true,
        enablePagination: true,
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
                disabled={table.getPrePaginationRowModel().rows.length === 0}
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
        <div className={"display-clients-table-container"}>
            <MaterialReactTable table={table} />
        </div>
    );
};

DisplayClients.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getClientsData(state),
        isLoading: getClientsIsLoading(state),
    }
}

const mapDispatchToProps = (dispatch) => ({
    requestClients: (clientID) => {
      dispatch(requestClients(clientID));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(DisplayClients);