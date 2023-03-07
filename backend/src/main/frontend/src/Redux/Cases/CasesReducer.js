import CASES from "./CasesConstants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
};

const CasesReducer = (state = initalState, action) => {
    switch (action.type) {
        case CASES.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case CASES.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
            };
        case CASES.POST_SUCCESS:
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

export default CasesReducer;