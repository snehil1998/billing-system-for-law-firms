import CLIENTS from "./ClientsConstants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
};

const ClientsReducer = (state = initalState, action) => {
    switch (action.type) {
        case CLIENTS.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case CLIENTS.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                isLoading: false,
            };
        case CLIENTS.POST_SUCCESS:
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

export default ClientsReducer;