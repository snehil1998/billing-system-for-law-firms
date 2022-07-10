import SERVICES from "./Constants";

export const requestServices = (caseID) => async (dispatch) => {
    dispatch({
        type: SERVICES.LOAD,
    });
    try {
        if(caseID === ''){
            await fetch("/services")
                .then(response => response.json())
                .then(json => {
                    dispatch({
                        type: SERVICES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/services/case="+caseID)
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