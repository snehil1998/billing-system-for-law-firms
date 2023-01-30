import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {requestCases} from "../../Redux/Cases/CasesActions";
import {getCasesData, getCasesIsLoading} from "../../Redux/Cases/CasesSelectors";
import AddCase from "../../Components/AddCase";
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {Page} from "../../Components/PagesEnum";


const DisplayCases = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestCases(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                Header: 'Cases',
                columns: [
                    {
                        Header: 'Case ID',
                        accessor: 'caseid',
                        sortType: "basic",
                        filter: "text"
                    },
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
    props.data.forEach((Case) => {
        tableData.push(
            {
                caseid: Case.caseId,
                casename: Case.caseName,
                clientname: props.clientsData.find(data => data.clientId === Case.clientId)?.clientName,
                currencycode: Case.currencyCode,
                disbursementsamount: Case.disbursementsAmount.toFixed(2),
                servicesamount: Case.servicesAmount.toFixed(2),
                amount: Case.amount.toFixed(2),
            })
    });

    return (
        <>
            <div className={"display-cases-table-container"}>
                <div id={'display-cases-table'} className={"table"}>
                    <AddCase/>
                    <Table columns={columns} data={tableData} type={Page.CASES}
                           filterByColumn={'caseid'} isDescending={false} />
                </div>
            </div>
        </>
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

export default connect(mapStateToProps, null)(DisplayCases);