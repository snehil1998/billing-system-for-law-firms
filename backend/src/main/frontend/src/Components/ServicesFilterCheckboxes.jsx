import {useEffect, useState} from "react";
import {addFilterCheckboxesServices} from "../Redux/Services/ServicesActions";
import {connect} from "react-redux";

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
       <div id={'services-filter-checkboxes-container'}>
           <div id={'services-filter-checkboxes-translation'} className={'dropdown-translation'}>
               {'Tick columns to include in the report:'}
           </div>
           <div id={'services-filter-checkboxes'} className={'dropdown-checkboxes'}>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Case Name")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-caseName"
                       name="Case Name"
                       value="Case Name"
                       checked={tickedBoxes.find(box => box === "Case Name")}
                       onChange={() => handleOnChange("Case Name")} />
                   Case Name <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Service")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-service"
                       name="Service"
                       value="Service"
                       checked={tickedBoxes.find(box => box === "Service")}
                       onChange={() => handleOnChange("Service")}
                   />
                   Service <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Date")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes--date"
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
                       id="services-filter-checkboxes-currencyCode"
                       name="Currency Code"
                       value="Currency Code"
                       checked={tickedBoxes.find(box => box === "Currency Code")}
                       onChange={() => handleOnChange("Currency Code")}
                   />
                   Currency Code <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Amount")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-amount"
                       name="Amount"
                       value="Amount"
                       checked={tickedBoxes.find(box => box === "Amount")}
                       onChange={() => handleOnChange("Amount")}
                   />
                   Amount <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Attorneys Name")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-attorneysName"
                       name="Attorneys Name"
                       value="Attorneys Name"
                       checked={tickedBoxes.find(box => box === "Attorneys Name")}
                       onChange={() => handleOnChange("Attorneys Name")}
                   />
                   Attorneys Name <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Time Spent (in Minutes)")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-timeSpentInMinutes"
                       name="Time Spent (in Minutes)"
                       value="Time Spent (in Minutes)"
                       checked={tickedBoxes.find(box => box === "Time Spent (in Minutes)")}
                       onChange={() => handleOnChange("Time Spent (in Minutes)")}
                   />
                   Time Spent (in Minutes) <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Time Spent (in Hours)")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-timeSpentInHours"
                       name="Time Spent (in Hours)"
                       value="Time Spent (in Hours)"
                       checked={tickedBoxes.find(box => box === "Time Spent (in Hours)")}
                       onChange={() => handleOnChange("Time Spent (in Hours)")}
                   />
                   Time Spent (in Hours) <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Rate Per Hour")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-ratePerHour"
                       name="Rate Per Hour"
                       value="Rate Per Hour"
                       checked={tickedBoxes.find(box => box === "Rate Per Hour")}
                       onChange={() => handleOnChange("Rate Per Hour")}
                   />
                   Rate Per Hour <t/>
               </div>
               <div className={'dropdown-checkbox'} onClick={() => handleOnChange("Total")}>
                   <input
                       type="checkbox"
                       id="services-filter-checkboxes-total"
                       name="Total"
                       value="Total"
                       checked={tickedBoxes.find(box => box === "Total")}
                       onChange={() => handleOnChange("Total")}
                   />
                   Total <t/>
               </div>
           </div>
       </div>
    );
}

const mapDispatchToProps = dispatch => ({
    addFilterCheckboxes: (filterCheckboxes) => {
        dispatch(addFilterCheckboxesServices(filterCheckboxes))
    }
})

export default connect(null, mapDispatchToProps)(ServicesFilterCheckboxes)