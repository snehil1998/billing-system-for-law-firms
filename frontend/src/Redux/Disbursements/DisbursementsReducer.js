import DISBURSEMENTS from "./DisbursementsConstants";

const initalState = {
    data: [],
    isLoading: false,
    isError: false,
    fromSearchDate: null,
    toSearchDate: null,
    filteredData: [],
    filterCheckboxes: [],
};

function filterDataByDate(fromDate, toDate, data){
    const filteredData = [];
    if(fromDate !== null && toDate !== null) {
        data.forEach(eachData => {
            const year = parseInt(eachData.date.split('-')[0]);
            const month = parseInt(eachData.date.split('-')[1]);
            const day = parseInt(eachData.date.split('-')[2]);
            if (((year > fromDate.year) || (year === fromDate.year && month > fromDate.month) ||
                    (year === fromDate.year && month === fromDate.month && day >= fromDate.day))
                && ((year < toDate.year) || (year === toDate.year && month < toDate.month) ||
                    (year === toDate.year && month === toDate.month && day <= toDate.day))) {
                filteredData.push(eachData);
            }
        });
    } else {
        return data;
    }
    return filteredData;
}

const DisbursementsReducer = (state = initalState, action) => {
    switch (action.type) {
        case DISBURSEMENTS.LOAD:
            return {
                ...state,
                isLoading: true,
                isError: false,
            };
        case DISBURSEMENTS.LOAD_SUCCESS:
            return {
                ...state,
                data: action.data,
                filteredData: filterDataByDate(state.fromSearchDate, state.toSearchDate, action.data),
                isLoading: false,
            };
        case DISBURSEMENTS.POST_SUCCESS:
            return {
                ...state,
                data: action.data,
                filteredData: filterDataByDate(state.fromSearchDate, state.toSearchDate, action.data),
                isLoading: false,
                isError: false
            };
        case DISBURSEMENTS.ADD_FROM_SEARCH_DATE:
            return {
                ...state,
                filteredData: filterDataByDate(action.fromSearchDate, state.toSearchDate, state.data),
                fromSearchDate: action.fromSearchDate,
            }
        case DISBURSEMENTS.ADD_TO_SEARCH_DATE:
            return {
                ...state,
                filteredData: filterDataByDate(state.fromSearchDate, action.toSearchDate, state.data),
                toSearchDate: action.toSearchDate,
            }
        case DISBURSEMENTS.ADD_FILTER_CHECKBOXES:
            return {
                ...state,
                filterCheckboxes: action.filterCheckboxes,
            }
        default:
            return state;
    }
};

export default DisbursementsReducer;