import {combineReducers} from "redux";
import ServicesReducer from "./Services/ServicesReducer";
import ClientsReducer from "./Clients/ClientsReducer";
import CasesReducer from "./Cases/CasesReducer";


const AppReducer = combineReducers({
    services: ServicesReducer,
    clients: ClientsReducer,
    cases: CasesReducer,
})

export default AppReducer;