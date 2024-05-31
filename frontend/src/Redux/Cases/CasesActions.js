import CASES from "./CasesConstants";
import {requestClients} from "../Clients/ClientsActions";

export const requestCases = (casesID) => async (dispatch) => {
    dispatch({
        type: CASES.LOAD,
    });
    try {
        if(casesID === ''){
            await fetch("/backend/cases")
                .then(response => response.json())
                .then(json => {
                    dispatch({
                        type: CASES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
                .finally(() => {
                    dispatch(requestClients(''));
                })
        } else{
            await fetch("/backend/cases="+casesID)
                .then(response => response.json())
                .then(json =>
                    dispatch({
                        type: CASES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    }))
        }

    } catch (e) {
        dispatch({
            type: CASES.LOAD_SUCCESS,
            data: [],
            isError: true,
        });
    }
};