package com.perfexiolegal.billingsystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents an attorney in the system.
 * This class serves as both the domain model and DTO.
 */
@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Attorneys {
    private String attorneyId;
    private String firstName;
    private String lastName;
    private List<ServicePricing> servicePricing;

    /**
     * Creates a new attorney without an ID (for creation requests).
     * @return A new attorney instance with null ID
     */
    @JsonIgnore
    public Attorneys withoutId() {
        return Attorneys.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .servicePricing(new ArrayList<>(this.servicePricing))
                .build();
    }

    /**
     * Adds a new service pricing entry.
     * @param newPricing The service pricing to add
     * @return true if added successfully, false if client already exists
     */
    public boolean addServicePricing(ServicePricing newPricing) {
        if (servicePricing == null) {
            servicePricing = new ArrayList<>();
        }
        
        boolean clientExists = servicePricing.stream()
                .anyMatch(pricing -> pricing.getClientId().equals(newPricing.getClientId()));
        
        if (!clientExists) {
            servicePricing.add(newPricing);
            return true;
        }
        return false;
    }

    /**
     * Removes a service pricing entry for a specific client.
     * @param clientId The client ID to remove pricing for
     * @return true if removed, false if client not found
     */
    public boolean removeServicePricing(String clientId) {
        if (servicePricing == null) {
            return false;
        }
        
        int initialSize = servicePricing.size();
        servicePricing.removeIf(pricing -> pricing.getClientId().equals(clientId));
        return initialSize != servicePricing.size();
    }

    /**
     * Gets service pricing for a specific client.
     * @param clientId The client ID to get pricing for
     * @return Optional containing the service pricing if found
     */
    public Optional<ServicePricing> getServicePricingForClient(String clientId) {
        if (servicePricing == null) {
            return Optional.empty();
        }
        
        return servicePricing.stream()
                .filter(pricing -> pricing.getClientId().equals(clientId))
                .findFirst();
    }

    /**
     * Updates service pricing for a specific client.
     * @param clientId The client ID to update pricing for
     * @param newPricing The new pricing information
     * @return true if updated, false if client not found
     */
    public boolean updateServicePricing(String clientId, ServicePricing newPricing) {
        if (servicePricing == null) {
            return false;
        }
        
        for (int i = 0; i < servicePricing.size(); i++) {
            if (servicePricing.get(i).getClientId().equals(clientId)) {
                servicePricing.set(i, newPricing);
                return true;
            }
        }
        return false;
    }
}
