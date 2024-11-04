ContactAddressSync: execution of BeforeUpdate caused by: System.DmlException: Update failed. First exception on row 0 with id 0038G00000iWuDJQA0; first error: CANNOT_INSERT_UPDATE_ACTIVATE_ENTITY, ContactAddressSync: execution of BeforeUpdate caused by: System.DmlException: Update failed. First exception on row 0 with id 0038G00000iWuMKQA0; first error: SELF_REFERENCE_FROM_TRIGGER, Object (id = 0038G00000iWuMK) is currently in trigger ContactAddressSync, therefore it cannot recursively update itself: [] Trigger.ContactAddressSync: line 65, column 1: [] Trigger.ContactAddressSync: line 65, column 1






trigger ContactAddressSync on Contact (before update) {
    System.debug('ContactAddressSync trigger fired');

    // Maps to track contacts to update by email
    Map<String, List<Contact>> contactsByEmail = new Map<String, List<Contact>>();
    List<Contact> contactsToUpdate = new List<Contact>();

    // Step 1: Collect emails of contacts where address fields have changed
    Set<String> changedEmails = new Set<String>();

    for (Contact contact : Trigger.new) {
        Contact oldContact = Trigger.oldMap.get(contact.Id);
        // Check if any of the address fields have changed
        if (contact.Email != null && (contact.MailingStreet != oldContact.MailingStreet || 
                                      contact.MailingCity != oldContact.MailingCity || 
                                      contact.MailingPostalCode != oldContact.MailingPostalCode)) {
            changedEmails.add(contact.Email);
        }
    }

    // Step 2: Query all contacts with the same email as changed contacts
    if (!changedEmails.isEmpty()) {
        for (Contact relatedContact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode 
                                       FROM Contact WHERE Email IN :changedEmails]) {
            if (!contactsByEmail.containsKey(relatedContact.Email)) {
                contactsByEmail.put(relatedContact.Email, new List<Contact>());
            }
            contactsByEmail.get(relatedContact.Email).add(relatedContact);
        }
    }

    // Step 3: Sync address fields for contacts with the same email
    for (Contact contact : Trigger.new) {
        if (contact.Email != null && contactsByEmail.containsKey(contact.Email)) {
            List<Contact> relatedContacts = contactsByEmail.get(contact.Email);

            // Get the updated address fields
            String updatedStreet = contact.MailingStreet;
            String updatedCity = contact.MailingCity;
            String updatedPostalCode = contact.MailingPostalCode;

            for (Contact relatedContact : relatedContacts) {
                // Skip if this is the contact being updated
                if (relatedContact.Id == contact.Id) continue;

                // Update address fields if they differ from the updated contact
                if (relatedContact.MailingStreet != updatedStreet || 
                    relatedContact.MailingCity != updatedCity || 
                    relatedContact.MailingPostalCode != updatedPostalCode) {
                    
                    relatedContact.MailingStreet = updatedStreet;
                    relatedContact.MailingCity = updatedCity;
                    relatedContact.MailingPostalCode = updatedPostalCode;
                    contactsToUpdate.add(relatedContact); // Add to list for bulk update
                    
                    System.debug('Updating Contact address for Email ' + relatedContact.Email + ': ' + relatedContact);
                }
            }
        }
    }

    // Step 4: Perform bulk update of contacts with changed addresses
    if (!contactsToUpdate.isEmpty()) {
        System.debug('Updating contacts with synchronized addresses: ' + contactsToUpdate);
        update contactsToUpdate;
    }
}







trigger UserAddressTrigger on User (before  insert, before  update) {
     System.debug('UserContactSyncTrigger fired');
    // Maps to hold user and contact data
    Map<String, Contact> contactsByEmail = new Map<String, Contact>();
    Map<String, User> usersByEmail = new Map<String, User>();

    // List to hold new contacts to be inserted after the loop
    List<Contact> newContacts = new List<Contact>();

    // Step 1: Populate the maps with existing Contacts and Users by email
    if (Trigger.isInsert || Trigger.isUpdate) {
        Set<String> userEmails = new Set<String>();
        
        for (User user : Trigger.new) {
            if (user.Email != null) {
                userEmails.add(user.Email);
            }
        }

        // Query existing contacts with the same emails
        for (Contact contact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode FROM Contact WHERE Email IN :userEmails]) {
            contactsByEmail.put(contact.Email, contact);
        }
        
        // Query existing users with the same emails
        for (User existingUser : [SELECT Id, Email, Street, City, PostalCode FROM User WHERE Email IN :userEmails]) {
            usersByEmail.put(existingUser.Email, existingUser);
        }
    }

    // Step 2: Create or Update User, and synchronize with Contact
    for (User user : Trigger.new) {
        // Skip processing if email is null
        if (user.Email == null) {
            continue;
        }

        if (Trigger.isInsert || (Trigger.isUpdate && user.Email != null)) {
            if (usersByEmail.containsKey(user.Email)) {
                // User exists, update the address fields
                User existingUser = usersByEmail.get(user.Email);
                existingUser.Street = user.Street;
                existingUser.City = user.City;
                existingUser.PostalCode = user.PostalCode;
            }

            // Step 3: Check for Contact by Email and sync address if needed
            if (contactsByEmail.containsKey(user.Email)) {
                // Contact exists, update the address fields if they have changed
                Contact existingContact = contactsByEmail.get(user.Email);
                
                existingContact.Contact_Address_Updated__c = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || existingContact.MailingPostalCode != user.PostalCode) {
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    existingContact.Contact_Address_Updated__c = true;
                }

                if (existingContact.Contact_Address_Updated__c) {
                    // Update existing contact in the map
                    contactsByEmail.put(existingContact.Email, existingContact);
                }

                // Log the existing contact
                System.debug('Updating existing Contact: ' + existingContact);
            } else {
                // No Contact exists, create a new Contact with User details
                Contact newContact = new Contact(
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    MailingStreet = user.Street,
                    MailingCity = user.City,
                    MailingPostalCode = user.PostalCode
                );

                // Check if Email is filled out before adding to newContacts list
                if (String.isBlank(newContact.Email)) {
                    user.addError('The Email field is required for creating a new Contact.');
                } else {
                    contactsByEmail.put(user.Email, newContact); // Track created contact
                    newContacts.add(newContact); // Add to list for insertion
                    
                    // Log the new contact
                    System.debug('Creating new Contact: ' + newContact);
                }
            }
        }
    }

    // Insert all new contacts after the loop
    if (!newContacts.isEmpty()) {
        System.debug('Inserting new Contacts: ' + newContacts);
        insert newContacts;
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        System.debug('Updating Contacts with changed addresses: ' + contactsByEmail.values());
        update contactsByEmail.values();
    }
}
