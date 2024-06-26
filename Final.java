System.AssertException: Assertion Failed: User Name should match the logged-in user: Expected: null, Actual: Test User



@isTest
private class YourClassName_Test {
    @isTest
    static void testGetLoggedInUserDetails() {
        // Create a test user
        Profile testProfile = [SELECT Id FROM Profile WHERE Name = 'Standard User'];
        User testUser = new User(
            ProfileId = testProfile.Id,
            FirstName = 'Test',
            LastName = 'User',
            Email = 'testuser@example.com',
            Username = 'testuser@example.com' + System.currentTimeMillis(),
            Alias = 'tuser',
            TimeZoneSidKey = 'America/Los_Angeles',
            LocaleSidKey = 'en_US',
            EmailEncodingKey = 'UTF-8',
            LanguageLocaleKey = 'en_US',
            Employee_ID__c = '123', // Populate with required field value
            Finra_CRD__c = '456', // Populate with required field value
            Entity__c = 'Test Entity' // Populate with required field value
        );
        insert testUser;

        // Set the test user as the running user
        System.runAs(testUser) {
            // Call the method being tested
            Test.startTest();
            User result = YourClassName.getLoggedInUserDetails();
            Test.stopTest();

            // Verify the result
            System.assertEquals(testUser.Id, result.Id, 'User Id should match the logged-in user');
            System.assertEquals(testUser.Name, result.Name, 'User Name should match the logged-in user');
            // Add more assertions as needed
        }
    }
}




System.DmlException: Insert failed. First exception on row 0; first error: FIELD_CUSTOM_VALIDATION_EXCEPTION, Either the Employee ID, Finra CRD, or entity is required: []




@isTest
private class YourClassName_Test {
    @isTest
    static void testGetLoggedInUserDetails() {
        // Create a test user
        Profile testProfile = [SELECT Id FROM Profile WHERE Name = 'Standard User'];
        User testUser = new User(
            ProfileId = testProfile.Id,
            FirstName = 'Test',
            LastName = 'User',
            Email = 'testuser@example.com',
            Username = 'testuser@example.com' + System.currentTimeMillis(),
            Alias = 'tuser',
            TimeZoneSidKey = 'America/Los_Angeles',
            LocaleSidKey = 'en_US',
            EmailEncodingKey = 'UTF-8',
            LanguageLocaleKey = 'en_US'
        );
        insert testUser;

        // Set the test user as the running user
        System.runAs(testUser) {
            // Call the method being tested
            Test.startTest();
            User result = YourClassName.getLoggedInUserDetails();
            Test.stopTest();

            // Verify the result
            System.assertEquals(testUser.Id, result.Id, 'User Id should match the logged-in user');
            System.assertEquals(testUser.Name, result.Name, 'User Name should match the logged-in user');
            // Add more assertions as needed
        }
    }
}




public static User getLoggedInUserDetails() {
        User loggedInUser = [SELECT Id, Name
                             FROM User 
                             WHERE Id = :UserInfo.getUserId()];
        return loggedInUser;
    }




@isTest
private class YourClassName_Test {
    @isTest
    static void testDynamicClientOfferQuery() {
        // Create test Client_Offer__c records
        List<Client_Offer__c> clientOffers = new List<Client_Offer__c>{
            new Client_Offer__c(Account_Ext_Id__c = '123456789', Field1__c = 'Value1', Field2__c = 'Value2'),
            new Client_Offer__c(Account_Ext_Id__c = '987654321', Field1__c = 'Value3', Field2__c = 'Value4')
            // Add more records as needed
        };
        insert clientOffers;

        // Create a set of SSNs to query
        Set<String> testSSNs = new Set<String>{'123456789'};
        // Add more SSNs as needed

        // Call the method being tested
        Test.startTest();
        List<Client_Offer__c> result = YourClassName.dynamicClientOfferQuery(testSSNs);
        Test.stopTest();

        // Verify the result
        System.assertEquals(1, result.size(), 'Expected one Client_Offer__c record');
        // Add more assertions based on the expected behavior
    }
}





public static List<Client_Offer__c> dynamicClientOfferQuery(Set<String> ssnSet) {
        String fields = '';
        for (String field : Schema.SObjectType.Client_Offer__c.fields.getMap().keySet()) {
            fields += field + ',';
        }
        fields = fields.substring(0,fields.length()-1);
        
        String ssns = '';
        for (String ssn : ssnSet) {
            ssns += '\''+ ssn + '\',';
        }
        ssns = ssns.substring(0,ssns.length()-1);

        if (ssns == null || ssns.length()<1 || fields == null || fields.length()<1) {
            return null;
        }  
        
        String query = 'select ' + fields;
        query += ' from client_offer__c where account_ext_id__c in (' + ssns + ') and account_ext_id__c != null';
        System.debug('DEBUG query - ' + query);
        return Database.query(query);       
    }


@isTest
private class YourClassName_Test {
    @isTest
    static void testGetCampaignOfferDoNotShowOfferMap() {
        // Create test Campaign_Offer_Summary__c records
        Campaign_Offer_Summary__c cos1 = new Campaign_Offer_Summary__c(
            OfferCode__c = 'Offer1',
            Present_Message__c = false,
            Planid_Text__c = 'Plan1',
            Customer_SSN__c = '123456789'
        );
        // Add more records as needed
        
        // Insert the test records
        insert new List<Campaign_Offer_Summary__c>{cos1};
        // Insert more records as needed
        
        // Create a set of SSNs to query
        Set<String> testSSNs = new Set<String>{'123456789'};
        // Add more SSNs as needed
        
        // Call the method being tested
        Test.startTest();
        Map<String, Campaign_Offer_Summary__c> result = YourClassName.getCampaignOfferDoNotShowOfferMap(testSSNs);
        Test.stopTest();
        
        // Verify the result
        System.assertEquals(1, result.size(), 'Map size should be 1');
        // Add more assertions based on the expected behavior
        
        // Example assertion for key existence
        System.assertTrue(result.containsKey('123456789_Plan1_Offer1'), 'Key should exist in the map');
    }
}




public static Map<String, Campaign_Offer_Summary__c> getCampaignOfferDoNotShowOfferMap(Set<String> ssnSet) {
        Map<String, Campaign_Offer_Summary__c> ssnCampaignOfferMap = new Map<String, Campaign_Offer_Summary__c>();
        String key = null;
        
        List<Campaign_Offer_Summary__c> coSummaryList = [select OfferCode__c, Present_Message__c, Planid_Text__c, Customer_SSN__c from Campaign_Offer_Summary__c where Customer_SSN__c in: ssnSet];
        for(Campaign_Offer_Summary__c cos : coSummaryList) {
            //Fill this up with only 'D0-NOT-SHOWS'
            if(!cos.Present_Message__c && !String.isBlank(cos.OfferCode__c) && !String.isBlank(cos.Planid_Text__c)) {
                key = cos.Customer_SSN__c + ConstantUtils.UNIQUE_SEPERATOR + cos.Planid_Text__c + ConstantUtils.UNIQUE_SEPERATOR + cos.OfferCode__c;
                
                ssnCampaignOfferMap.put(key, cos);
            }
        }
        
        return ssnCampaignOfferMap;       
    }



System.DmlException: Insert failed. First exception on row 0; first error: FIELD_CUSTOM_VALIDATION_EXCEPTION, SSN/TIN Field must be a numeric &amp;amp; 9 digits.: [SSN__c]



@isTest
private class YourTestClass {
    
    @isTest
    static void testGetOpenClientOffers() {
        // Test when clientOpenOfferList is null
        Test.startTest();
        List<vfClientOffer> nullClientOffers = YourClassName.getOpenClientOffers('TestSSN');
        Test.stopTest();
        System.assertEquals(new List<vfClientOffer>(), nullClientOffers, 'The method should return an empty list when clientOpenOfferList is null');
        
        // Test when clientOpenOfferList is empty
        Test.startTest();
        List<Client_Offer__c> emptyClientOfferList = new List<Client_Offer__c>();
        // Mock dynamicClientOfferQuery method to return emptyClientOfferList
        List<vfClientOffer> emptyClientOffers = YourClassName.getOpenClientOffers('TestSSN');
        Test.stopTest();
        System.assertEquals(new List<vfClientOffer>(), emptyClientOffers, 'The method should return an empty list when clientOpenOfferList is empty');
        
        // Test when clientOpenOfferList contains records and valid campaign data
        Test.startTest();
        // Implement test data setup with relevant Client_Offer__c records and campaign data
        // Mock dynamicClientOfferQuery and getCampaignOfferDoNotShowOfferMap methods to return appropriate data
        List<vfClientOffer> clientOffers = YourClassName.getOpenClientOffers('TestSSN');
        Test.stopTest();
        // Implement assertions based on the constructed vfClientOfferList
        // Ensure to verify the values of vfClientOffer attributes such as offerCode, offerName, etc.
    }
}




private static List<vfClientOffer> getOpenClientOffers(String ssn) {
        List<vfClientOffer> vfClientOfferList = new List<vfClientOffer>();
        
        List<Client_Offer__c> clientOpenOfferList = dynamicClientOfferQuery(new Set<String>{ssn});
        if(clientOpenOfferList == null || clientOpenOfferList.size() == 0)
            return vfClientOfferList;
        
        Client_Offer__c co = clientOpenOfferList[0];
        
        Map<String, Campaign_Offer_Summary__c> campaignOfferDoNotShowOfferMap = getCampaignOfferDoNotShowOfferMap(new Set<String>{ssn});
        
        System.debug('co ::::: ' + co);
        String planId = null, key = null;
        Campaign_Offer_Summary__c offerSummary = null;
        for (Campaign c : [select id, name, offer_code__c, offer_priority__c from campaign where offer_code__c != null limit 50]) {
            try { 
                //Check data in Campaign Offer Summary Object
                //Match on PlanID and Offer Code
                
                planId = (String)co.get('PlanId_' + c.offer_code__c + '__c');
                key = ssn + ConstantUtils.UNIQUE_SEPERATOR + planId + ConstantUtils.UNIQUE_SEPERATOR + c.offer_code__c;
                
                offerSummary = campaignOfferDoNotShowOfferMap.get(key);
                //Case# 16921 && Case# 11899 - Rahul Sahay - 07/17/2013 
                if(offerSummary != null && planId == offerSummary.Planid_Text__c && c.offer_code__c == offerSummary.OfferCode__c)
                        continue;
                
                if (((String)co.get('status_' + c.offer_code__c + '__c') == 'Open') && ((Decimal)co.get('score_' + c.offer_code__c + '__c') != null)) {
                    VfClientOffer vf = new VfClientOffer();
                    vf.offerCode = c.offer_code__c;
                    vf.offerName = c.name;                    
                    vf.offerPriority = c.offer_priority__c;
                    vf.offerScore = (Decimal)co.get('score_' + c.offer_code__c + '__c');
                    vf.offerPlanId = (String)co.get('planid_' + c.offer_code__c + '__c');
                    vf.offerPlanName = (String)co.get('PlanName_' + c.offer_code__c + '__c');
                    
                    //Case # 00011325: Added new field to show the Avtive Mailer information on the Targeted Messages list on the Offer Page.
                    vf.activeMailer = (String)co.get('Active_Mailer_' + c.offer_code__c + '__c');
                   
                    vf.offerCampaign = c.id;
                    vfClientOfferList.add(vf);//---this is need to display in UI
                }
            } catch (Exception e) {
                // this exception means the user has inserted a campaign with an offer_code__c but has not inserted the corresponding fields on the client_offer__c object
                // swallow 
            }
        }        
        
        System.debug('vfClientOfferList :::: ' + vfClientOfferList);
        
        return vfClientOfferList;
    }






public static List<Client_Offer__c> dynamicClientOfferQuery(Set<String> ssnSet) {
        String fields = '';
        for (String field : Schema.SObjectType.Client_Offer__c.fields.getMap().keySet()) {
            fields += field + ',';
        }
        fields = fields.substring(0,fields.length()-1);
        
        String ssns = '';
        for (String ssn : ssnSet) {
            ssns += '\''+ ssn + '\',';
        }
        ssns = ssns.substring(0,ssns.length()-1);

        if (ssns == null || ssns.length()<1 || fields == null || fields.length()<1) {
            return null;
        }  
        
        String query = 'select ' + fields;
        query += ' from client_offer__c where account_ext_id__c in (' + ssns + ') and account_ext_id__c != null';
        System.debug('DEBUG query - ' + query);
        return Database.query(query);       
    }
    
 
    
    
     public static Map<String, Campaign_Offer_Summary__c> getCampaignOfferDoNotShowOfferMap(Set<String> ssnSet) {
        Map<String, Campaign_Offer_Summary__c> ssnCampaignOfferMap = new Map<String, Campaign_Offer_Summary__c>();
        String key = null;
        
        List<Campaign_Offer_Summary__c> coSummaryList = [select OfferCode__c, Present_Message__c, Planid_Text__c, Customer_SSN__c from Campaign_Offer_Summary__c where Customer_SSN__c in: ssnSet];
        for(Campaign_Offer_Summary__c cos : coSummaryList) {
            //Fill this up with only 'D0-NOT-SHOWS'
            if(!cos.Present_Message__c && !String.isBlank(cos.OfferCode__c) && !String.isBlank(cos.Planid_Text__c)) {
                key = cos.Customer_SSN__c + ConstantUtils.UNIQUE_SEPERATOR + cos.Planid_Text__c + ConstantUtils.UNIQUE_SEPERATOR + cos.OfferCode__c;
                
                ssnCampaignOfferMap.put(key, cos);
            }
        }
        
        return ssnCampaignOfferMap;       
    }



@isTest
private class YourTestClass {
    // Test method to cover the sortClientOffers method
    static testMethod void testSortClientOffers() {
        // Create test data
        // Create vfClientOffer objects with different priorities and scores
        YourClassName.vfClientOffer offer1 = new YourClassName.vfClientOffer();
        offer1.offerName = 'Offer 1';
        offer1.offerPriority = 1;
        offer1.offerScore = 10;
        
        YourClassName.vfClientOffer offer2 = new YourClassName.vfClientOffer();
        offer2.offerName = 'Offer 2';
        offer2.offerPriority = 2;
        offer2.offerScore = 20;
        
        YourClassName.vfClientOffer offer3 = new YourClassName.vfClientOffer();
        offer3.offerName = 'Offer 3';
        offer3.offerPriority = 2;
        offer3.offerScore = 50;
        
        YourClassName.vfClientOffer offer4 = new YourClassName.vfClientOffer();
        offer4.offerName = 'Offer 4';
        offer4.offerPriority = 1;
        offer4.offerScore = 20;
        
        List<YourClassName.vfClientOffer> unsortedOffers = new List<YourClassName.vfClientOffer>{offer1, offer2, offer3, offer4};
        
        // Call the sortClientOffers method
        List<YourClassName.vfClientOffer> sortedOffers = YourClassName.sortClientOffers(unsortedOffers);
        
        // Assert that the method sorts the offers correctly
        // The expected order should be: Offer 4, Offer 1, Offer 3, Offer 2
        
        System.assertEquals('Offer 4', sortedOffers[0].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 1', sortedOffers[1].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 3', sortedOffers[2].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 2', sortedOffers[3].offerName, 'The method should sort offers by priority and score');
    }
}






@isTest
private class YourTestClass {
    // Test method to cover the sortClientOffers method
    static testMethod void testSortClientOffers() {
        // Create test data
        // Create vfClientOffer objects with different priorities and scores
        vfClientOffer offer1 = new vfClientOffer(offerName = 'Offer 1', offerPriority = 1, offerScore = 10);
        vfClientOffer offer2 = new vfClientOffer(offerName = 'Offer 2', offerPriority = 2, offerScore = 20);
        vfClientOffer offer3 = new vfClientOffer(offerName = 'Offer 3', offerPriority = 2, offerScore = 50);
        vfClientOffer offer4 = new vfClientOffer(offerName = 'Offer 4', offerPriority = 1, offerScore = 20);
        
        List<vfClientOffer> unsortedOffers = new List<vfClientOffer>{offer1, offer2, offer3, offer4};
        
        // Call the sortClientOffers method
        List<vfClientOffer> sortedOffers = YourClassName.sortClientOffers(unsortedOffers);
        
        // Assert that the method sorts the offers correctly
        // The expected order should be: Offer 4, Offer 1, Offer 3, Offer 2
        
        System.assertEquals('Offer 4', sortedOffers[0].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 1', sortedOffers[1].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 3', sortedOffers[2].offerName, 'The method should sort offers by priority and score');
        System.assertEquals('Offer 2', sortedOffers[3].offerName, 'The method should sort offers by priority and score');
    }
}




private static List<vfClientOffer> sortClientOffers(List<vfClientOffer> col) {
        // this method sorts by both offerPriority and offerScore
        // lowest offerPriorty takes priority, highest offerScore takes priority
        // given the following list: {offerPriority -> offerScore}
        // {1 -> 10}
        // {2 -> 20}
        // {2 -> 50}
        // {1 -> 20}
        // this method will produce the following ordered list:
        // {1 -> 20}
        // {1 -> 10}
        // {2 -> 50}
        // {2 -> 20}    
        //
        // this method could be tweaked to perform better, but since the max list size will probably be
        // no larger than 20, it's probably ok.
        
        // sort by offer score
        System.debug('Templist: ' + col.size());    
        List<Decimal> vfclientOfferScoreList = new List<Decimal>();
        for (vfClientOffer vfco : col) {
            System.debug('Add to decimal list: OfferPriority:' + vfco.offerPriority + '; SortedScoreList: ' + vfco.offerScore + ';OfferName: '+vfco.offerName);
            vfclientOfferScoreList.add(vfco.offerScore);
        }
        vfClientOfferScoreList.sort();
        
        // revers sort so offer scores are descending (higher offer score means higher priority)
        List<Decimal> reverseSortList = new Decimal[vfClientOfferScoreList.size()];
        Integer j = 0;
        for (Integer i=vfClientOfferScoreList.size()-1; i>=0 ;i--) {
            reverseSortList[j] = vfClientOfferScoreList[i];
            j++;
        }       
        
        List<vfClientOffer> sortedScoreList = new List<vfClientOffer>();        
        for (Decimal d : reverseSortList) {
            Integer index = 0;
            for (vfClientOffer vfco : col) {
                if (vfco.offerScore == d) {
                    System.debug('OfferPriority:' + vfco.offerPriority + '; SortedScoreList: ' + vfco.offerScore + ';OfferName: '+vfco.offerName);
                    sortedScoreList.add(vfco);
                    break;
                }
                index++;
            }
            col.remove(index);
        }
        
        // create map of ordere offers based on campaign priority       
        Map<Decimal, List<vfClientOffer>> offerPriorityMap = new Map<Decimal, List<vfClientOffer>>();
        for (vfClientOffer vfco : sortedScoreList) {
            if (offerPriorityMap.containsKey(vfco.offerPriority)) {
                List<vfClientOffer> newList = offerPriorityMap.get(vfco.offerPriority);
                newList.add(vfco);
                offerPriorityMap.put(vfco.offerPriority, newList);
                System.debug('Add to existing map - OfferPriority: ' + vfco.offerPriority + '; Score:' + vfco.offerScore);
            } else {
                offerPriorityMap.put(vfco.offerPriority,new List<vfClientOffer>{vfco});
                System.debug('New map - OfferPriority: ' + vfco.offerPriority + '; Score:' + vfco.offerScore);
            }
        }

        // sort the offer priority
        List<Decimal> offerPriorityList = new List<Decimal>();
        for (Decimal d : offerPriorityMap.keySet()) {
            offerPriorityList.add(d);
        }
        offerPriorityList.sort();
        
        // create the final list sorted by offer priority, then by offer score
        List<vfClientOffer> finalList = new List<vfClientOffer>();
        for (Decimal d : offerPriorityList) {
            System.debug('OfferPriorityList: ' + d);
            for (vfClientOffer vfco : offerPriorityMap.get(d)) {
                finalList.add(vfco);
            }
        }       
        return finalList;
    }


@isTest
private class YourTestClass {
    // Test method to cover the initClientOffers method
    static testMethod void testInitClientOffers() {
        // Test scenario: clientSSN is provided
        // Call the initClientOffers method with a valid clientSSN
        List<VfClientOffer> offersWithSSN = YourClassName.initClientOffers(null, 'TestSSN');
        
        // Assert that the method retrieves client offers based on the provided SSN
        // Add assertions as needed
        
        // Test scenario: clientSSN is not provided but recId is provided
        // Create a mock Account record with SSN
        Account testAccount = new Account(Name = 'Test Account', SSN__c = 'TestSSN');
        insert testAccount;
        
        // Call the initClientOffers method with a blank clientSSN and a valid recId
        List<VfClientOffer> offersWithRecId = YourClassName.initClientOffers(testAccount.Id, null);
        
        // Assert that the method retrieves client offers based on the SSN of the provided Account record
        // Add assertions as needed
        
        // Test scenario: both clientSSN and recId are blank
        // Call the initClientOffers method with blank clientSSN and recId
        List<VfClientOffer> offersWithBlankInputs = YourClassName.initClientOffers(null, null);
        
        // Assert that the method returns an empty list when both clientSSN and recId are blank
        // Add assertions as needed
    }
}




public class vfClientOffer {
         @AuraEnabled public String offerName {get; set;}
         @AuraEnabled public Decimal offerScore {get; set;}
         @AuraEnabled public Decimal offerPriority {get; set;}
         @AuraEnabled public String offerPlanId {get; set;}
         @AuraEnabled public String offerPlanName {get; set;}
         @AuraEnabled public String offerCode {get; set;}
         @AuraEnabled public String offerCampaign {get; set;}
        
        //Case # 00011325: Added new field to show the Avtive Mailer information on the Targeted Messages list on the Offer Page.  
         @AuraEnabled public String activeMailer {get; set;}
    }


@isTest
private class YourTestClass {
    // Test method to cover the getCampaignId method
    static testMethod void testGetCampaignId() {
        // Create test data
        // Create a Campaign record
        Campaign testCampaign = new Campaign(Name = 'Test Campaign');
        insert testCampaign;
        
        // Call the getCampaignId method with the test Campaign name
        Campaign retrievedCampaign = YourClassName.getCampaignId('Test Campaign');
        
        // Assert that the method retrieves the correct Campaign Id
        System.assertEquals(testCampaign.Id, retrievedCampaign.Id, 'The method should retrieve the correct Campaign Id');
    }
}



public static Campaign getCampaignId(String campaignName) {
        return [ SELECT Id
            FROM Campaign WHERE Name= :campaignName
            LIMIT 1 ];
    } 



@isTest
private class YourTestClass {
    // Test method to cover the getCampaignNames method
    static testMethod void testGetCampaignNames() {
        // Create test data
        // Create two Campaign records, one with Create_Manual_Offer__c true and one with false
        Campaign testCampaign1 = new Campaign(Name = 'Test Campaign 1', Create_Manual_Offer__c = true);
        Campaign testCampaign2 = new Campaign(Name = 'Test Campaign 2', Create_Manual_Offer__c = false);
        insert new List<Campaign>{testCampaign1, testCampaign2};
        
        // Call the getCampaignNames method
        List<Campaign> campaignNames = YourClassName.getCampaignNames();
        
        // Assert that the method retrieves only campaigns with Create_Manual_Offer__c true
        System.assertEquals(1, campaignNames.size(), 'The method should retrieve campaigns with Create_Manual_Offer__c true');
        
        // Assert that the method retrieves campaigns in alphabetical order by name
        System.assertEquals('Test Campaign 1', campaignNames[0].Name, 'The method should retrieve campaigns in alphabetical order by name');
    }
}





public static List<Campaign> getCampaignNames() {
    List<Campaign> camList= [Select Id, Name from Campaign where Create_Manual_Offer__c=true order by Name];
        return camList ;
    }



@isTest
private class YourTestClass {
    // Test method to cover the initClientOffers method
    static testMethod void testInitClientOffers() {
        // Test scenario: clientSSN is not blank
        // Call the initClientOffers method with a non-blank clientSSN
        List<VfClientOffer> offersWithSSN = YourClassName.initClientOffers(null, 'TestSSN');
        
        // Assert that the method retrieves client offers based on the provided SSN
        System.assertNotEquals(null, offersWithSSN, 'The method should retrieve client offers based on the provided SSN');
        
        // Test scenario: clientSSN is blank, but recId is provided
        // Create a mock Account record with SSN
        Account testAccount = new Account(Name = 'Test Account', SSN__c = 'TestSSN');
        insert testAccount;
        
        // Call the initClientOffers method with a blank clientSSN and a valid recId
        List<VfClientOffer> offersWithRecId = YourClassName.initClientOffers(testAccount.Id, null);
        
        // Assert that the method retrieves client offers based on the SSN of the provided Account record
        System.assertNotEquals(null, offersWithRecId, 'The method should retrieve client offers based on the SSN of the provided Account record');
        
        // Test scenario: both clientSSN and recId are blank
        // Call the initClientOffers method with blank clientSSN and recId
        List<VfClientOffer> offersWithBlankInputs = YourClassName.initClientOffers(null, null);
        
        // Assert that the method returns an empty list when both clientSSN and recId are blank
        System.assertEquals(0, offersWithBlankInputs.size(), 'The method should return an empty list when both clientSSN and recId are blank');
    }
}





public static List<VfClientOffer> initClientOffers(String recId, String clientSSN ) {
        if(string.isNotBlank(clientSSN)){
            ClientSSn =  clientSSN;
        }else{
            if(string.isNotBlank(recId)){
                Account a = [SELECT Id,Name,SSN__c FROM ACCOUNT WHERE Id =: recId LIMIT 1];
                ClientSSn =  a.SSN__c;
            }
        }   
         system.debug('clientSSN'+clientSSN);
         List<VfClientOffer> vfClientOfferList = new List<VfClientOffer>();
        
        //If Client is Opted out for offers or 
        //boolean clientOfferOptOut , boolean targetedmessages
      /*  if(clientOfferOptOut | !targetedmessages )
            return vfClientOfferList;*/
        
        vfClientOfferList = sortClientOffers(getOpenClientOffers(clientSSN));
        
     system.debug('vfClientOfferList ::::::::: ' + vfClientOfferList);
    
     return vfClientOfferList;
    }



@isTest
private class YourTestClass {
    // Test method to cover the getKnowledgeArticlesDetails method
    static testMethod void testGetKnowledgeArticlesDetails() {
        // Create test data
        Knowledge__kav testArticle = new Knowledge__kav(
            Title = 'Test Article',
            Summary = 'Test Summary',
            PublishStatus = 'Online'
            // Add any other required fields
        );
        insert testArticle;
        
        // Call the getKnowledgeArticlesDetails method with the test article name
        Knowledge__kav retrievedArticle = YourClassName.getKnowledgeArticlesDetails('Test Article');
        
        // Assert that the method retrieves the correct article details
        System.assertEquals('Test Article', retrievedArticle.Title, 'The method should retrieve the correct article title');
        System.assertEquals('Test Summary', retrievedArticle.Summary, 'The method should retrieve the correct article summary');
        System.assertEquals('Online', retrievedArticle.PublishStatus, 'The method should retrieve the correct publish status');
        
        // Add more assertions as needed to cover other fields
        
        // Test case for article not found
        // Call the method with a non-existing article name
        Knowledge__kav notFoundArticle = YourClassName.getKnowledgeArticlesDetails('Non-existing Article');
        
        // Assert that the method returns null when article is not found
        System.assertNull(notFoundArticle, 'The method should return null when the article is not found');
    }
}


public static Knowledge__kav getKnowledgeArticlesDetails(String articleName) {
        return [
            SELECT Id, Title, ArticleNumber, PublishStatus, FORMAT(LastPublishedDate) lastPub, Summary, 
                KnowledgeArticleId, LastModifiedDate, IsVisibleInCsp,
                UrlName, ArticleTotalViewCount, ArticleCaseAttachCount, RecordTypeId, RecordType.Name,
                Owner.Name, IsVisibleInApp, IsVisibleInPkb, IsVisibleInPrm, OwnerID
            FROM Knowledge__kav
            WHERE Title = :articleName
            LIMIT 1
        ];
    }   




@isTest
private class YourTestClass {
    // Test method to cover the createOpportunity method
    static testMethod void testCreateOpportunity() {
        // Create test data for input parameters
        String clientLastName = 'TestLastName';
        String campaignName = 'TestCampaign';
        String ownerId = UserInfo.getUserId();
        String response = 'TestResponse';
        String responseReason = 'TestResponseReason';
        String comment = 'TestComment';
        
        // Call the createOpportunity method with valid input parameters
        String createdOpportunityName = YourClassName.createOpportunity(clientLastName, campaignName, ownerId, response, responseReason, comment);
        
        // Retrieve the inserted Opportunity record
        List<Opportunity> insertedOpportunities = [SELECT Name FROM Opportunity];
        
        // Assert that the method creates the Opportunity with the correct name
        System.assertEquals(clientLastName + '-' + campaignName, createdOpportunityName, 'The method should return the correct Opportunity name');
        
        // Assert that an Opportunity record is inserted
        System.assertNotEquals(0, insertedOpportunities.size(), 'An Opportunity record should be inserted');
        
        // Test the scenario where oppList is empty
        // Call the createOpportunity method with empty input parameters
        String nullOpportunityName = YourClassName.createOpportunity(null, null, null, null, null, null);
        
        // Assert that the method returns null when oppList is empty
        System.assertEquals(null, nullOpportunityName, 'The method should return null when oppList is empty');
    }
}










@isTest
private class YourTestClass {
    // Test method to cover the createOpportunityWithPlanAndCampaign method
    static testMethod void testCreateOpportunityWithPlanAndCampaign() {
        // Create test data for input parameters
        String messageName = 'TestCampaignName';
        String planId = 'TestPlanId';
        String clientLastName = 'TestLastName';
        String ownerId = UserInfo.getUserId();
        String response = 'TestResponse';
        String responseReason = 'TestResponseReason';
        String comment = 'TestComment';
        
        // Create mock Campaign and Plan__c records
        Campaign testCampaign = new Campaign(Name = messageName);
        insert testCampaign;
        
        Plan__c testPlan = new Plan__c(Name = planId);
        insert testPlan;
        
        // Call the createOpportunityWithPlanAndCampaign method with the test data
        String createdOpportunityName = YourClassName.createOpportunityWithPlanAndCampaign(messageName, planId, clientLastName, ownerId, response, responseReason, comment);
        
        // Retrieve the inserted Opportunity record
        Opportunity insertedOpportunity = [SELECT Name, Plan__c, CampaignId FROM Opportunity LIMIT 1];
        
        // Assert that the method creates the Opportunity with the correct name
        System.assertEquals(clientLastName + '-' + testCampaign.Name, createdOpportunityName, 'The method should return the correct Opportunity name');
        
        // Assert that the inserted Opportunity has the correct values
        System.assertEquals(clientLastName + '-' + testCampaign.Name, insertedOpportunity.Name, 'The inserted Opportunity should have the correct name');
        System.assertEquals(testPlan.Id, insertedOpportunity.Plan__c, 'The inserted Opportunity should have the correct Plan__c Id');
        System.assertEquals(testCampaign.Id, insertedOpportunity.CampaignId, 'The inserted Opportunity should have the correct CampaignId');
    }
}








public static String createOpportunityWithPlanAndCampaign(String messageName, String planId, String clientLastName, string ownerId,string response, string responseReason, string comment) 
    { 
        System.debug('messageName'+ messageName);
       Campaign c= [Select Id,Name from Campaign where Name=:messageName  Limit 1];
        
        System.debug('Campaign'+c.Name);
        system.debug('checking plan'+planId);
        Plan__c p=(Plan__c)[Select Id,Name from Plan__c where Name=:planId Limit 1];
        
        system.debug('checking plan'+p.Name);
        List<Opportunity> oppList= new List<Opportunity>();
          
        Opportunity opp= new opportunity();
        opp.StageName='Need Analysis';
        opp.CloseDate=system.today();
        opp.Name=clientLastName+'-'+c.Name;
        opp.Plan__c=p.Id;
        opp.CampaignId=c.Id;
        opp.ownerId=ownerId;
		opp.Offer_Response__c=response;
        opp.Offer_Response_Reason__c=responseReason;
        opp.Message_Comments__c=comment;
        oppList.add(opp);
        // system.debug('seeing heree opportunity value', oppList[0]);
        if(!oppList.isEmpty()){
            system.debug('createOpportunityWithPlanAndCampaign '+ oppList);
            insert oppList;
            return oppList[0].Name;
        } 
       
        return oppList[0].Name;
    } 
    













@isTest
private class YourTestClass {
    // Test method to cover the createOpportunity method
    static testMethod void testCreateOpportunity() {
        // Create test data for input parameters
        String clientLastName = 'TestLastName';
        String campaignName = 'TestCampaign';
        String ownerId = UserInfo.getUserId();
        String response = 'TestResponse';
        String responseReason = 'TestResponseReason';
        String comment = 'TestComment';
        
        // Call the createOpportunity method with the test data
        String createdOpportunityName = YourClassName.createOpportunity(clientLastName, campaignName, ownerId, response, responseReason, comment);
        
        // Retrieve the inserted Opportunity record
        Opportunity insertedOpportunity = [SELECT Name FROM Opportunity LIMIT 1];
        
        // Assert that the method creates the Opportunity with the correct name
        System.assertEquals(clientLastName + '-' + campaignName, createdOpportunityName, 'The method should return the correct Opportunity name');
        
        // Assert that the inserted Opportunity has the correct name
        System.assertEquals(clientLastName + '-' + campaignName, insertedOpportunity.Name, 'The inserted Opportunity should have the correct name');
    }
}









public static String createOpportunity(String clientLastName, String campaignName, string ownerId,string response, string responseReason, string comment) {
    List<Opportunity> oppList= new List<Opportunity>();
        
        Opportunity opp= new opportunity();
        opp.StageName='Need Analysis';
        opp.CloseDate=system.today();
        opp.Name=clientLastName+'-'+campaignName;
        opp.ownerId=ownerId;
		opp.Offer_Response__c=response;
        opp.Offer_Response_Reason__c=responseReason;
        opp.Message_Comments__c=comment;
        oppList.add(opp);
        // system.debug('seeing heree opportunity value', oppList[0]);
        if(!oppList.isEmpty()){
            system.debug('seeing heree opportunity value '+ oppList);
            insert oppList;
            return oppList[0].Name;
        }
        return null;
    }  










@isTest
private class YourTestClass {
    // Test method to cover the getClientLastName method
    static testMethod void testGetClientLastName() {
        // Create a test Account record
        Account testAccount = new Account(
            LastName = 'TestLastName'
            // Add any other required fields
        );
        insert testAccount;
        
        // Call the getClientLastName method with the Id of the test Account record
        String actualLastName = YourClassName.getClientLastName(testAccount.Id);
        
        // Retrieve the inserted Account record
        Account insertedAccount = [SELECT LastName FROM Account WHERE Id = :testAccount.Id];
        
        // Assert that the method returns the correct last name
        System.assertEquals(insertedAccount.LastName, actualLastName, 'The method should return the correct last name');
    }
}







public static String getClientLastName(string recordId) {
        Account acc = [SELECT Id, LastName
                             FROM Account 
                             WHERE Id = :recordId];
        system.debug('acc.LastName '+acc.LastName);
        return acc.LastName;
        
    }



@isTest
private class YourTestClass {
    // Test method to cover the getProfileId method
    static testMethod void testGetProfileId() {
        // Insert test data for ProfileNames__c custom metadata type
        ProfileNames__c testProfile = new ProfileNames__c(
            MasterLabel = 'Test Profile',
            Profile_Id__c = 'SomeProfileId'
        );
        insert testProfile;
        
        // Call the getProfileId method
        String actualProfileId = YourClassName.getProfileId();
        
        // Retrieve the inserted ProfileNames__c record
        List<ProfileNames__c> insertedProfiles = [SELECT Profile_Id__c FROM ProfileNames__c];
        
        // Assert that the method returns the correct profile Id
        System.assertEquals(insertedProfiles[0].Profile_Id__c, actualProfileId, 'The method should return the correct profile Id');
    }
}









public static String getProfileId(){
        List<ProfileNames__c> profileIds= ProfileNames__c.getAll().values();
        for(ProfileNames__c profileId:profileIds){
            
        }
        return profileIds[0].Profile_Id__c;
    }





public class cTargetedMessage {
     
    @AuraEnabled(cacheable=true)
    public static String getProfileId(){
        List<ProfileNames__c> profileIds= ProfileNames__c.getAll().values();
        for(ProfileNames__c profileId:profileIds){
            
        }
        return profileIds[0].Profile_Id__c;
    }
    
    
    //Radhika
     @AuraEnabled(cacheable=true)
    public static campaign getCampaignDetails(String campaignId) {
     List<Campaign> campList= new List<Campaign>();
        return [ SELECT Id, Name,TM_Article_Name__c
            FROM Campaign WHERE Id= :campaignId
            LIMIT 1 ];//Id, Name , 
        
    }  
    @AuraEnabled(cacheable=true)
    public static User getLoggedInUserDetails() {
        User loggedInUser = [SELECT Id, Name
                             FROM User 
                             WHERE Id = :UserInfo.getUserId()];
        return loggedInUser;
    }

    @AuraEnabled(cacheable=true)
    public static String getClientLastName(string recordId) {
        Account acc = [SELECT Id, LastName
                             FROM Account 
                             WHERE Id = :recordId];
        system.debug('acc.LastName '+acc.LastName);
        return acc.LastName;
        
    }
    
    @AuraEnabled
    public static String createOpportunity(String clientLastName, String campaignName, string ownerId,string response, string responseReason, string comment) {
    List<Opportunity> oppList= new List<Opportunity>();
        
        Opportunity opp= new opportunity();
        opp.StageName='Need Analysis';
        opp.CloseDate=system.today();
        opp.Name=clientLastName+'-'+campaignName;
        opp.ownerId=ownerId;
		opp.Offer_Response__c=response;
        opp.Offer_Response_Reason__c=responseReason;
        opp.Message_Comments__c=comment;
        oppList.add(opp);
        // system.debug('seeing heree opportunity value', oppList[0]);
        if(!oppList.isEmpty()){
            system.debug('seeing heree opportunity value '+ oppList);
            insert oppList;
            return oppList[0].Name;
        }
        return null;
    }  
    
    
    @AuraEnabled
    public static String createOpportunityWithPlanAndCampaign(String messageName, String planId, String clientLastName, string ownerId,string response, string responseReason, string comment) 
    { 
       Campaign c= [Select Id,Name from Campaign where Name=:messageName  Limit 1];
        Plan__c p=(Plan__c)[Select Id,Name from Plan__c where Name=:planId Limit 1];
        
        system.debug('checking plan'+p.Name);
        List<Opportunity> oppList= new List<Opportunity>();
          
        Opportunity opp= new opportunity();
        opp.StageName='Need Analysis';
        opp.CloseDate=system.today();
        opp.Name=clientLastName+'-'+c.Name;
        opp.Plan__c=p.Id;
        opp.CampaignId=c.Id;
        opp.ownerId=ownerId;
		opp.Offer_Response__c=response;
        opp.Offer_Response_Reason__c=responseReason;
        opp.Message_Comments__c=comment;
        oppList.add(opp);
        // system.debug('seeing heree opportunity value', oppList[0]);
        if(!oppList.isEmpty()){
            system.debug('createOpportunityWithPlanAndCampaign '+ oppList);
            insert oppList;
            return oppList[0].Name;
        } 
       
        return oppList[0].Name;
    } 
    
    @AuraEnabled(cacheable=true)
    public static List<Campaign> getCampaignNames() {
    List<Campaign> camList= [Select Id, Name from Campaign order by Name];
        return camList ;
    }
    
    
    
   /*  @AuraEnabled(cacheable=true)
    public static String getCampaignArticleName(String campaignId) {
     List<Campaign> campList= new List<Campaign>();
        campList= [ SELECT Id, TM_Article_Name__c
            FROM Campaign WHERE Id= :campaignId
            LIMIT 1 ];//Id, Name , 
        return campList[0].TM_Article_Name__c;
    }  */
    
@AuraEnabled(cacheable=true)
    public static Campaign getCampaignId(String campaignName) {
        return [ SELECT Id
            FROM Campaign WHERE Name= :campaignName
            LIMIT 1 ];
    } 
    
    
    
    @AuraEnabled(cacheable=true)
    public static Knowledge__kav getKnowledgeArticlesDetails(String articleName) {
        return [
            SELECT Id, Title, ArticleNumber, PublishStatus, FORMAT(LastPublishedDate) lastPub, Summary, 
                KnowledgeArticleId, LastModifiedDate, IsVisibleInCsp,
                UrlName, ArticleTotalViewCount, ArticleCaseAttachCount, RecordTypeId, RecordType.Name,
                Owner.Name, IsVisibleInApp, IsVisibleInPkb, IsVisibleInPrm, OwnerID
            FROM Knowledge__kav
            WHERE Title = :articleName
            LIMIT 1
        ];
    }    
    
    
    
    ///Code for Targeted message
     @AuraEnabled(cacheable=true)
    public static List<VfClientOffer> initClientOffers(String recId, String clientSSN ) {
        if(string.isNotBlank(clientSSN)){
            ClientSSn =  clientSSN;
        }else{
            if(string.isNotBlank(recId)){
                Account a = [SELECT Id,Name,SSN__c FROM ACCOUNT WHERE Id =: recId LIMIT 1];
                ClientSSn =  a.SSN__c;
            }
        }   
         system.debug('clientSSN'+clientSSN);
         List<VfClientOffer> vfClientOfferList = new List<VfClientOffer>();
        
        //If Client is Opted out for offers or 
        //boolean clientOfferOptOut , boolean targetedmessages
      /*  if(clientOfferOptOut | !targetedmessages )
            return vfClientOfferList;*/
        
        vfClientOfferList = sortClientOffers(getOpenClientOffers(clientSSN));
        
     system.debug('vfClientOfferList ::::::::: ' + vfClientOfferList);
    
     return vfClientOfferList;
    }
    
    private static List<vfClientOffer> getOpenClientOffers(String ssn) {
        List<vfClientOffer> vfClientOfferList = new List<vfClientOffer>();
        
        List<Client_Offer__c> clientOpenOfferList = dynamicClientOfferQuery(new Set<String>{ssn});
        if(clientOpenOfferList == null || clientOpenOfferList.size() == 0)
            return vfClientOfferList;
        
        Client_Offer__c co = clientOpenOfferList[0];
        
        Map<String, Campaign_Offer_Summary__c> campaignOfferDoNotShowOfferMap = getCampaignOfferDoNotShowOfferMap(new Set<String>{ssn});
        
        System.debug('co ::::: ' + co);
        String planId = null, key = null;
        Campaign_Offer_Summary__c offerSummary = null;
        for (Campaign c : [select id, name, offer_code__c, offer_priority__c from campaign where offer_code__c != null limit 50]) {
            try { 
                //Check data in Campaign Offer Summary Object
                //Match on PlanID and Offer Code
                
                planId = (String)co.get('PlanId_' + c.offer_code__c + '__c');
                key = ssn + ConstantUtils.UNIQUE_SEPERATOR + planId + ConstantUtils.UNIQUE_SEPERATOR + c.offer_code__c;
                
                offerSummary = campaignOfferDoNotShowOfferMap.get(key);
                //Case# 16921 && Case# 11899 - Rahul Sahay - 07/17/2013 
                if(offerSummary != null && planId == offerSummary.Planid_Text__c && c.offer_code__c == offerSummary.OfferCode__c)
                        continue;
                
                if (((String)co.get('status_' + c.offer_code__c + '__c') == 'Open') && ((Decimal)co.get('score_' + c.offer_code__c + '__c') != null)) {
                    VfClientOffer vf = new VfClientOffer();
                    vf.offerCode = c.offer_code__c;
                    vf.offerName = c.name;                    
                    vf.offerPriority = c.offer_priority__c;
                    vf.offerScore = (Decimal)co.get('score_' + c.offer_code__c + '__c');
                    vf.offerPlanId = (String)co.get('planid_' + c.offer_code__c + '__c');
                    vf.offerPlanName = (String)co.get('PlanName_' + c.offer_code__c + '__c');
                    
                    //Case # 00011325: Added new field to show the Avtive Mailer information on the Targeted Messages list on the Offer Page.
                    vf.activeMailer = (String)co.get('Active_Mailer_' + c.offer_code__c + '__c');
                   
                    vf.offerCampaign = c.id;
                    vfClientOfferList.add(vf);//---this is need to display in UI
                }
            } catch (Exception e) {
                // this exception means the user has inserted a campaign with an offer_code__c but has not inserted the corresponding fields on the client_offer__c object
                // swallow 
            }
        }        
        
        System.debug('vfClientOfferList :::: ' + vfClientOfferList);
        
        return vfClientOfferList;
    }
    
 	public static List<Client_Offer__c> dynamicClientOfferQuery(Set<String> ssnSet) {
        String fields = '';
        for (String field : Schema.SObjectType.Client_Offer__c.fields.getMap().keySet()) {
            fields += field + ',';
        }
        fields = fields.substring(0,fields.length()-1);
        
        String ssns = '';
        for (String ssn : ssnSet) {
            ssns += '\''+ ssn + '\',';
        }
        ssns = ssns.substring(0,ssns.length()-1);

        if (ssns == null || ssns.length()<1 || fields == null || fields.length()<1) {
            return null;
        }  
        
        String query = 'select ' + fields;
        query += ' from client_offer__c where account_ext_id__c in (' + ssns + ') and account_ext_id__c != null';
        System.debug('DEBUG query - ' + query);
        return Database.query(query);       
    }
    
 
    
    
     public static Map<String, Campaign_Offer_Summary__c> getCampaignOfferDoNotShowOfferMap(Set<String> ssnSet) {
        Map<String, Campaign_Offer_Summary__c> ssnCampaignOfferMap = new Map<String, Campaign_Offer_Summary__c>();
        String key = null;
        
        List<Campaign_Offer_Summary__c> coSummaryList = [select OfferCode__c, Present_Message__c, Planid_Text__c, Customer_SSN__c from Campaign_Offer_Summary__c where Customer_SSN__c in: ssnSet];
        for(Campaign_Offer_Summary__c cos : coSummaryList) {
            //Fill this up with only 'D0-NOT-SHOWS'
            if(!cos.Present_Message__c && !String.isBlank(cos.OfferCode__c) && !String.isBlank(cos.Planid_Text__c)) {
                key = cos.Customer_SSN__c + ConstantUtils.UNIQUE_SEPERATOR + cos.Planid_Text__c + ConstantUtils.UNIQUE_SEPERATOR + cos.OfferCode__c;
                
                ssnCampaignOfferMap.put(key, cos);
            }
        }
        
        return ssnCampaignOfferMap;       
    }
    
    private static List<vfClientOffer> sortClientOffers(List<vfClientOffer> col) {
        // this method sorts by both offerPriority and offerScore
        // lowest offerPriorty takes priority, highest offerScore takes priority
        // given the following list: {offerPriority -> offerScore}
        // {1 -> 10}
        // {2 -> 20}
        // {2 -> 50}
        // {1 -> 20}
        // this method will produce the following ordered list:
        // {1 -> 20}
        // {1 -> 10}
        // {2 -> 50}
        // {2 -> 20}    
        //
        // this method could be tweaked to perform better, but since the max list size will probably be
        // no larger than 20, it's probably ok.
        
        // sort by offer score
        System.debug('Templist: ' + col.size());    
        List<Decimal> vfclientOfferScoreList = new List<Decimal>();
        for (vfClientOffer vfco : col) {
            System.debug('Add to decimal list: OfferPriority:' + vfco.offerPriority + '; SortedScoreList: ' + vfco.offerScore + ';OfferName: '+vfco.offerName);
            vfclientOfferScoreList.add(vfco.offerScore);
        }
        vfClientOfferScoreList.sort();
        
        // revers sort so offer scores are descending (higher offer score means higher priority)
        List<Decimal> reverseSortList = new Decimal[vfClientOfferScoreList.size()];
        Integer j = 0;
        for (Integer i=vfClientOfferScoreList.size()-1; i>=0 ;i--) {
            reverseSortList[j] = vfClientOfferScoreList[i];
            j++;
        }       
        
        List<vfClientOffer> sortedScoreList = new List<vfClientOffer>();        
        for (Decimal d : reverseSortList) {
            Integer index = 0;
            for (vfClientOffer vfco : col) {
                if (vfco.offerScore == d) {
                    System.debug('OfferPriority:' + vfco.offerPriority + '; SortedScoreList: ' + vfco.offerScore + ';OfferName: '+vfco.offerName);
                    sortedScoreList.add(vfco);
                    break;
                }
                index++;
            }
            col.remove(index);
        }
        
        // create map of ordere offers based on campaign priority       
        Map<Decimal, List<vfClientOffer>> offerPriorityMap = new Map<Decimal, List<vfClientOffer>>();
        for (vfClientOffer vfco : sortedScoreList) {
            if (offerPriorityMap.containsKey(vfco.offerPriority)) {
                List<vfClientOffer> newList = offerPriorityMap.get(vfco.offerPriority);
                newList.add(vfco);
                offerPriorityMap.put(vfco.offerPriority, newList);
                System.debug('Add to existing map - OfferPriority: ' + vfco.offerPriority + '; Score:' + vfco.offerScore);
            } else {
                offerPriorityMap.put(vfco.offerPriority,new List<vfClientOffer>{vfco});
                System.debug('New map - OfferPriority: ' + vfco.offerPriority + '; Score:' + vfco.offerScore);
            }
        }

        // sort the offer priority
        List<Decimal> offerPriorityList = new List<Decimal>();
        for (Decimal d : offerPriorityMap.keySet()) {
            offerPriorityList.add(d);
        }
        offerPriorityList.sort();
        
        // create the final list sorted by offer priority, then by offer score
        List<vfClientOffer> finalList = new List<vfClientOffer>();
        for (Decimal d : offerPriorityList) {
            System.debug('OfferPriorityList: ' + d);
            for (vfClientOffer vfco : offerPriorityMap.get(d)) {
                finalList.add(vfco);
            }
        }       
        return finalList;
    }
    
    
    public class vfClientOffer {
         @AuraEnabled public String offerName {get; set;}
         @AuraEnabled public Decimal offerScore {get; set;}
         @AuraEnabled public Decimal offerPriority {get; set;}
         @AuraEnabled public String offerPlanId {get; set;}
         @AuraEnabled public String offerPlanName {get; set;}
         @AuraEnabled public String offerCode {get; set;}
         @AuraEnabled public String offerCampaign {get; set;}
        
        //Case # 00011325: Added new field to show the Avtive Mailer information on the Targeted Messages list on the Offer Page.  
         @AuraEnabled public String activeMailer {get; set;}
    }
    
   
   
    
}
