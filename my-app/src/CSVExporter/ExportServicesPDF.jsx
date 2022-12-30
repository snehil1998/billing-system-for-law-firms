import jsPDF from "jspdf";
import "jspdf-autotable";
import PropTypes from "prop-types";

function ExportServicesPDF(props) {
    let csvRows = []
    props.rows.forEach((row, index) => {
        let csvRow = {}
        csvRow["Sno"] = index+1
        csvRow["Case"] = row.original.casename
        csvRow["Service"] = row.original.service
        csvRow["Date"] = row.original.date
        csvRow["CurrencyCode"] = row.original.currencycode
        csvRow["Amount"] = row.original.amount
        if(row.canExpand){
            csvRow["Attorneys"] = row.original?.subRows[0].attorneys
            csvRow["Minutes"] = row.original?.subRows[0].minutes
            csvRow["Hours"] = row.original?.subRows[0].hours
            csvRow["Pricing"] = row.original?.subRows[0].pricing
            csvRow["Total"] = row.original?.subRows[0].total
        } else {
            csvRow["Attorneys"] = row.original.attorneys
            csvRow["Minutes"] = row.original.minutes
            csvRow["Hours"] = row.original.hours
            csvRow["Pricing"] = row.original.pricing
            csvRow["Total"] = row.original.total
        }
        csvRows.push(csvRow)
        csvRow = {}
        row.original?.subRows?.forEach((subRow, index) => {
            if (index > 0){
                csvRow["Sno"] = ""
                csvRow["Case"] = subRow.casename
                csvRow["Service"] = subRow.service
                csvRow["Date"] = subRow.date
                csvRow["CurrencyCode"] = subRow.currencycode
                csvRow["Amount"] = subRow.amount
                csvRow["Attorneys"] = subRow.attorneys
                csvRow["Minutes"] = subRow.minutes
                csvRow["Hours"] = subRow.hours
                csvRow["Pricing"] = subRow.pricing
                csvRow["Total"] = subRow.total
                csvRows.push(csvRow)
                csvRow = {}
            }
        })
    })

    const fromDate = props.fromDate?.day + '/' + props.fromDate?.month + '/' + props.fromDate?.year;
    const toDate = props.toDate?.day + '/' + props.toDate?.month + '/' + props.toDate?.year;
    const unit = "pt";
    const size = "A4";
    const orientation = "landscape";

    const marginLeft = 40;
    const doc = new jsPDF(orientation, unit, size);

    doc.setFontSize(15);

    const title = "Services Report";
    const report = "Report";
    const client = "Client: " + props.client;
    const period = "Period: " + fromDate + " to " + toDate;
    const headers = [["S.No.", "Case", "Service", "Date", "Currency Code", "Service Fee", "Attorney(s)",
        "Time spent (in minutes)", "Time spent (in hours)", "Rate per hour", "Amount for Attorney"]];

    const data = csvRows.map(row=> [row.Sno, row.Case, row.Service, row.Date, row.CurrencyCode,
        row.Amount, row.Attorneys, row.Minutes, row.Hours, row.Pricing, row.Total]);

    let content = {
        startY: 150,
        head: headers,
        body: data
    };

    doc.text(title, marginLeft, 40);
    doc.text(report, marginLeft, 70);
    doc.text(client, marginLeft, 100);
    doc.text(period, marginLeft, 130);
    doc.autoTable(content);
    doc.save("report.pdf")
    return null;
}

ExportServicesPDF.propTypes = {
    rows: PropTypes.array.isRequired,
    fromDate: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,

    }),
    toDate: PropTypes.shape({
        day: PropTypes.number.isRequired,
        month: PropTypes.number.isRequired,
        year: PropTypes.number.isRequired,
    }),
    client: PropTypes.string.isRequired,
}

export default ExportServicesPDF;