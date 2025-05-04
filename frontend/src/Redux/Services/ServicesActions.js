import SERVICES from "./ServicesConstants";
import {requestCases} from "../cases/CasesActions";
import {requestClients} from "../clients/ClientsActions";
import {requestAttorneys} from "../attorneys/AttorneysActions";
import { servicesApi } from "../../services/api";
export const requestServices = (clientID) => async (dispatch) => {
    dispatch({
        type: SERVICES.LOAD,
    });
    try {
        if(clientID === ''){
            await servicesApi.getAll()
                .then(json => {
                    dispatch({
                        type: SERVICES.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    })})
                .finally(() => {
                    dispatch(requestCases(''));
                    dispatch(requestClients(''));
                    dispatch(requestAttorneys(''));
                    dispatch(addFromSearchDateServices(null));
                    dispatch(addToSearchDateServices(null));
                })
        } else{
            await servicesApi.getByClientId(clientID)
                .then(json =>
                    dispatch({
                        type: SERVICES.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
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