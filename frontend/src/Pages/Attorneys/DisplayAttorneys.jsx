import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {requestAttorneys} from "../../redux/attorneys/AttorneysActions";
import {getAttorneysData, getAttorneysIsLoading} from "../../redux/attorneys/AttorneysSelectors";
import {getClientsData} from "../../redux/clients/ClientsSelectors";
import {clearMessage} from "../../redux/message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';
import { Box, Button, IconButton, Tooltip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { attorneysApi } from "../../services/api";
import { handleExportRows } from "../../components/common/CsvExporter";
import FileDownloadIcon from '@mui/icons-material/FileDownload';

const DisplayAttorneys = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestAttorneys(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                header: 'Attorneys',
                columns: [
                    {
                        header: 'Attorney ID',
                        accessorKey: 'attorneyid',
                    }, {
                        header: 'First Name',
                        accessorKey: 'firstname',
                    }, {
                        header: 'Last Name',
                        accessorKey: 'lastname',
                    },
                ]},
            {
                header: 'Service Pricing',
                columns: [
                    {
                        header: 'Client Name',
                        accessorKey: 'clientname',

                    },
                    {
                        header: 'Currency Code',
                        accessorKey: 'currencycode',

                    },
                    {
                        header: 'Price',
                        accessorKey: 'price',
                    },
                ]
            }
        ], []);

    let data = [];
    props.data.forEach((attorney) => {
        const price = [];
        const clientName = [];
        const currencyCode = [];
        attorney.servicePricing?.forEach(pricing => {
            clientName.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.clientName || 'N/A');
            price.push(pricing.price.toFixed(2));
            currencyCode.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.currencyCode || 'N/A')
        })

        data.push(
            {
                attorneyid: attorney.attorneyId,
                firstname: attorney.firstName,
                lastname: attorney.lastName,
                clientname: clientName.toString(),
                currencycode: currencyCode.toString(),
                price: price.toString(),
                subRows: clientName.length <= 1
                ? null : clientName.map((name, index) => {
                        return {
                            attorneyid: "",
                            firstname: "",
                            lastname: "",
                            clientname: name,
                            currencycode: currencyCode[index],
                            price: price[index],
                        };
                    }),
            })
    });

    useEffect(() => {
        setTableData(data);
     }, [data])

    const openDeleteConfirmModal = async (row) => {
        if (window.confirm(`Are you sure you want to delete attorney: ${row.original.firstname} ${row.original.lastname}?`)) {
            await attorneysApi.delete(row.original.attorneyid);
            props.requestAttorneys('');
        }
    };

    const table = useMaterialReactTable({
        columns,
        data: tableData,
        enableExpandAll: true,
        enableExpanding: true,
        getSubRows: (row) => row.subRows,
        initialState: { expanded: false },
        paginateExpandedRows: false,
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
        <div className={"display-attorneys-table-container"}>
            <MaterialReactTable table={table} />
        </div>
    );
};

DisplayAttorneys.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
    clientsData: PropTypes.array.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getAttorneysData(state),
        isLoading: getAttorneysIsLoading(state),
        clientsData: getClientsData(state),
    }
}

const mapDispatchToProps = (dispatch) => ({
    requestAttorneys: (attorneyID) => {
      dispatch(requestAttorneys(attorneyID));
    }
});

export default connect(mapStateToProps, mapDispatchToProps)(DisplayAttorneys);