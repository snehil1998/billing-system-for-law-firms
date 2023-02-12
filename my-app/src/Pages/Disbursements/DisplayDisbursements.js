import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../../Redux/Cases/CasesSelectors";
import {requestDisbursements} from "../../Redux/Disbursements/DisbursementsActions";
import {getDisbursementsData, getDisbursementsIsLoading} from "../../Redux/Disbursements/DisbursementsSelectors";
import AddDisbursement from "../../Components/AddDisbursement";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {Page} from "../../Components/PagesEnum";

const DisplayServices = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestDisbursements(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                Header: 'Disbursements',
                columns: [
                    {
                        Header: 'Case Name',
                        accessor: 'casename',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Client Name',
                        accessor: 'clientname',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Disbursement',
                        accessor: 'disbursement',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Date',
                        accessor: 'date',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Currency Code',
                        accessor: 'currencycode',
                        sortType: "basic",
                        filter: "text",

                    }, {
                        Header: 'Conversion Rate',
                        accessor: 'conversionrate',
                        sortType: "basic",
                        filter: "numeric"
                    }, {
                        Header: 'INR Amount',
                        accessor: 'inramount',
                        sortType: "basic",
                        filter: "numeric"
                    }, {
                        Header: 'Conversion Amount',
                        accessor: 'conversionamount',
                        sortType: "basic",
                        filter: "numeric"
                    },
            ]},
    ], []);

    let tableData = [];
    props.data.forEach((disbursement) => {
        const filterCases = Object.values(props.casesData).find(filteredCase => filteredCase.caseId === disbursement.caseId);
        const filterClients = Object.values(props.clientsData).find(filteredClient => filteredClient.clientId === disbursement.clientId);
        tableData.push(
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

    return (
        <>
            <div className={"display-disbursements-table-container"}>
                <div id={'display-disbursements-table'} className={"table"}>
                    <AddDisbursement/>
                    <Table columns={columns} data={tableData} type={Page.DISBURSEMENTS}
                           filterByColumn={'date'} isDescending={false} />
                </div>
            </div>
        </>
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

export default connect(mapStateToProps, null)(DisplayServices);