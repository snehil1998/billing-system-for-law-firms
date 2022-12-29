import React, {useEffect, useMemo} from "react";
import {connect, useDispatch} from "react-redux";
import Table from "../../Components/Table";
import PropTypes from 'prop-types';
import {requestCases} from "../../Redux/Cases/CasesActions";
import {getCasesData, getCasesIsLoading} from "../../Redux/Cases/CasesSelectors";
import AddCase from "../../Components/AddCase";

const DisplayCases = (props) => {
    const dispatch = useDispatch();
    useEffect(() => {
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
                        Header: 'Currency Code',
                        accessor: 'currencycode',
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
                currencycode: Case.currencyCode,
                amount: Case.amount,
            })
    });

    return (
        <>
            {/*{props.isLoading && <div className="loading">Data loading...</div>}*/}
            <div className={"display table container"}>
                <div className={"table container"} style={{textAlign:'left'}}>
                    <AddCase/>
                    <Table columns={columns} data={tableData} type={'casesdispla'} />
                </div>
            </div>
        </>
    );
};

DisplayCases.propTypes = {
    data: PropTypes.array.isRequired,
    isLoading: PropTypes.bool.isRequired,
}

const mapStateToProps = state => {
    return {
        data: getCasesData(state),
        isLoading: getCasesIsLoading(state),
    }
}

export default connect(mapStateToProps, null)(DisplayCases);