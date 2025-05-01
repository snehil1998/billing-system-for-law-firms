import React from 'react';
import PropTypes from 'prop-types';
import './FormField.css';

export const FormField = ({
    label,
    type = 'text',
    value,
    onChange,
    options,
    disabled = false,
    required = false,
    placeholder,
    className = '',
    id,
}) => {
    const renderInput = () => {
        if (type === 'select') {
            return (
                <select
                    id={id}
                    className={`form-field-select ${className}`}
                    value={value}
                    onChange={onChange}
                    disabled={disabled}
                    required={required}
                >
                    <option value="" disabled>
                        {placeholder || 'Select an option'}
                    </option>
                    {options?.sort((a, b) => a.label.localeCompare(b.label)).map((option) => (
                        <option key={option.value} value={option.value}>
                            {option.label}
                        </option>
                    ))}
                </select>
            );
        }

        return (
            <input
                id={id}
                type={type}
                className={`form-field-input ${className}`}
                value={value}
                onChange={onChange}
                disabled={disabled}
                required={required}
                placeholder={placeholder}
            />
        );
    };

    return (
        <div className="form-field-container">
            <label className="form-field-label" htmlFor={id}>
                {label}
            </label>
            {renderInput()}
        </div>
    );
};

FormField.propTypes = {
    label: PropTypes.string.isRequired,
    type: PropTypes.oneOf(['text', 'select', 'date', 'number']),
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onChange: PropTypes.func.isRequired,
    options: PropTypes.arrayOf(
        PropTypes.shape({
            label: PropTypes.string.isRequired,
            value: PropTypes.string.isRequired,
        })
    ),
    disabled: PropTypes.bool,
    required: PropTypes.bool,
    placeholder: PropTypes.string,
    className: PropTypes.string,
    id: PropTypes.string.isRequired,
};

export default FormField; 