import CASES from "./CasesConstants";
import {requestClients} from "../clients/ClientsActions";
import { casesApi } from "../../services/api";
export const requestCases = (casesID) => async (dispatch) => {
    dispatch({
        type: CASES.LOAD,
    });
    try {
        if(casesID === ''){
            await casesApi.getAll()
                .then(json => {
                    dispatch({
                        type: CASES.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    })})
                .finally(() => {
                    dispatch(requestClients(''));
                })
        } else{
            await casesApi.getById(casesID)
                .then(json =>
                    dispatch({
                        type: CASES.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
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