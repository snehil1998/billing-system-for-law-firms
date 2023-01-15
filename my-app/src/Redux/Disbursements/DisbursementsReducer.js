import DISBURSEMENTS from "./DisbursementsConstants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
};

const DisbursementsReducer = (state = initalState, action) => {
    switch (action.type) {
        case DISBURSEMENTS.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case DISBURSEMENTS.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
            };
        case DISBURSEMENTS.POST_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
                isError: false
            };
        default:
            return state;
    }
};

export default DisbursementsReducer;