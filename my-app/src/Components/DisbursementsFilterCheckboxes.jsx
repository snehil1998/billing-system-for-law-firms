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
       <div className={'disbursements-filter-checkboxes-container'}>
           <div className={'disbursements-filter-checkboxes-translation'} style={{fontSize: '18px'}}>
               Tick columns to include in the report:
           </div>
           <div className={'checkboxes-container'} style={{fontSize: '16px'}}>
               <input
                   type="checkbox"
                   id="caseName"
                   name="Case Name"
                   value="Case Name"
                   checked={tickedBoxes.find(box => box === "Case Name")}
                   onChange={() => handleOnChange("Case Name")}
               />
               Case Name <t/>

               <input
                   type="checkbox"
                   id="disbursement"
                   name="Disbursement"
                   value="Disbursement"
                   checked={tickedBoxes.find(box => box === "Disbursement")}
                   onChange={() => handleOnChange("Disbursement")}
               />
               Disbursement <t/>

               <input
                   type="checkbox"
                   id="date"
                   name="Date"
                   value="Date"
                   checked={tickedBoxes.find(box => box === "Date")}
                   onChange={() => handleOnChange("Date")}
               />
               Date <t/>

               <input
                   type="checkbox"
                   id="currencyCode"
                   name="Currency Code"
                   value="Currency Code"
                   checked={tickedBoxes.find(box => box === "Currency Code")}
                   onChange={() => handleOnChange("Currency Code")}
               />
               Currency Code <t/>

               <input
                   type="checkbox"
                   id="conversionRate"
                   name="Conversion Rate"
                   value="Conversion Rate"
                   checked={tickedBoxes.find(box => box === "Conversion Rate")}
                   onChange={() => handleOnChange("Conversion Rate")}
               />
               Conversion Rate <t/>

               <input
                   type="checkbox"
                   id="inrAmount"
                   name="INR Amount"
                   value="INR Amount"
                   checked={tickedBoxes.find(box => box === "INR Amount")}
                   onChange={() => handleOnChange("INR Amount")}
               />
               INR Amount <t/>

               <input
                   type="checkbox"
                   id="conversionAmount"
                   name="Conversion Amount"
                   value="Conversion Amount"
                   checked={tickedBoxes.find(box => box === "Conversion Amount")}
                   onChange={() => handleOnChange("Conversion Amount")}
               />
               Conversion Amount <t/>
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