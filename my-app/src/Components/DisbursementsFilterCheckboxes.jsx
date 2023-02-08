import {useEffect, useState} from "react";
import {connect} from "react-redux";
import {addFilterCheckboxesDisbursements} from "../Redux/Disbursements/DisbursementsActions";

function ServicesFilterCheckboxes(props) {
    const [tickedBoxes, setTickedBoxes] = useState([]);
    const handleOnChange = (value) => {
        if(tickedBoxes.find(box => box === value)){
            const updatedTickedBoxes = tickedBoxes.filter(box => box !== value);
            setTickedBoxes(updatedTickedBoxes);
        } else {
            setTickedBoxes([...tickedBoxes, value]);
        }
    }

    useEffect(() => {
        props.addFilterCheckboxes(tickedBoxes);
    }, [tickedBoxes])

    return (
       <div id={'disbursements-filter-checkboxes-container'}>
           <div id={'disbursements-filter-checkboxes-translation'} className={'dropdown-translation'}>
               {'Tick columns to include in the report:'}
           </div>
           <div id={'disbursements-filter-checkboxes'} className={'dropdown-checkboxes'}>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Case Name")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-caseName"
                       name="Case Name"
                       value="Case Name"
                       checked={tickedBoxes.find(box => box === "Case Name")}
                       onChange={() => handleOnChange("Case Name")}
                   />
                   Case Name <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Disbursement")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-disbursement"
                       name="Disbursement"
                       value="Disbursement"
                       checked={tickedBoxes.find(box => box === "Disbursement")}
                       onChange={() => handleOnChange("Disbursement")}
                   />
                   Disbursement <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Date")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-date"
                       name="Date"
                       value="Date"
                       checked={tickedBoxes.find(box => box === "Date")}
                       onChange={() => handleOnChange("Date")}
                   />
                   Date <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Currency Code")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-currencyCode"
                       name="Currency Code"
                       value="Currency Code"
                       checked={tickedBoxes.find(box => box === "Currency Code")}
                       onChange={() => handleOnChange("Currency Code")}
                   />
                   Currency Code <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Conversion Rate")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-conversionRate"
                       name="Conversion Rate"
                       value="Conversion Rate"
                       checked={tickedBoxes.find(box => box === "Conversion Rate")}
                       onChange={() => handleOnChange("Conversion Rate")}
                   />
                   Conversion Rate <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("INR Amount")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-inrAmount"
                       name="INR Amount"
                       value="INR Amount"
                       checked={tickedBoxes.find(box => box === "INR Amount")}
                       onChange={() => handleOnChange("INR Amount")}
                   />
                   INR Amount <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Conversion Amount")}>
                   <input
                       type="checkbox"
                       id="disbursements-filter-checkboxes-conversionAmount"
                       name="Conversion Amount"
                       value="Conversion Amount"
                       checked={tickedBoxes.find(box => box === "Conversion Amount")}
                       onChange={() => handleOnChange("Conversion Amount")}
                   />
                   Conversion Amount <t/>
               </div>
           </div>
       </div>
    );
}

const mapDispatchToProps = dispatch => ({
    addFilterCheckboxes: (filterCheckboxes) => {
        dispatch(addFilterCheckboxesDisbursements(filterCheckboxes))
    }
})

export default connect(null, mapDispatchToProps)(ServicesFilterCheckboxes)