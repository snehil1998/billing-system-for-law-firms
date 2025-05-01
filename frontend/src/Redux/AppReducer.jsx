import {combineReducers} from "redux";
import ServicesReducer from "./services/ServicesReducer";
import ClientsReducer from "./clients/ClientsReducer";
import CasesReducer from "./cases/CasesReducer";
import AttorneysReducer from "./attorneys/AttorneysReducer";
import DisbursementsReducer from "./disbursements/DisbursementsReducer";
import MessageReducer from "./message/MessageReducer";


const AppReducer = combineReducers({
    services: ServicesReducer,
    clients: ClientsReducer,
    cases: CasesReducer,
    attorneys: AttorneysReducer,
    disbursements: DisbursementsReducer,
    message: MessageReducer,
})

export default AppReducer;