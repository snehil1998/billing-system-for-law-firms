import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {requestClients} from "../../Redux/Clients/ClientsActions";
import {getClientsData, getClientsIsLoading} from "../../Redux/Clients/ClientsSelectors";
import AddClient from "../../Components/AddClients/AddClient";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';

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

    const table = useMaterialReactTable({
        columns: columns,
        data: tableData
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

export default connect(mapStateToProps, null)(DisplayClients);