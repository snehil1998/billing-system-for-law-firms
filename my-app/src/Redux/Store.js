import thunkMiddleware from "redux-thunk";
import { createStore, applyMiddleware } from "redux";
import Reducer from "./Reducer";
import {composeWithDevTools} from 'redux-devtools-extension';


export const store = createStore(Reducer,
    undefined,
    composeWithDevTools({trace: true, traceLimit: 25})(
        applyMiddleware(thunkMiddleware)
    )
);