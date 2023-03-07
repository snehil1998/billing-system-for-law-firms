import MESSAGE from "./MessageConstants";

export const addMessage = (message) => async (dispatch) => {
  dispatch({
      type: MESSAGE.ADD_MESSAGE,
      message,
  });
};

export const clearMessage = () => async (dispatch) => {
    dispatch({type: MESSAGE.CLEAR_MESSAGE});
};