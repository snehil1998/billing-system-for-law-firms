import React from "react";

const ButtonGroup = ({ submitLabel = "Submit", resetLabel = "Clear" }) => (
  <div className="form-buttons">
    <button type="submit" className="submit-button">{submitLabel}</button>
    <button type="reset" className="reset-button">{resetLabel}</button>
  </div>
);

export default ButtonGroup; 