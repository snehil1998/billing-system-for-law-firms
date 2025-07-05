import { useRef } from "react"
import "./FileInput.css";

export function FileInput({fileName, onChange}) {
    const fileInputRef = useRef()
    return (
        <div>
            <button
                type="button"
                onClick={() => fileInputRef.current.click()}
                className="form-submit-btn">
                Choose PDF
            </button>
            <input
                type="file"
                accept=".pdf"
                style={{ display: 'none' }}
                onChange={(e) => onChange(e.target.files[0])}
                ref={fileInputRef}
            />
            <span className="file-name-span">
                {fileName ?? 'No file chosen'}
            </span>                
        </div>
    )
}