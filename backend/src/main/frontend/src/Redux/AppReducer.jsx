import {combineReducers} from "redux";
import ServicesReducer from "./Services/ServicesReducer";
import ClientsReducer from "./Clients/ClientsReducer";
import CasesReducer from "./Cases/CasesReducer";
import AttorneysReducer from "./Attorneys/AttorneysReducer";
import DisbursementsReducer from "./Disbursements/DisbursementsReducer";
import MessageReducer from "./Message/MessageReducer";


const AppReducer = combineReducers({
    services: ServicesReducer,
    clients: ClientsReducer,
    cases: CasesReducer,
    attorneys: AttorneysReducer,
    disbursements: DisbursementsReducer,
    message: MessageReducer,
})

export default AppReducer;