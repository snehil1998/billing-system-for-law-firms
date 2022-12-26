import SERVICES from "./Constants";

export const requestServices = (clientID) => async (dispatch) => {
    dispatch({
        type: SERVICES.LOAD,
    });
    try {
        if(clientID === ''){
            await fetch("/services")
                .then(response => response.json())
                .then(json => {
                    dispatch({
                        type: SERVICES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/services/client="+clientID)
                .then(response => response.json())
                .then(json =>
                    dispatch({
                        type: SERVICES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    }))
        }

    } catch (e) {
        dispatch({
            type: SERVICES.LOAD_SUCCESS,
            data: [],
            isError: true,
        });
    }
};

export const addFromSearchDate = (fromSearchDate) => async (dispatch) => {
    dispatch({
        type: SERVICES.ADD_FROM_SEARCH_DATE,
        fromSearchDate,
    });
};

export const addToSearchDate = (toSearchDate) => async (dispatch) => {
    dispatch({
        type: SERVICES.ADD_TO_SEARCH_DATE,
        toSearchDate,
    });
};