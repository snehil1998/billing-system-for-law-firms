import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {requestClients} from "../../Redux/Clients/ClientsActions";
import {getClientsData, getClientsIsLoading} from "../../Redux/Clients/ClientsSelectors";
import AddClient from "../../Components/AddClient";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {Page} from "../../Components/PagesEnum";


const DisplayClients = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(clearMessage());
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
                        Header: 'Disbursements Amount',
                        accessor: 'disbursementsamount',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Services Amount',
                        accessor: 'servicesamount',
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
                disbursementsamount: client.disbursementsAmount.toFixed(2),
                servicesamount: client.servicesAmount.toFixed(2),
                amount: client.amount.toFixed(2),
            })
    });

    return (
        <>
            <div className={"display-clients-table-container"}>
                <div id={'display-clients-table'} className={"table"}>
                    <AddClient/>
                    <Table columns={columns} data={tableData} type={Page.CLIENTS}
                           filterByColumn={'clientid'} isDescending={false} />
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