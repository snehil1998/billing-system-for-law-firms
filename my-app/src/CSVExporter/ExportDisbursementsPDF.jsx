import jsPDF from "jspdf";
import "jspdf-autotable";
import PropTypes from "prop-types";

export default function ExportDisbursementsPDF(props) {
    let csvRows = [];
    let header = ['S.No.'];
    props.rows.forEach((row, index) => {
        let csvRow = {}
        csvRow["Sno"] = index+1
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Case Name')) {
            csvRow["Case"] = row.original.casename
            if (index === 0) header.push('Case Name')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Disbursement')) {
            csvRow["Disbursement"] = row.original.disbursement
            if (index === 0) header.push(('Disbursement'))
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Date')) {
            csvRow["Date"] = row.original.date
            if (index === 0) header.push('Date')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Currency Code')) {
            csvRow["CurrencyCode"] = row.original.currencycode
            if (index === 0) header.push('Currency Code')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Conversion Rate')) {
            csvRow["ConversionRate"] = row.original.conversionrate
            if (index === 0) header.push('Conversion Rate')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'INR Amount')) {
            csvRow["INRAmount"] = row.original.inramount
            if (index === 0) header.push('Amount (in INR)')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Conversion Amount')) {
            csvRow["ConversionAmount"] = row.original.conversionamount
            if (index === 0) header.push('Converted Amount')
        }
        csvRows.push(csvRow)
    })

    const fromDate = props.fromDate?.day + '/' + props.fromDate?.month + '/' + props.fromDate?.year;
    const toDate = props.toDate?.day + '/' + props.toDate?.month + '/' + props.toDate?.year;
    const unit = "pt";
    const size = "A4";
    const orientation = "landscape";

    const marginLeft = 40;
    const doc = new jsPDF(orientation, unit, size);

    doc.setFontSize(15);

    const title = "Disbursements Report";
    const report = props.title;
    const client = "Client: " + props.client;
    const period = "Period: " + fromDate + " to " + toDate;
    const headers = [header];

    const data = [];
    csvRows.forEach(row => {
        const eachRowData = [];
        eachRowData.push(row.Sno);
        if(row.Case !== undefined) eachRowData.push(row.Case);
        if(row.Disbursement !== undefined) eachRowData.push(row.Disbursement);
        if(row.Date !== undefined) eachRowData.push(row.Date);
        if(row.CurrencyCode !== undefined) eachRowData.push(row.CurrencyCode);
        if(row.ConversionRate !== undefined) eachRowData.push(row.ConversionRate);
        if(row.INRAmount !== undefined) eachRowData.push(row.INRAmount);
        if(row.ConversionAmount !== undefined) eachRowData.push(row.ConversionAmount);
        data.push(eachRowData);
    });

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
    doc.save("disbursement-report.pdf")
    return {};
}

ExportDisbursementsPDF.propTypes = {
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
    filterCheckboxes: PropTypes.array.isRequired,
    title: PropTypes.string.isRequired,
}