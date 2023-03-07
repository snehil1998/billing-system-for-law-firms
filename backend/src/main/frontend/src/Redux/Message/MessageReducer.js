import MESSAGE from "./MessageConstants";

const initalState = {
    message: null,
};

const MessageReducer = (state = initalState, action) => {
    switch (action.type) {
        case MESSAGE.ADD_MESSAGE:
            return {
                ...state,
                message: action.message,
            };
        case MESSAGE.CLEAR_MESSAGE:
            return {
                ...state,
                message: null,
            };
        default:
            return state;
    }
};

export default MessageReducer;