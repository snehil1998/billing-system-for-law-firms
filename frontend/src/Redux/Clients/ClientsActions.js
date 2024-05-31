import CLIENTS from "./ClientsConstants";

export const requestClients = (clientID) => async (dispatch) => {
    dispatch({
        type: CLIENTS.LOAD,
    });
    try {
        if(clientID === ''){
            await fetch("/backend/clients")
                .then(response => response.json())
                .then(json => {
                    dispatch({
                        type: CLIENTS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/backend/clients="+clientID)
                .then(response => response.json())
                .then(json =>
                    dispatch({
                        type: CLIENTS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
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