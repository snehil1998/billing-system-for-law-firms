import ATTORNEYS from "./AttorneysConstants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
};

const AttorneysReducer = (state = initalState, action) => {
    switch (action.type) {
        case ATTORNEYS.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case ATTORNEYS.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
            };
        case ATTORNEYS.POST_SUCCESS:
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

export default AttorneysReducer;