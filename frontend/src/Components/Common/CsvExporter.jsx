import { mkConfig, generateCsv, download } from 'export-to-csv';

const csvConfig = mkConfig({
    fieldSeparator: ',',
    decimalSeparator: '.',
    useKeysAsHeaders: true,
  });

export const handleExportRows = (rows, visibleColumns) => {
    const rowData = rows.map((row) => row.original);
    const cols = visibleColumns
        .map((column) => {
            return {
                "columnId": column.columnDef.id,
                "columnHeader": column.columnDef.header,
            }
        })
        .filter((col) => col.columnId !== 'mrt-row-actions');
    const rowDataForVisibleColumns = rowData.map((row) => {
        const obj = {};
        cols.forEach((col) => {
            obj[col.columnHeader] = row[col.columnId];
        });
        return obj;
    });
    const csv = generateCsv(csvConfig)(rowDataForVisibleColumns);
    download(csvConfig)(csv);
};