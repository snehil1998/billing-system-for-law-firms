  import DISBURSEMENTS from "./DisbursementsConstants";
  import {requestCases} from "../cases/CasesActions";
  import {requestClients} from "../clients/ClientsActions";
  import { disbursementsApi } from "../../services/api";
export const requestDisbursements = (clientID) => async (dispatch) => {
    dispatch({
        type: DISBURSEMENTS.LOAD,
    });
    try {
        if(clientID === ''){
            await disbursementsApi.getAll()
                .then(json => {
                    dispatch({
                        type: DISBURSEMENTS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
                    })})
                .finally( () => {
                    dispatch(requestClients(''));
                    dispatch(requestCases(''));
                    dispatch(addFromSearchDateDisbursements(null));
                    dispatch(addToSearchDateDisbursements(null));
                })
        } else{
            await disbursementsApi.getByClientId(clientID)
                .then(json =>
                    dispatch({
                        type: DISBURSEMENTS.LOAD_SUCCESS,
                        data: json.data,
                        isError: json.success,
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

export const addFromSearchDateDisbursements = (fromSearchDate) => async (dispatch) => {
  dispatch({
      type: DISBURSEMENTS.ADD_FROM_SEARCH_DATE,
      fromSearchDate,
  });
};

export const addToSearchDateDisbursements = (toSearchDate) => async (dispatch) => {
  dispatch({
      type: DISBURSEMENTS.ADD_TO_SEARCH_DATE,
      toSearchDate,
  });
};

export const addFilterCheckboxesDisbursements = (filterCheckboxes) => async (dispatch) => {
  dispatch({
      type: DISBURSEMENTS.ADD_FILTER_CHECKBOXES,
      filterCheckboxes,
  });
};