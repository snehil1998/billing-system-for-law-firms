import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../../Redux/Cases/CasesSelectors";
import {requestDisbursements} from "../../Redux/Disbursements/DisbursementsActions";
import {getDisbursementsData, getDisbursementsIsLoading} from "../../Redux/Disbursements/DisbursementsSelectors";
import AddDisbursement from "../../Components/AddDisbursement";

const DisplayServices = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
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
        const filterCases = Object.values(props.casesData).filter(filteredCase => filteredCase.caseId === disbursement.caseId);
        const filterClients = Object.values(props.clientsData).filter(filteredClient => filteredClient.clientId === disbursement.clientId)
        tableData.push(
            {
                disbursementid: disbursement.disbursementId,
                casename: filterCases[0]?.caseName,
                clientname: filterClients[0]?.clientName,
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
        {/*{props.isLoading && <div className="loading">Data loading...</div>}*/}
            <div className={"display table container"} style={{backgroundColor:'maroon'}}>
                <div className={"table container"} style={{textAlign:'left'}}>
                    <AddDisbursement/>
                    <Table columns={columns} data={tableData} type={'disbursements'} />
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