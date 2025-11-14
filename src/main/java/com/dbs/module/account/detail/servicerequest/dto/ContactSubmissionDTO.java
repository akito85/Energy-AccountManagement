package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for contact submission in service request
 * Supports both existing contacts and new contact creation
 */
@Data
public class ContactSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // For existing contact
    private Integer contactId;

    // For new contact creation
    private String contactName;
    private Integer contactType;
    private String phoneNumber;
    private String email;
    private String address;
    private Integer jobId;
    private Integer positionId;

    // Contact relationship
    private Boolean isPrimary = false;
    private String relationshipType;
    private String additionalNote;

    /**
     * Check if this is a new contact that needs to be created
     * @return true if contactId is null and contactName is provided
     */
    public boolean isNewContact() {
        return contactId == null && contactName != null && !contactName.trim().isEmpty();
    }
}
