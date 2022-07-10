import React, {useEffect, useMemo, useState} from "react";
import { useSelector, useDispatch } from "react-redux";
import {requestServices} from "../../Redux/Action";
import Table from "../../Components/Table";
import AddService from "../../Components/AddService";

const DisplayServices = () => {
    const { data, isLoading } = useSelector((state) => state);
    const [ cases, setCases ] = useState("");
    const [ clients, setClients ] = useState("");
    const [attorneys, setAttorneys] = useState("");
    const dispatch = useDispatch();
    const caseID = '';
    useEffect(() => {
        dispatch(requestServices(caseID));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                id: 'expander', // Make sure it has an ID
                Header: ({ getToggleAllRowsExpandedProps, isAllRowsExpanded }) => (
                    <span {...getToggleAllRowsExpandedProps()}>
                        {isAllRowsExpanded ? '👇' : '👉'}
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
                            {row.isExpanded ? '👇' : '👉'}
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
                        Header: 'Description',
                        accessor: 'description',
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
                        Header: 'Minutes',
                        accessor: 'minutes',
                        sortType: "basic",
                        filter: "numeric"
                    }
                ]
            }
    ], []);

    useEffect(() => {
        fetch("/cases")
            .then(response => response.json())
            .then(json => {
                setCases(json);
            })
    }, []);

    useEffect(() => {
        fetch("/clients")
            .then(response => response.json())
            .then(json => {
                setClients(json);
            })
    }, []);

    useEffect(() => {
        fetch("/attorneys")
            .then(response => response.json())
            .then(json => {
                setAttorneys(json);
            })
    }, []);


    let tableData = [];
    data.map((service) => {
        const filterCases = Object.values(cases).filter(filteredCase => filteredCase.caseId === service.caseId);
        const filterClients = Object.values(clients).filter(filteredClient => filteredClient.clientId === service.clientId)
        const filterAttorneys = []
        const minutes = []
        service.attorneys.map(serviceAttorney => {
            filterAttorneys.push(Object.values(attorneys).filter(filteredAttorney => filteredAttorney.attorneyId ===
                serviceAttorney.id));
            minutes.push(serviceAttorney.minutes);
            return 0;
        })
        const attorneyNamesList = []
            filterAttorneys.map(attorney => attorneyNamesList.push(attorney[0]?.firstName + " " + attorney[0]?.lastName))
        tableData.push(
            {
                serviceid: service.serviceId,
                casename: filterCases[0]?.caseName,
                clientname: filterClients[0]?.clientName,
                service: service.service,
                description: service.description,
                date: service.date,
                attorneys: attorneyNamesList.toString(),
                minutes: minutes.toString(),
                amount: service.amount,
                subRows: attorneyNamesList.length <= 1
                    ? null : attorneyNamesList.map((name, index) => {
                    return {
                        serviceid: "",
                        casename: "",
                        clientname: "",
                        service: "",
                        description: "",
                        date: "",
                        attorneys: name,
                        minutes: minutes[index],
                        amount: "",
                    };
                }),
            })
        return 0;
    });

    return (
        <>
        {isLoading && <div className="loading">Data loading...</div>}
            <div className={"display table container"}>
                <div className={"table container"} style={{textAlign:'left'}}>
                    <AddService/>
                    <Table columns={columns} data={tableData} type={'services'} />
                </div>
            </div>
        </>
    );
};

export default DisplayServices;