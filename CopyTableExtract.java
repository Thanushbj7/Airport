"Integrated the Genisys team into the retirement application by implementing a seamless workflow where, upon collection of basic customer details by Genisys representatives and confirming the data, the process transitions smoothly to the retirement application, ensuring a cohesive and efficient user experience."

@isTest
public class ContactTriggerHandlerTest {
    @isTest
    static void testHandleTrigger() {
        // Setup test data
        List<Contact> contacts = new List<Contact>();
        for (Integer i = 0; i < 5; i++) {
            contacts.add(new Contact(FirstName = 'Test', LastName = 'User' + i, Email = 'test' + i + '@example.com'));
        }
        insert contacts;

        // Start the test
        Test.startTest();

        // Test Insert Trigger
        ContactTriggerHandler.handleTrigger(true, false, false, true, contacts, null);

        // Update Contacts
        for (Contact con : contacts) {
            con.FirstName = 'Updated Test';
        }
        update contacts;

        // Test Update Trigger
        ContactTriggerHandler.handleTrigger(false, true, false, true, contacts, null);

        // Delete Contacts
        delete contacts;

        // Test Delete Trigger
        ContactTriggerHandler.handleTrigger(false, false, true, true, null, contacts);

        // Undelete Contacts
        undelete contacts;

        // Test Undelete Trigger
        ContactTriggerHandler.handleTrigger(false, false, false, true, contacts, null);

        // End the test
        Test.stopTest();

        // Assert email limit usage
        System.assertEquals(20, Limits.getEmailInvocations(), 'Emails should be sent for all trigger operations');
    }
}




  
public class ContactTriggerHandler {
    public static void handleTrigger(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isAfter, 
                                     List<Contact> newContacts, List<Contact> oldContacts) {
        if (isAfter) {
            if (isInsert) {
                notifyCreated(newContacts);
            } else if (isUpdate) {
                notifyModified(newContacts);
            } else if (isDelete) {
                notifyDeleted(oldContacts);
            } else if (!isInsert && !isUpdate && !isDelete) {
                notifyUndelete(newContacts);
            }
        }
    }

    private static void notifyCreated(List<Contact> newContacts) {
        // Prepare batched email
        List<String> emailAddresses = new List<String>();
        List<String> emailBodies = new List<String>();
        
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                emailAddresses.add(contact.Email);
                emailBodies.add('Dear ' + contact.Name + ',\n\nYour contact record has been created.\n\nBest regards,\nYour Team');
            }
        }

        sendBatchEmails(emailAddresses, 'Welcome! Your Contact Record has been Created', emailBodies);
    }

    private static void notifyModified(List<Contact> newContacts) {
        List<String> emailAddresses = new List<String>();
        List<String> emailBodies = new List<String>();

        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                emailAddresses.add(contact.Email);
                emailBodies.add('Dear ' + contact.Name + ',\n\nYour contact record has been modified.\n\nBest regards,\nYour Team');
            }
        }

        sendBatchEmails(emailAddresses, 'Your Contact Record has been Updated', emailBodies);
    }

    private static void notifyDeleted(List<Contact> oldContacts) {
        List<String> emailAddresses = new List<String>();
        List<String> emailBodies = new List<String>();

        for (Contact contact : oldContacts) {
            if (contact.Email != null) {
                emailAddresses.add(contact.Email);
                emailBodies.add('Dear ' + contact.FirstName + ',\n\nYour contact record has been deleted.\n\nBest regards,\nYour Team');
            }
        }

        sendBatchEmails(emailAddresses, 'Your Contact Record has been Deleted', emailBodies);
    }

    private static void notifyUndelete(List<Contact> newContacts) {
        List<String> emailAddresses = new List<String>();
        List<String> emailBodies = new List<String>();

        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                emailAddresses.add(contact.Email);
                emailBodies.add('Dear ' + contact.Name + ',\n\nYour contact record has been restored.\n\nBest regards,\nYour Team');
            }
        }

        sendBatchEmails(emailAddresses, 'Your Contact Record has been Restored', emailBodies);
    }

    private static void sendBatchEmails(List<String> emailAddresses, String subject, List<String> bodies) {
        if (emailAddresses.isEmpty()) return;

        List<Messaging.SingleEmailMessage> emailMessages = new List<Messaging.SingleEmailMessage>();
        
        for (Integer i = 0; i < emailAddresses.size(); i++) {
            Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
            email.setToAddresses(new String[] { emailAddresses[i] });
            email.setSubject(subject);
            email.setPlainTextBody(bodies[i]);
            emailMessages.add(email);
        }
        
        // Ensure we don't exceed governor limits
        if (emailMessages.size() <= Limits.getEmailInvocations()) {
            Messaging.sendEmail(emailMessages);
        } else {
            throw new LimitException('Too many emails in one transaction.');
        }
    }
          }
