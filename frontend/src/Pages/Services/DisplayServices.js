import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import {requestServices} from "../../Redux/Services/ServicesActions";
import AddService from "../../Components/AddServices/AddService";
import {getFilteredServiceData, getServiceIsLoading} from "../../Redux/Services/ServicesSelectors";
import PropTypes from 'prop-types';
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../../Redux/Cases/CasesSelectors";
import {getAttorneysData} from "../../Redux/Attorneys/AttorneysSelectors";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';

const DisplayServices = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestServices(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                header: 'Services',
                columns: [
                    {
                        header: 'Case Name',
                        accessorKey: 'casename',
                    }, {
                        header: 'Client Name',
                        accessorKey: 'clientname',
                    }, {
                        header: 'Service',
                        accessorKey: 'service',
                    }, {
                        header: 'Date',
                        accessorKey: 'date',
                    }, {
                        header: 'Currency Code',
                        accessorKey: 'currencycode',

                    }, {
                        header: 'Amount',
                        accessorKey: 'amount',
                    },
                ]
            },
            {
                header: 'Attorneys',
                columns: [
                    {
                        header: 'Name',
                        accessorKey: 'attorneys',

                    },
                    {
                        header: 'Time Spent (in Minutes)',
                        accessorKey: 'minutes',
                    },
                    {
                        header: 'Time Spent (in hours)',
                        accessorKey: 'hours',
                    },
                    {
                        header: 'Rate per hour',
                        accessorKey: 'pricing',
                    },
                    {
                        header: 'Total',
                        accessorKey: 'total',
                    }
                ]
            }
    ], []);

    let data = [];
    props.data.forEach((service) => {
        const filterCases = Object.values(props.casesData).filter(filteredCase => filteredCase.caseId === service.caseId);
        const filterClients = Object.values(props.clientsData).filter(filteredClient => filteredClient.clientId === service.clientId)
        const filterAttorneys = []
        const minutes = []
        const hours = []
        const total = []
        service.attorneys?.forEach(serviceAttorney => {
            filterAttorneys.push(Object.values(props.attorneysData).filter(filteredAttorney => filteredAttorney.attorneyId ===
                serviceAttorney.id));
            minutes.push(serviceAttorney.minutes);
        })
        
        const attorneyNamesList = []
        const attorneyPricingList = []
        filterAttorneys.forEach(attorney => {
            attorneyNamesList.push((attorney[0]?.firstName || 'N/A') + " " + (attorney[0]?.lastName || ''))
            attorney?.[0]?.servicePricing !== undefined ?
                attorney?.[0]?.servicePricing.filter(price => price.clientId === service.clientId)
            .forEach(price => attorneyPricingList.push(price.price.toFixed(2))) :
                attorneyPricingList.push('N/A')
        })
        
        minutes.forEach(minute => hours.push((minute/60.0).toFixed(2)))
        hours.forEach((hour, index) => total.push((hour * attorneyPricingList[index]).toFixed(2)))
        
        data.push(
            {
                serviceid: service.serviceId,
                casename: filterCases[0]?.caseName,
                clientname: filterClients[0]?.clientName,
                service: service.service,
                date: service.date,
                currencycode: filterClients[0]?.currencyCode,
                attorneys: attorneyNamesList?.toString(),
                minutes: minutes.toString(),
                hours: hours.toString(),
                amount: service.amount?.toFixed(2),
                pricing: attorneyPricingList?.toString(),
                total: total?.toString(),
                subRows: attorneyNamesList.length <= 1
                    ? null : attorneyNamesList.map((name, index) => {
                    return {
                        serviceid: "",
                        casename: "",
                        clientname: "",
                        service: "",
                        date: "",
                        currencycode: "",
                        attorneys: name,
                        minutes: minutes[index],
                        hours: hours[index],
                        amount: "",
                        pricing: attorneyPricingList[index],
                        total: total[index]
                    };
                }),
            })
    });

    useEffect(() => {
        setTableData(data);
     }, [data])

    const table = useMaterialReactTable({
        columns,
        data: tableData,
        enableExpandAll: true,
        enableExpanding: true,
        filterFromLeafRows: true, 
        getSubRows: (row) => row.subRows,
        initialState: { expanded: true }, 
        paginateExpandedRows: false,
      });

    return (
        <div className={"display-services-table-container"}>
            <MaterialReactTable table={table} />
        </div>
    );
};

DisplayServices.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
    clientsData: PropTypes.array.isRequired,
    casesData: PropTypes.array.isRequired,
    attorneysData: PropTypes.array.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getFilteredServiceData(state),
        isLoading: getServiceIsLoading(state),
        clientsData: getClientsData(state),
        casesData: getCasesData(state),
        attorneysData: getAttorneysData(state),

    }
}

export default connect(mapStateToProps, null)(DisplayServices);