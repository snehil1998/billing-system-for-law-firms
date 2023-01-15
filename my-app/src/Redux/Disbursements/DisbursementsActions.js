  import DISBURSEMENTS from "./DisbursementsConstants";
  import {requestCases} from "../Cases/CasesActions";
  import {requestClients} from "../Clients/ClientsActions";

export const requestDisbursements = (clientID) => async (dispatch) => {
    dispatch({
        type: DISBURSEMENTS.LOAD,
    });
    try {
        if(clientID === ''){
            await fetch("/disbursements")
                .then(response => response.json())
                .then(json => {
                    dispatch(requestCases(''));
                    dispatch(requestClients(''));
                    dispatch({
                        type: DISBURSEMENTS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/disbursements/clients="+clientID)
                .then(response => response.json())
                .then(json =>
                    dispatch({
                        type: DISBURSEMENTS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    }))
        }

    } catch (e) {
        dispatch({
            type: DISBURSEMENTS.LOAD_SUCCESS,
            data: [],
            isError: true,
        });
    }
};