import jsPDF from "jspdf";
import "jspdf-autotable";
import PropTypes from "prop-types";

export default function ExportServicesPDF(props) {
    let csvRows = [];
    let header = ['S.No.'];
    props.rows.forEach((row, index) => {
        let csvRow = {}
        csvRow["Sno"] = index+1
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Case Name')) {
            csvRow["Case"] = row.original.casename
            if (index === 0) header.push('Case Name')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Service')) {
            csvRow["Service"] = row.original.service
            if (index === 0) header.push(('Service'))
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Date')) {
            csvRow["Date"] = row.original.date
            if (index === 0) header.push('Date')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Currency Code')) {
            csvRow["CurrencyCode"] = row.original.currencycode
            if (index === 0) header.push('Currency Code')
        }
        if (props.filterCheckboxes.find(checkbox => checkbox === 'Amount')) {
            csvRow["Amount"] = row.original.amount
            if (index === 0) header.push('Amount')
        }
        if(row.canExpand){
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Attorneys Name')) {
                csvRow["Attorneys"] = row.original?.subRows[0].attorneys
                if (index === 0) header.push('Attorneys')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Minutes)')) {
                csvRow["Minutes"] = row.original?.subRows[0].minutes
                if (index === 0) header.push('Minutes')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Hours)')) {
                csvRow["Hours"] = row.original?.subRows[0].hours
                if (index === 0) header.push('Hours')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Rate Per Hour')) {
                csvRow["Pricing"] = row.original?.subRows[0].pricing
                if (index === 0) header.push('Pricing')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Total')) {
                csvRow["Total"] = row.original?.subRows[0].total
                if (index === 0) header.push('Total')
            }
        } else {
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Attorneys Name')) {
                csvRow["Attorneys"] = row.original.attorneys
                if (index === 0) header.push('Attorneys')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Minutes)')) {
                csvRow["Minutes"] = row.original.minutes
                if (index === 0) header.push('Minutes')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Hours)')) {
                csvRow["Hours"] = row.original.hours
                if (index === 0) header.push('Hours')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Rate Per Hour')) {
                csvRow["Pricing"] = row.original.pricing
                if (index === 0) header.push('Pricing')
            }
            if (props.filterCheckboxes.find(checkbox => checkbox === 'Total')) {
                csvRow["Total"] = row.original.total
                if (index === 0) header.push('Total')
            }
        }
        csvRows.push(csvRow)
        csvRow = {}
        row.original?.subRows?.forEach((subRow, index) => {
            if (index > 0){
                csvRow["Sno"] = ""
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Case Name')) csvRow["Case"] = subRow.casename
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Service')) csvRow["Service"] = subRow.service
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Date')) csvRow["Date"] = subRow.date
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Currency Code')) csvRow["CurrencyCode"] = subRow.currencycode
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Amount')) csvRow["Amount"] = subRow.amount
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Attorneys Name')) csvRow["Attorneys"] = subRow.attorneys
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Minutes)')) csvRow["Minutes"] = subRow.minutes
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Time Spent (in Hours)')) csvRow["Hours"] = subRow.hours
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Rate Per Hour')) csvRow["Pricing"] = subRow.pricing
                if (props.filterCheckboxes.find(checkbox => checkbox === 'Total')) csvRow["Total"] = subRow.total
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
    const report = props.title;
    const client = "Client: " + props.client;
    const period = "Period: " + fromDate + " to " + toDate;
    const headers = [header];

    const data = [];
    csvRows.forEach(row => {
        const eachRowData = [];
        eachRowData.push(row.Sno);
        if(row.Case !== undefined) eachRowData.push(row.Case);
        if(row.Service !== undefined) eachRowData.push(row.Service);
        if(row.Date !== undefined) eachRowData.push(row.Date);
        if(row.CurrencyCode !== undefined) eachRowData.push(row.CurrencyCode);
        if(row.Amount !== undefined) eachRowData.push(row.Amount);
        if(row.Attorneys !== undefined) eachRowData.push(row.Attorneys);
        if(row.Minutes !== undefined) eachRowData.push(row.Minutes);
        if(row.Hours !== undefined) eachRowData.push(row.Hours);
        if(row.Pricing !== undefined) eachRowData.push(row.Pricing);
        if(row.Total !== undefined) eachRowData.push(row.Total);
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
    doc.save("service-report.pdf")
    return {};
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
    filterCheckboxes: PropTypes.array.isRequired,
    title: PropTypes.string.isRequired,
}