trigger UserContactSync on User (before insert, before update) {
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
                
                Boolean addressChanged = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || existingContact.MailingPostalCode != user.PostalCode) {
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    addressChanged = true;
                }

                if (addressChanged) {
                    // Update existing contact in the map
                    contactsByEmail.put(existingContact.Email, existingContact);
                }
            } else if (!contactsByEmail.containsKey(user.Email)) {
                // No Contact exists, create a new Contact with User details only if not already created for this email
                Contact newContact = new Contact(
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    MailingStreet = user.Street,
                    MailingCity = user.City,
                    MailingPostalCode = user.PostalCode
                );
                contactsByEmail.put(user.Email, newContact); // Add to map to track created contact
                newContacts.add(newContact); // Add to list for insertion
            }
        }
    }

    // Insert all new contacts after the loop
    if (!newContacts.isEmpty()) {
        insert newContacts;
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        update contactsByEmail.values();
    }
}









trigger UserContactSync on User (before insert, before update) {
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
                
                Boolean addressChanged = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || existingContact.MailingPostalCode != user.PostalCode) {
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    addressChanged = true;
                }

                if (addressChanged) {
                    // Add the contact to the update map for further processing
                    contactsByEmail.put(existingContact.Email, existingContact);
                }
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
                newContacts.add(newContact); // Add to list instead of inserting immediately
            }
        }
    }

    // Insert all new contacts after the loop
    if (!newContacts.isEmpty()) {
        insert newContacts;
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        update contactsByEmail.values();
    }
}








trigger UserContactSync on User (before insert, before update) {
    // Maps to hold user and contact data
    Map<String, Contact> contactsByEmail = new Map<String, Contact>();
    Map<String, User> usersByEmail = new Map<String, User>();

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
                
                Boolean addressChanged = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || existingContact.MailingPostalCode != user.PostalCode) {
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    addressChanged = true;
                }

                if (addressChanged) {
                    // Add the contact to the update list
                    contactsByEmail.put(existingContact.Email, existingContact);
                }
            } else {
                // No Contact exists, so create a new Contact with User details
                Contact newContact = new Contact(
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    MailingStreet = user.Street,
                    MailingCity = user.City,
                    MailingPostalCode = user.PostalCode
                );
                insert newContact;
            }
        }
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        update contactsByEmail.values();
    }
}
