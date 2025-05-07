package com.perfexiolegal.billingsystem.Transformer;

import com.perfexiolegal.billingsystem.Exceptions.ServiceException;
import com.perfexiolegal.billingsystem.Model.Attorneys;
import com.perfexiolegal.billingsystem.Model.ServicePricing;
import com.perfexiolegal.billingsystem.Service.AttorneysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Transformer class for handling attorney data transformations.
 * Manages the conversion and manipulation of attorney data between different formats.
 */
@Component
public class AttorneysTransformer {

    private static final Logger logger = LoggerFactory.getLogger(AttorneysTransformer.class);

    @Autowired
    private AttorneysService attorneysService;

    /**
     * Updates an attorney's information with new service pricing data.
     * @param updatedData The updated attorney data
     * @param attorneyID The ID of the attorney to update
     * @return The updated attorney with merged service pricing
     * @throws ServiceException if there's an error retrieving the attorney
     */
    public Attorneys update(Attorneys updatedData, String attorneyID) throws ServiceException {
        logger.debug("Updating attorney with ID: {}", attorneyID);
        Attorneys existingAttorney = attorneysService.getAttorneyById(attorneyID)
                .orElseThrow(() -> new ServiceException("Attorney not found"));

        // Update basic information
        Attorneys updatedAttorney = Attorneys.builder()
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

    /**
     * Deletes a service price for a specific client from an attorney's service pricing list.
     * @param attorney The attorney whose service pricing needs to be updated
     * @param clientID The ID of the client whose service price should be removed
     * @return The updated attorney with the service price removed
     */
    public Attorneys deleteServicePrice(Attorneys attorney, String clientID) {
        logger.debug("Deleting service price for client ID: {} from attorney ID: {}", 
                clientID, attorney.getAttorneyId());

        if (!attorney.removeServicePricing(clientID)) {
            logger.warn("No service pricing found for client ID: {} in attorney ID: {}", 
                    clientID, attorney.getAttorneyId());
        }

        return attorney;
    }

    /**
     * Creates a new attorney with the provided data and a UUID.
     * @param attorney The attorney data
     * @param attorneyID The UUID to assign to the attorney
     * @return The new attorney with the assigned UUID
     */
    public Attorneys createWithId(Attorneys attorney, String attorneyID) {
        logger.debug("Creating new attorney with ID: {}", attorneyID);
        return Attorneys.builder()
                .attorneyId(attorneyID)
                .firstName(attorney.getFirstName())
                .lastName(attorney.getLastName())
                .servicePricing(attorney.getServicePricing())
                .build();
    }
}
