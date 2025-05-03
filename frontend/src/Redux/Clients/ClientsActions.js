import CLIENTS from "./ClientsConstants";
import { clientsApi } from "../../services/api";
export const requestClients = (clientID) => async (dispatch) => {
    dispatch({
        type: CLIENTS.LOAD,
    });
    try {
        if(clientID === ''){
            await clientsApi.getAll()
                .then(json => {
                    dispatch({
                        type: CLIENTS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    })})
        } else{
            await clientsApi.getById(clientID)
                .then(json =>
                    dispatch({
                        type: CLIENTS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    }))
        }

    } catch (e) {
        dispatch({
            type: CLIENTS.LOAD_SUCCESS,
            data: [],
            isError: true,
        });
    }
};