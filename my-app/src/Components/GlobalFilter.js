import './GlobalFilter.css';

export const GlobalFilter = ({
  preGlobalFilteredRows,
  globalFilter,
  setGlobalFilter
}) => {
    const count = preGlobalFilteredRows && preGlobalFilteredRows.length;

    return (
        <div className={'global-filter-container'}>
            <span>
                <div id={'global-filter-search-translation'} className={'dropdown-translation'}>
                  SEARCH:{" "}
                </div>
                <input
                    className={'global-filter-search-input-field'}
                    value={globalFilter || ""}
                    onChange={e => {
                        setGlobalFilter(e.target.value || undefined); // Set undefined to remove the filter entirely
                    }}
                    placeholder={`${count} records...`}
                />
            </span>
        </div>
    );
};