import {combineReducers} from "redux";
import ServicesReducer from "./Services/ServicesReducer";
import ClientsReducer from "./Clients/ClientsReducer";


const AppReducer = combineReducers({
    services: ServicesReducer,
    clients: ClientsReducer,
})

export default AppReducer;