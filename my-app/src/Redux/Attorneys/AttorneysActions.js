import ATTORNEYS from "./AttorneysConstants";
import {requestClients} from "../Clients/ClientsActions";

export const requestAttorneys = (attorneyID) => async (dispatch) => {
    dispatch({
        type: ATTORNEYS.LOAD,
    });
    try {
        if(attorneyID === ''){
            await fetch("/attorneys")
                .then(response => response.json())
                .then(json => {
                    dispatch(requestClients(''))
                    dispatch({
                        type: ATTORNEYS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/attorneys="+attorneyID)
                .then(response => response.json())
                .then(json =>
                    dispatch({
                        type: ATTORNEYS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
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