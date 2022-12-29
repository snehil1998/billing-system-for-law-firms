import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import {requestServices} from "../../Redux/Services/ServicesActions";
import Table from "../../Components/Table";
import AddService from "../../Components/AddService";
import {getFilteredServiceData, getServiceIsLoading} from "../../Redux/Services/ServicesSelectors";
import PropTypes from 'prop-types';
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {getCasesData} from "../../Redux/Cases/CasesSelectors";
import {getAttorneysData} from "../../Redux/Attorneys/AttorneysSelectors";

const DisplayServices = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(requestServices(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                id: 'expander', // Make sure it has an ID
                Header: ({ getToggleAllRowsExpandedProps, isAllRowsExpanded }) => (
                    <span {...getToggleAllRowsExpandedProps()}>
                        {isAllRowsExpanded ? '⬇️' : '➡️'}
                    </span>
                ),
                Cell: ({ row }) =>
                    row.canExpand ? (
                        <span
                            {...row.getToggleRowExpandedProps({
                                style: {
                                    paddingLeft: `${row.depth * 2}rem`,
                                },
                            })}
                        >
                            {row.isExpanded ? '⬇️' : '➡️'}
                        </span>
                    ) : null,
            },
            {
                Header: 'Services',
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
                        Header: 'Service',
                        accessor: 'service',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Date',
                        accessor: 'date',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Amount',
                        accessor: 'amount',
                        sortType: "basic",
                        filter: "text"
                    },
            ]},
            {
                Header: 'Attorneys',
                columns: [
                    {
                        Header: 'Name',
                        accessor: 'attorneys',
                        sortType: "basic",
                        filter: "text",

                    },
                    {
                        Header: 'Time Spent (in Minutes)',
                        accessor: 'minutes',
                        sortType: "basic",
                        filter: "numeric"
                    },
                    {
                        Header: 'Time Spent (in hours)',
                        accessor: 'hours',
                        sortType: "basic",
                        filter: "numeric"
                    },
                    {
                        Header: 'Rate per hour',
                        accessor: 'pricing',
                        sortType: "basic",
                        filter: "numeric"
                    },
                    {
                        Header: 'Total',
                        accessor: 'total',
                        sortType: "basic",
                        filter: "numeric"
                    }
                ]
            }
    ], []);

    let tableData = [];
    props.data.forEach((service) => {
        const filterCases = Object.values(props.casesData).filter(filteredCase => filteredCase.caseId === service.caseId);
        const filterClients = Object.values(props.clientsData).filter(filteredClient => filteredClient.clientId === service.clientId)
        const filterAttorneys = []
        const minutes = []
        const hours = []
        const total = []
        service.attorneys.map(serviceAttorney => {
            filterAttorneys.push(Object.values(props.attorneysData).filter(filteredAttorney => filteredAttorney.attorneyId ===
                serviceAttorney.id));
            minutes.push(serviceAttorney.minutes);
            return 0;
        })
        
        const attorneyNamesList = []
        const attorneyPricingList = []
        filterAttorneys.forEach(attorney => {
            attorneyNamesList.push(attorney[0]?.firstName + " " + attorney[0]?.lastName)
            attorney[0]?.servicePricing.filter(price => price.clientId === service.clientId)
                .forEach(price => attorneyPricingList.push(price.price))
        })
        
        minutes.forEach(minute => hours.push((minute/60.0).toFixed(2)))
        hours.forEach((hour, index) => total.push((hour * attorneyPricingList[index]).toFixed(2)))
        
        tableData.push(
            {
                serviceid: service.serviceId,
                casename: filterCases[0]?.caseName,
                clientname: filterClients[0]?.clientName,
                service: service.service,
                date: service.date,
                attorneys: attorneyNamesList.toString(),
                minutes: minutes.toString(),
                hours: hours.toString(),
                amount: service.amount.toFixed(2),
                pricing: attorneyPricingList.toString(),
                total: total.toString(),
                subRows: attorneyNamesList.length <= 1
                    ? null : attorneyNamesList.map((name, index) => {
                    return {
                        serviceid: "",
                        casename: "",
                        clientname: "",
                        service: "",
                        date: "",
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

    return (
        <>
        {/*{props.isLoading && <div className="loading">Data loading...</div>}*/}
            <div className={"display table container"} style={{backgroundColor:'maroon'}}>
                <div className={"table container"} style={{textAlign:'left'}}>
                    <AddService/>
                    <Table columns={columns} data={tableData} type={'services'} />
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