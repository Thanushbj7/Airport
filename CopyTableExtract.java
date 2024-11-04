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
