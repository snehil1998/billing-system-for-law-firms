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
                    dispatch({
                        type: DISBURSEMENTS.LOAD_SUCCESS,
                        data: json,
                        isError: false,
                    })})
                .finally( () => {
                    dispatch(requestClients(''));
                    dispatch(requestCases(''));
                    dispatch(addFromSearchDateDisbursements(null));
                    dispatch(addToSearchDateDisbursements(null));
                })
        } else{
            await fetch("/disbursements/client="+clientID)
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