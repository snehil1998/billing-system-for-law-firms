import CASES from "./CasesConstants";

export const requestCases = (casesID) => async (dispatch) => {
    dispatch({
        type: CASES.LOAD,
    });
    try {
        if(casesID === ''){
            await fetch("/cases")
                .then(response => response.json())
                .then(json => {
                    dispatch({
                        type: CASES.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
        } else{
            await fetch("/cases="+casesID)
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