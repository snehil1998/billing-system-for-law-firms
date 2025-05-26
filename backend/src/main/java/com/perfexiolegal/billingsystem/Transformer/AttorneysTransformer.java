package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.AttorneyDetails;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttorneysTransformer {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysTransformer.class);

    @Autowired
    private AttorneysService attorneysService;

    public AttorneyDetails update(AttorneyDetails updatedData, String attorneyID) throws ServiceException {
        logger.debug("Updating attorney with ID: {}", attorneyID);
        AttorneyDetails existingAttorney = attorneysService.getAttorneyById(attorneyID)
                .orElseThrow(() -> new ServiceException("Attorney not found"));

        // Update basic information
        AttorneyDetails updatedAttorney = AttorneyDetails.builder()
                .attorneyId(attorneyID)
                .firstName(updatedData.getFirstName())
                .lastName(updatedData.getLastName())
                .servicePricing(existingAttorney.getServicePricing())
                .build();

        // Add new service pricing entries
        for (ServicePricing newPricing : updatedData.getServicePricing()) {
            if (!updatedAttorney.addServicePricing(newPricing)) {
                throw new ServiceException("Client " + newPricing.getClientId() + " already exists in service pricing");
            }
        }

        return updatedAttorney;
    }

    public AttorneyDetails deleteServicePrice(AttorneyDetails attorney, String clientID) {
        logger.debug("Deleting service price for client ID: {} from attorney ID: {}", 
                clientID, attorney.getAttorneyId());

        if (!attorney.removeServicePricing(clientID)) {
            logger.warn("No service pricing found for client ID: {} in attorney ID: {}", 
                    clientID, attorney.getAttorneyId());
        }

        return attorney;
    }
}
