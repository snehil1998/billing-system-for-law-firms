import SERVICES from "./ServicesConstants";
import {requestCases} from "../Cases/CasesActions";
import {requestClients} from "../Clients/ClientsActions";
import {requestAttorneys} from "../Attorneys/AttorneysActions";

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
                .finally(() => {
                    dispatch(requestCases(''));
                    dispatch(requestClients(''));
                    dispatch(requestAttorneys(''));
                    dispatch(addFromSearchDateServices(null));
                    dispatch(addToSearchDateServices(null));
                })
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

export const addFromSearchDateServices = (fromSearchDate) => async (dispatch) => {
    dispatch({
        type: SERVICES.ADD_FROM_SEARCH_DATE,
        fromSearchDate,
    });
};

export const addToSearchDateServices = (toSearchDate) => async (dispatch) => {
    dispatch({
        type: SERVICES.ADD_TO_SEARCH_DATE,
        toSearchDate,
    });
};

export const addFilterCheckboxesServices = (filterCheckboxes) => async (dispatch) => {
    dispatch({
        type: SERVICES.ADD_FILTER_CHECKBOXES,
        filterCheckboxes,
    });
};