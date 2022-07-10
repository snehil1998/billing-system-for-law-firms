import thunkMiddleware from "redux-thunk";
import { createStore, applyMiddleware } from "redux";
import Reducer from "./Reducer";

export const store = createStore(Reducer, applyMiddleware(thunkMiddleware));