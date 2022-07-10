import SERVICES from "./Constants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
};

const Reducer = (state = initalState, action) => {
    switch (action.type) {
        case SERVICES.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case SERVICES.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
            };
        case SERVICES.POST_SUCCESS:
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

export default Reducer;