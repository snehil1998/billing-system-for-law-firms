import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {requestAttorneys} from "../../Redux/Attorneys/AttorneysActions";
import {getAttorneysData, getAttorneysIsLoading} from "../../Redux/Attorneys/AttorneysSelectors";
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import AddAttorney from "../../Components/AddAttorney";
import AddServicePricing from "../../Components/AddServicePricing";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {Page} from "../../Components/PagesEnum";
import DeleteServicePricing from "../../Components/DeleteServicePricing";

const DisplayAttorneys = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestAttorneys(''));
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
                Header: 'Attorneys',
                columns: [
                    {
                        Header: 'Attorney ID',
                        accessor: 'attorneyid',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'First Name',
                        accessor: 'firstname',
                        sortType: "basic",
                        filter: "text"
                    }, {
                        Header: 'Last Name',
                        accessor: 'lastname',
                        sortType: "basic",
                        filter: "text"
                    },
                ]},
            {
                Header: 'Service Pricing',
                columns: [
                    {
                        Header: 'Client Name',
                        accessor: 'clientname',
                        sortType: "basic",
                        filter: "text",

                    },
                    {
                        Header: 'Currency Code',
                        accessor: 'currencycode',
                        sortType: "basic",
                        filter: "text",

                    },
                    {
                        Header: 'Price',
                        accessor: 'price',
                        sortType: "basic",
                        filter: "numeric"
                    },
                ]
            }
        ], []);

    let tableData = [];
    props.data.forEach((attorney) => {
        const price = [];
        const clientName = [];
        const currencyCode = [];
        attorney.servicePricing.forEach(pricing => {
            clientName.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.clientName);
            price.push(pricing.price.toFixed(2));
            currencyCode.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.currencyCode)
        })

        tableData.push(
            {
                attorneyid: attorney.attorneyId,
                firstname: attorney.firstName,
                lastname: attorney.lastName,
                clientname: clientName.toString(),
                currencycode: currencyCode.toString(),
                price: price.toString(),
                subRows: clientName.length <= 1
                    ? null : clientName.map((name, index) => {
                        return {
                            attorneyid: "",
                            firstname: "",
                            lastname: "",
                            clientname: name,
                            currencycode: currencyCode[index],
                            price: price[index],
                        };
                    }),
            })
    });

    return (
        <>
            <div className={"display-attorneys-table-container"}>
                <div id={"display-attorneys-table"} className={"table"}>
                    <AddAttorney/>
                    <AddServicePricing/>
                    <DeleteServicePricing/>
                    <Table columns={columns} data={tableData} type={Page.ATTORNEYS}
                           filterByColumn={'attorneyid'} isDescending={false} />
                </div>
            </div>
        </>
    );
};

DisplayAttorneys.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
    clientsData: PropTypes.array.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getAttorneysData(state),
        isLoading: getAttorneysIsLoading(state),
        clientsData: getClientsData(state),
    }
}

export default connect(mapStateToProps, null)(DisplayAttorneys);