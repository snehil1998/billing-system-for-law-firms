import ATTORNEYS from "./AttorneysConstants";
import {requestClients} from "../clients/ClientsActions";
import { attorneysApi } from "../../services/api";
export const requestAttorneys = (attorneyID) => async (dispatch) => {
    dispatch({
        type: ATTORNEYS.LOAD,
    });
    try {
        if(attorneyID === ''){
            await attorneysApi.getAll()
                .then(json => {
                    dispatch({
                        type: ATTORNEYS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    })})
                .finally(() => {
                    dispatch(requestClients(''))
                })
        } else{
            await attorneysApi.getById(attorneyID)
                .then(json =>
                    dispatch({
                        type: ATTORNEYS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    }))
        }

    } catch (e) {
        dispatch({
            type: ATTORNEYS.LOAD_SUCCESS,
            data: [],
            isError: true,
        });
    }
};