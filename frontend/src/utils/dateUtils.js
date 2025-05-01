export const formatDate = (date) => {
    if (!date) return '';
    const { year, month, day } = date;
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
};

export const parseDate = (dateString) => {
    if (!dateString) return null;
    const [year, month, day] = dateString.split('-').map(Number);
    return { year, month, day };
};

export const isDateInRange = (date, fromDate, toDate) => {
    if (!date || !fromDate || !toDate) return true;
    
    const { year, month, day } = date;
    
    const isAfterFromDate = 
        year > fromDate.year || 
        (year === fromDate.year && month > fromDate.month) ||
        (year === fromDate.year && month === fromDate.month && day >= fromDate.day);
        
    const isBeforeToDate = 
        year < toDate.year || 
        (year === toDate.year && month < toDate.month) ||
        (year === toDate.year && month === toDate.month && day <= toDate.day);
        
    return isAfterFromDate && isBeforeToDate;
};

export const getCurrentDate = () => {
    const now = new Date();
    return {
        year: now.getFullYear(),
        month: now.getMonth() + 1,
        day: now.getDate()
    };
}; 