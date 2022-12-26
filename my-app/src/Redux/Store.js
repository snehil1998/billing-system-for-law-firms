import thunkMiddleware from "redux-thunk";
import { createStore, applyMiddleware } from "redux";
import {composeWithDevTools} from 'redux-devtools-extension';
import AppReducer from "./AppReducer";


export const store = createStore(AppReducer,
    undefined,
    composeWithDevTools({trace: true, traceLimit: 25})(
        applyMiddleware(thunkMiddleware)
    )
);