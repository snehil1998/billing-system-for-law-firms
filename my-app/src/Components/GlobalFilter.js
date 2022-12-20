
export const GlobalFilter = ({
  preGlobalFilteredRows,
  globalFilter,
  setGlobalFilter
}) => {
    const count = preGlobalFilteredRows && preGlobalFilteredRows.length;

    return (
        <div style={{textAlign:'left', margin:'1vw', width: '10vw'}}>
            <span>
                <div className={'search-translation'} style={{fontSize: '20px'}}>
                  SEARCH:{" "}
                </div>
                <input
                    value={globalFilter || ""}
                    onChange={e => {
                        setGlobalFilter(e.target.value || undefined); // Set undefined to remove the filter entirely
                    }}
                    placeholder={`${count} records...`}
                    style={{
                        border: "0",
                        height: '3vh',
                        fontSize: '16px',
                        marginTop: '1vh',
                    }}
                />
            </span>
        </div>
    );
};