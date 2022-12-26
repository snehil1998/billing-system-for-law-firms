import {combineReducers} from "redux";
import ServicesReducer from "./Services/Reducer";


const AppReducer = combineReducers({
    services: ServicesReducer,
})

export default AppReducer;