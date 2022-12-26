import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {requestClients} from "../../Redux/Clients/ClientsActions";
import {getClientsData, getClientsIsLoading} from "../../Redux/Clients/ClientsSelectors";
import AddClient from "../../Components/AddClient";

const DisplayClients = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(requestClients(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                Header: 'Clients',
                columns: [
                    {
                        Header: 'Client ID',
                        accessor: 'clientid',
                        sortType: "basic",
                        filter: "text"
                    },
                    {
                        Header: 'Client Name',
                        accessor: 'clientname',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Currency Code',
                        accessor: 'currencycode',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Amount',
                        accessor: 'amount',
                        sortType: "basic",
                        filter: "text"
                    },
                ]
            },
        ], []);

    let tableData = [];
    props.data.forEach((client) => {
        tableData.push(
            {
                clientid: client.clientId,
                clientname: client.clientName,
                currencycode: client.currencyCode,
                amount: client.amount,
            })
    });

    return (
        <>
            {props.isLoading && <div className="loading">Data loading...</div>}
            <div className={"display table container"}>
                <div className={"table container"} style={{textAlign:'left'}}>
                    <AddClient/>
                    <Table columns={columns} data={tableData} type={'clients'} />
                </div>
            </div>
        </>
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