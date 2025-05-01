export const API_BASE_URL = '/backend';
export const EXCHANGE_RATE_API = 'https://api.exchangerate.host/';

export const API_ENDPOINTS = {
    CLIENTS: `${API_BASE_URL}/clients`,
    CASES: `${API_BASE_URL}/cases`,
    SERVICES: `${API_BASE_URL}/services`,
    DISBURSEMENTS: `${API_BASE_URL}/disbursements`,
    ATTORNEYS: `${API_BASE_URL}/attorneys`,
};

export const HTTP_STATUS = {
    OK: 200,
    CREATED: 201,
    CONFLICT: 410,
};

export const DEFAULT_VALUES = {
    INITIAL_AMOUNT: 0,
    DATE_FORMAT: 'YYYY-MM-DD',
}; 