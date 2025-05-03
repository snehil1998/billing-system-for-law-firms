import React from "react";
import "./DynamicInputList.css";

const DynamicInputList = ({
  count = 0,
  labelPrefix = "Item",
  dropdownOptions = [],
  dropdownValues = [],
  inputValues = [],
  onDropdownChange,
  onInputChange,
  dropdownPlaceholder = "Select option",
  inputPlaceholder = "Enter value",
  inputType = "text",
}) => {
  return (
    <div className="dynamic-input-list">
      {[...Array(count)].map((_, idx) => (
        <div className="form-group dynamic-input-item" key={idx}>
          <label className="form-label">{labelPrefix} {idx + 1}:</label>
          <div className="dynamic-input-inputs">
            <select
              className="form-input"
              value={dropdownValues[idx] || ""}
              onChange={e => onDropdownChange(idx, e.target.value)}
            >
              <option value="">{dropdownPlaceholder}</option>
              {dropdownOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
            <input
              className="form-input"
              type={inputType}
              value={inputValues[idx] || ""}
              onChange={e => onInputChange(idx, e.target.value)}
              placeholder={inputPlaceholder}
            />
          </div>
        </div>
      ))}
    </div>
  );
};

export default DynamicInputList; 