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

    const table = useMaterialReactTable({
        columns: columns,
        data: tableData
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

export default connect(mapStateToProps, null)(DisplayServices);