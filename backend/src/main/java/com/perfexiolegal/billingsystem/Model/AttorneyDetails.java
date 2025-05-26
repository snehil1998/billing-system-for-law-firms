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

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AttorneyDetails {
    private String attorneyId;
    private String firstName;
    private String lastName;
    private List<ServicePricing> servicePricing;

    @JsonIgnore
    public AttorneyDetails withoutId() {
        return AttorneyDetails.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .servicePricing(new ArrayList<>(this.servicePricing))
                .build();
    }

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

    public boolean removeServicePricing(String clientId) {
        if (servicePricing == null) {
            return false;
        }
        
        int initialSize = servicePricing.size();
        servicePricing.removeIf(pricing -> pricing.getClientId().equals(clientId));
        return initialSize != servicePricing.size();
    }

    public Optional<ServicePricing> getServicePricingForClient(String clientId) {
        if (servicePricing == null) {
            return Optional.empty();
        }
        
        return servicePricing.stream()
                .filter(pricing -> pricing.getClientId().equals(clientId))
                .findFirst();
    }

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
