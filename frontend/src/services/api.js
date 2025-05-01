import { API_ENDPOINTS, HTTP_STATUS } from '../constants/api';

class ApiError extends Error {
    constructor(message, status) {
        super(message);
        this.status = status;
        this.name = 'ApiError';
    }
}

const handleResponse = async (response) => {
    if (response.status === HTTP_STATUS.OK || response.status === HTTP_STATUS.CREATED) {
        return await response.json();
    }
    
    if (response.status === HTTP_STATUS.CONFLICT) {
        throw new ApiError('Resource already exists', response.status);
    }
    
    throw new ApiError('An error occurred', response.status);
};

const apiService = {
    async get(endpoint) {
        try {
            const response = await fetch(endpoint);
            return await handleResponse(response);
        } catch (error) {
            console.error(`Error fetching from ${endpoint}:`, error);
            throw error;
        }
    },

    async post(endpoint, data) {
        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data),
            });
            return await handleResponse(response);
        } catch (error) {
            console.error(`Error posting to ${endpoint}:`, error);
            throw error;
        }
    },

    async put(endpoint, data) {
        try {
            const response = await fetch(endpoint, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(data),
            });
            return await handleResponse(response);
        } catch (error) {
            console.error(`Error putting to ${endpoint}:`, error);
            throw error;
        }
    },

    async delete(endpoint) {
        try {
            const response = await fetch(endpoint, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                },
            });
            return await handleResponse(response);
        } catch (error) {
            console.error(`Error deleting from ${endpoint}:`, error);
            throw error;
        }
    },
};

export const clientsApi = {
    getAll: () => apiService.get(API_ENDPOINTS.CLIENTS),
    create: (data) => apiService.post(API_ENDPOINTS.CLIENTS, data),
    update: (id, data) => apiService.put(`${API_ENDPOINTS.CLIENTS}/${id}`, data),
    delete: (id) => apiService.delete(`${API_ENDPOINTS.CLIENTS}/${id}`),
};

export const casesApi = {
    getAll: () => apiService.get(API_ENDPOINTS.CASES),
    create: (data) => apiService.post(API_ENDPOINTS.CASES, data),
    update: (id, data) => apiService.put(`${API_ENDPOINTS.CASES}/${id}`, data),
    delete: (id) => apiService.delete(`${API_ENDPOINTS.CASES}/${id}`),
};

export const servicesApi = {
    getAll: () => apiService.get(API_ENDPOINTS.SERVICES),
    create: (data) => apiService.post(API_ENDPOINTS.SERVICES, data),
    update: (id, data) => apiService.put(`${API_ENDPOINTS.SERVICES}/${id}`, data),
    delete: (id) => apiService.delete(`${API_ENDPOINTS.SERVICES}/${id}`),
};

export const disbursementsApi = {
    getAll: () => apiService.get(API_ENDPOINTS.DISBURSEMENTS),
    create: (data) => apiService.post(API_ENDPOINTS.DISBURSEMENTS, data),
    update: (id, data) => apiService.put(`${API_ENDPOINTS.DISBURSEMENTS}/${id}`, data),
    delete: (id) => apiService.delete(`${API_ENDPOINTS.DISBURSEMENTS}/${id}`),
};

export const attorneysApi = {
    getAll: () => apiService.get(API_ENDPOINTS.ATTORNEYS),
    create: (data) => apiService.post(API_ENDPOINTS.ATTORNEYS, data),
    update: (id, data) => apiService.put(`${API_ENDPOINTS.ATTORNEYS}/${id}`, data),
    delete: (id) => apiService.delete(`${API_ENDPOINTS.ATTORNEYS}/${id}`),
}; 