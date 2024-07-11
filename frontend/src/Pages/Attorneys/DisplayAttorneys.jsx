import React, {useEffect, useMemo, useState} from "react";
import {connect, useDispatch} from "react-redux";
import PropTypes from 'prop-types';
import {requestAttorneys} from "../../Redux/Attorneys/AttorneysActions";
import {getAttorneysData, getAttorneysIsLoading} from "../../Redux/Attorneys/AttorneysSelectors";
import {getClientsData} from "../../Redux/Clients/ClientsSelectors";
import {clearMessage} from "../../Redux/Message/MessageActions";
import {
    MaterialReactTable,
    useMaterialReactTable,
  } from 'material-react-table';

const DisplayAttorneys = (props) => {
    const dispatch = useDispatch();
    const [tableData, setTableData] = useState([]);
    useEffect(() => {
        dispatch(clearMessage());
        dispatch(requestAttorneys(''));
    }, [dispatch]);

    const columns = useMemo(
        () => [
            {
                header: 'Attorneys',
                columns: [
                    {
                        header: 'Attorney ID',
                        accessorKey: 'attorneyid',
                    }, {
                        header: 'First Name',
                        accessorKey: 'firstname',
                    }, {
                        header: 'Last Name',
                        accessorKey: 'lastname',
                    },
                ]},
            {
                header: 'Service Pricing',
                columns: [
                    {
                        header: 'Client Name',
                        accessorKey: 'clientname',

                    },
                    {
                        header: 'Currency Code',
                        accessorKey: 'currencycode',

                    },
                    {
                        header: 'Price',
                        accessorKey: 'price',
                    },
                ]
            }
        ], []);

    let data = [];
    props.data.forEach((attorney) => {
        const price = [];
        const clientName = [];
        const currencyCode = [];
        attorney.servicePricing?.forEach(pricing => {
            clientName.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.clientName || 'N/A');
            price.push(pricing.price.toFixed(2));
            currencyCode.push(props.clientsData.find(client => client.clientId ===
                pricing.clientId)?.currencyCode || 'N/A')
        })

        data.push(
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

    useEffect(() => {
        setTableData(data);
     }, [data])

    const table = useMaterialReactTable({
        columns,
        data: tableData,
        enableExpandAll: true,
        enableExpanding: true,
        getSubRows: (row) => row.subRows,
        initialState: { expanded: false },
        paginateExpandedRows: true,
      });

    return (
        <div className={"display-attorneys-table-container"}>
            <MaterialReactTable table={table} />
        </div>
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