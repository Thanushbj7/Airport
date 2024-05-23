@AuraEnabled(cacheable=false)
public static List<UltimatePopControllerHelper.SearchResult> initializeAndLoadPlanData(String clientId){
    Case currentCase = new Case();
    //system.debug('Acc Id '+clientId);
	String clientSSN = '';
	String test = '';

    ////system.debug('id '+Id+' cientSSN '+clientSSN+' vru '+test);
    //this.innerSharingClassInstance = new CTIWelcomePageController.CTIWelcomePageControllerWithSharing();
    
    ////system.debug('====================== ApexPages.currentPage().getParameters().get(id) ' + ApexPages.currentPage().getParameters().get('id'));
    
    CTI_Console_Pop__c  ctiConsolePop = [select Id, Account__r.SSN__c, CTI_Params__c, DC_Serialized_Result__c, Case__c, 
        Case__r.Id, Case__r.CaseNumber, Offer_Pop__c, Offer_Pop__r.Id from CTI_Console_Pop__c 
        where account__c = :clientId order by LastModifiedDate  desc limit 1];
        String[] keyValuePairs = ctiConsolePop.CTI_Params__c.split(';');
        for(String pair : keyValuePairs) {
            String[] keyValue = pair.split(':');
            if(keyValue.size() == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
        
                if(key == 'clientId') {
                    clientSSN = value;
                } else if(key == 'VRUAPP') {
                    test = value;
                }
            }
        }
        
        //system.debug('clientSSN: ' + clientSSN);
        //system.debug('test: ' + test);
    Account client = UltimatePopControllerHelper.getSFDCClientInfo(ctiConsolePop.Account__r.SSN__c, true);
    
    List<Case> currentCaseList = [select Id, CaseNumber, Offers_Available__c from Case where Id = :ctiConsolePop.Case__r.Id];
    if(currentCaseList != null && currentCaseList.size() > 0)
        currentCase = currentCaseList.get(0);
    	
    String ctiParams = ctiConsolePop.CTI_Params__c;
    CurCase=ctiConsolePop.Case__r.Id;
  
    return initializeAvailablePlans(currentCase,clientSSN,test,client);
    






}



@isTest
private class TestUltimatePopController {

    @testSetup
    static void setupTestData() {
        setupCaseAndCTIConsolePopData();
        setupAnotherCaseAndCaseActionsData();
    }
    
    // Helper method for the Case and CTI_Console_Pop__c setup
    private static void setupCaseAndCTIConsolePopData() {
        // Create Accounts
        Account testAccount = new Account(Name='Test Account', SSN__c='123-45-6789');
        insert testAccount;

        // Create a Case
        Case testCase = new Case(AccountId=testAccount.Id, Status='New', Origin='Web');
        insert testCase;
        
        // Create CTI_Console_Pop__c record
        CTI_Console_Pop__c ctiConsolePop = new CTI_Console_Pop__c(
            Account__c=testAccount.Id,
            CTI_Params__c='clientId:123;VRUAPP:test;',
            Case__c=testCase.Id,
            Offer_Pop__c=testCase.Id
        );
        insert ctiConsolePop;
    }
    
    // Helper method for the additional setup process
    private static void setupAnotherCaseAndCaseActionsData() {
        // Create another Case
        Case c = new Case();
        c.Origin = 'Email';
        c.Status = 'New';
        c.Description = 'Test Description';
        insert c;
        
        // Create Case Actions
        Case_Actions__c ca = new Case_Actions__c();
        ca.Case__c = c.Id;
        ca.Call_Activity__c = 'Transaction';
        ca.Call_Type__c = 'Name Change';
        ca.PlanID_Text__c = '664034';
        insert ca;
    }

    @isTest
    static void testInitializeAndLoadPlanData() {
        // Mock the UltimatePopControllerHelper class
        TestUltimatePopControllerHelper mockHelper = new TestUltimatePopControllerHelper();
        Test.setMock(ApexMocks.class, mockHelper);

        Test.startTest();
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAndLoadPlanData('001xx000003DI12');
        Test.stopTest();
        
        System.assertNotEquals(null, result, 'Expected non-null result from initializeAndLoadPlanData.');
    }
    
    // Mock Helper Class
    public class TestUltimatePopControllerHelper implements UltimatePopControllerHelper {
        public static Account getSFDCClientInfo(String ssn, Boolean includeClosed) {
            Account mockAccount = new Account(Id='001xx000003DI12', Name='Mock Account', SSN__c=ssn);
            return mockAccount;
        }
        
        public static List<UltimatePopControllerHelper.SearchResult> initializeAvailablePlans(Case currentCase, String clientSSN, String test, Account client) {
            UltimatePopControllerHelper.SearchResult mockResult = new UltimatePopControllerHelper.SearchResult();
            mockResult.someField = 'Mock Data';
            return new List<UltimatePopControllerHelper.SearchResult> { mockResult };
        }
    }
    
    // Dummy method in your class for context. Replace with your actual class name and method if necessary.
    public class YourClassName {
        @AuraEnabled(cacheable=false)
        public static List<UltimatePopControllerHelper.SearchResult> initializeAndLoadPlanData(String clientId) {
            // Method implementation
        }
    }
}










@testSetup
    public static void createCase(){ 
        
        Case c= new Case();
        c.Origin='Email';
        c.Status='New';
        c.Description='Test Description';
        insert c;
        
        Case_Actions__c ca= new Case_Actions__c();
        ca.Case__c=c.Id;
        ca.Call_Activity__c='Transaction';
        ca.Call_Type__c='Name Change';
        ca.PlanID_Text__c='664034';
        
        insert ca;
         
        
    }




Only one method per type can be defined with: TestSetup

@isTest
private class TestUltimatePopController {
    
    @testSetup
    static void setupTestData() {
        // Create Accounts
        Account testAccount = new Account(Name='Test Account', SSN__c='123-45-6789');
        insert testAccount;

        // Create a Case
        Case testCase = new Case(AccountId=testAccount.Id, Status='New', Origin='Web');
        insert testCase;
        
        // Create CTI_Console_Pop__c record
        CTI_Console_Pop__c ctiConsolePop = new CTI_Console_Pop__c(
            Account__c=testAccount.Id,
            CTI_Params__c='clientId:123;VRUAPP:test;',
            Case__c=testCase.Id,
            Offer_Pop__c=testCase.Id
        );
        insert ctiConsolePop;
    }
    
    @isTest
    static void testInitializeAndLoadPlanData() {
        // Mock the UltimatePopControllerHelper class
        TestUltimatePopControllerHelper mockHelper = new TestUltimatePopControllerHelper();
        Test.setMock(ApexMocks.class, mockHelper);

        Test.startTest();
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAndLoadPlanData('001xx000003DI12');
        Test.stopTest();
        
        System.assertNotEquals(null, result, 'Expected non-null result from initializeAndLoadPlanData.');
    }
    
    // Mock Helper Class
    public class TestUltimatePopControllerHelper implements UltimatePopControllerHelper {
        public static Account getSFDCClientInfo(String ssn, Boolean includeClosed) {
            Account mockAccount = new Account(Id='001xx000003DI12', Name='Mock Account', SSN__c=ssn);
            return mockAccount;
        }
        
        public static List<UltimatePopControllerHelper.SearchResult> initializeAvailablePlans(Case currentCase, String clientSSN, String test, Account client) {
            UltimatePopControllerHelper.SearchResult mockResult = new UltimatePopControllerHelper.SearchResult();
            mockResult.someField = 'Mock Data';
            return new List<UltimatePopControllerHelper.SearchResult> { mockResult };
        }
    }
    
    // Dummy method in your class for context. Replace with your actual class name and method if necessary.
    public class YourClassName {
        @AuraEnabled(cacheable=false)
        public static List<UltimatePopControllerHelper.SearchResult> initializeAndLoadPlanData(String clientId) {
            // Method implementation
        }
    }
}








@isTest
private class TestKnowledgeArticleController {
    
    @testSetup
    static void setupTestData() {
        // Create a RecordType for Knowledge__kav
        RecordType rt = [SELECT Id FROM RecordType WHERE SObjectType = 'Knowledge__kav' LIMIT 1];
        
        // Create Knowledge__kav records
        List<Knowledge__kav> knowledgeArticles = new List<Knowledge__kav>();
        for (Integer i = 0; i < 5; i++) {
            knowledgeArticles.add(new Knowledge__kav(
                Title = 'Test Article ' + i,
                ArticleNumber = '000' + i,
                Summary = 'Summary of test article ' + i,
                ValidationStatus = 'Draft',
                PublishStatus = 'Online',
                KnowledgeArticleId = 'KA' + i,
                LastPublishedDate = System.today().addDays(-i),
                LastModifiedDate = System.today().addDays(-i),
                IsVisibleInPkb = true,
                RecordTypeId = rt.Id,
                Language = 'en_US'
            ));
        }
        
        insert knowledgeArticles;
        
        // Create KnowledgeArticleVersion records
        List<KnowledgeArticleVersion> knowledgeArticleVersions = new List<KnowledgeArticleVersion>();
        for (Knowledge__kav kav : knowledgeArticles) {
            knowledgeArticleVersions.add(new KnowledgeArticleVersion(
                Title = kav.Title,
                KnowledgeArticleId = kav.KnowledgeArticleId,
                PublishStatus = kav.PublishStatus,
                Language = kav.Language
            ));
        }
        
        insert knowledgeArticleVersions;
    }
    
    @isTest
    static void testGetKnowledgeArticles_NoFilters() {
        Test.startTest();
        List<knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticles('', '');
        Test.stopTest();
        
        System.assertEquals(5, result.size(), 'Expected 5 articles when no filters are applied.');
    }
    
    @isTest
    static void testGetKnowledgeArticles_ClientStatusFilter() {
        Test.startTest();
        List<knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticles('', 'Active');
        Test.stopTest();
        
        System.assert(result.size() > 0, 'Expected articles when clientStatus is filtered.');
    }
    
    @isTest
    static void testGetKnowledgeArticles_ReceivedValueFilter() {
        Test.startTest();
        List<knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticles('Inquiry', '');
        Test.stopTest();
        
        System.assert(result.size() > 0, 'Expected articles when receivedValue is filtered.');
    }
    
    @isTest
    static void testGetKnowledgeArticles_BothFilters() {
        Test.startTest();
        List<knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticles('Inquiry', 'Active');
        Test.stopTest();
        
        System.assert(result.size() > 0, 'Expected articles when both filters are applied.');
    }
    
    // Dummy method in your class for context. Replace with your actual class name and method if necessary.
    public class YourClassName {
        @AuraEnabled
        public static List<knowledgeArticleWrapper> getKnowledgeArticles(String receivedValue, String clientStatus) {
            // Method implementation
        }
    }
}









public class knowledgeArticleWrapper {
    @AuraEnabled public String id;
    @AuraEnabled public String title;
    @AuraEnabled public String articleNumber;
    @AuraEnabled public String summary;
    @AuraEnabled public String validationStatus;
    @AuraEnabled public String publishStatus;
    @AuraEnabled public String lastPublishedDate;
    @AuraEnabled public String knowledgeArticleId;
    @AuraEnabled public String lastModifiedDate;
    @AuraEnabled public String recordTypeName;
    @AuraEnabled public String accountName;
    @AuraEnabled public Boolean articleAttachedToCase;
    @AuraEnabled public Boolean displayGenericKnowArticles;
    @AuraEnabled public Boolean displayCopytoClipboardIcon;
    @AuraEnabled public String chatAnswer;
    @AuraEnabled public Integer caseArticleSize;
    @AuraEnabled public String attachedBy;
    @AuraEnabled public String attachedDate;
    @AuraEnabled public Boolean visibleInPublicKnowledgeBase;
    @AuraEnabled public String viewCountDS;
}




@AuraEnabled
public static List<knowledgeArticleWrapper> getKnowledgeArticles(String receivedValue,String clientStatus) {
//system.debug('Rec '+receivedValue+' '+clientStatus);

Set<Id> knowledgeArticleIds = new Set<Id>();
List<knowledge__kav> knowledeArticles = new List<knowledge__kav>();
   if((String.isBlank(receivedValue) || receivedValue=='--None--' || receivedValue==''||receivedValue==null) &&(String.isBlank(clientStatus) || clientStatus=='--None--' || clientStatus==''||clientStatus==null)){
knowledeArticles = [select id, Title, ArticleNumber, Summary,ValidationStatus, PublishStatus,
    KnowledgeArticleId, LastPublishedDate, LastModifiedDate,
     IsVisibleInPkb,RecordType.Name from knowledge__kav limit 5];
   }
   else if((String.isBlank(receivedValue) || receivedValue=='--None--' || receivedValue==''||receivedValue==null) && (!String.isBlank(clientStatus) || clientStatus!='--None--')){
    //system.debug('Clien T '+clientStatus);
    clientStatus = clientStatus +'__c';
    clientStatus = String.escapeSingleQuotes(clientStatus);
    //system.debug('Clien T '+clientStatus);
    // Reconstruct the complete data category part of the query
    String dataCategoryQueryPart = 'WITH DATA CATEGORY Client_Status__c ABOVE_OR_BELOW ' + String.escapeSingleQuotes(clientStatus) ;

    String query = 'SELECT Title, Id, KnowledgeArticleId FROM KnowledgeArticleVersion ' +
    'WHERE PublishStatus = \'Online\' ' + dataCategoryQueryPart;



//system.debug('Query '+query);

    List<KnowledgeArticleVersion> knowledgeLinks =Database.query(query);
    for(KnowledgeArticleVersion cc : knowledgeLinks){
        knowledgeArticleIds.add(cc.KnowledgeArticleId);
    }
    knowledeArticles = [select id, Title, ArticleNumber, Summary,ValidationStatus, PublishStatus,
    KnowledgeArticleId, LastPublishedDate, LastModifiedDate,
     IsVisibleInPkb,RecordType.Name from knowledge__kav where KnowledgeArticleId   in :knowledgeArticleIds order by ArticleTotalViewCount desc ];
   }
   else if((!String.isBlank(receivedValue) ||receivedValue!='--None--' ) && (String.isBlank(clientStatus) || clientStatus==''||clientStatus==null || clientStatus=='--None--')){

    receivedValue = receivedValue +'__c';
    receivedValue = String.escapeSingleQuotes(receivedValue);
    //system.debug('Clien T '+receivedValue);
    // Reconstruct the complete data category part of the query
    String dataCategoryQueryPart = 'WITH DATA CATEGORY Call_Type_Hierarchy__c ABOVE_OR_BELOW ' + String.escapeSingleQuotes(receivedValue) ;

    String query = 'SELECT Title, Id, KnowledgeArticleId FROM KnowledgeArticleVersion ' +
    'WHERE PublishStatus = \'Online\' ' + dataCategoryQueryPart;

// Add the DATA CATEGORY part directly without checking if it's blank
   // query += 'WITH DATA CATEGORY Client_Status__c  ABOVE_OR_BELOW \'' + 
    //String.escapeSingleQuotes(clientStatus) + '\'';

// Execute the dynamic query





    List<KnowledgeArticleVersion> knowledgeLinks =Database.query(query);
    for(KnowledgeArticleVersion cc : knowledgeLinks){
        knowledgeArticleIds.add(cc.KnowledgeArticleId);
    }
    knowledeArticles = [select id, Title, ArticleNumber, Summary,ValidationStatus, PublishStatus,
    KnowledgeArticleId, LastPublishedDate, LastModifiedDate,
     IsVisibleInPkb,RecordType.Name from knowledge__kav where KnowledgeArticleId   in :knowledgeArticleIds order by ArticleTotalViewCount desc];

   }
   else if((!String.isBlank(receivedValue) || receivedValue!='--None--') && (!String.isBlank(clientStatus) || clientStatus!='--None--')){
    clientStatus = clientStatus +'__c';
    receivedValue = receivedValue +'__c';
    receivedValue = String.escapeSingleQuotes(receivedValue);
    //system.debug('Clien T '+receivedValue);
    // Reconstruct the complete data category part of the query
    String dataCategoryQueryPart = 'WITH DATA CATEGORY Call_Type_Hierarchy__c ABOVE_OR_BELOW ' + String.escapeSingleQuotes(receivedValue) + ' AND Client_Status__c ABOVE_OR_BELOW  '+String.escapeSingleQuotes(clientStatus) ;

    String query = 'SELECT Title, Id, KnowledgeArticleId FROM KnowledgeArticleVersion ' +
    'WHERE PublishStatus = \'Online\' ' + dataCategoryQueryPart;






    List<KnowledgeArticleVersion> knowledgeLinks =Database.query(query);
    for(KnowledgeArticleVersion cc : knowledgeLinks){
        knowledgeArticleIds.add(cc.KnowledgeArticleId);
    }
    knowledeArticles = [select id, Title, ArticleNumber, Summary,ValidationStatus, PublishStatus,
    KnowledgeArticleId, LastPublishedDate, LastModifiedDate,
     IsVisibleInPkb,RecordType.Name from knowledge__kav where KnowledgeArticleId   in :knowledgeArticleIds order by ArticleTotalViewCount desc];

   }
   
    return generateWrapperData(knowledeArticles);

}






@isTest
private class TestGetJSON {
    @isTest
    static void testGetJSON() {
        // Create a test Knowledge__kav record with PAAG_Configuration record type
        RecordType rt = [SELECT Id FROM RecordType WHERE SObjectType = 'Knowledge__kav' AND Name = 'PAAG_Configuration' LIMIT 1];
        
        Knowledge__kav testKnowledge = new Knowledge__kav(
            Title = 'Test PAAG Configuration',
            UrlName = 'test-paag-config',
            PAAG_Configuration_Metadata__c = '{"configKey": "configValue"}',
            RecordTypeId = rt.Id,
            PublishStatus = 'Online',
            Language = 'en_US'
        );
        
        insert testKnowledge;
        
        // Call the method under test
        Test.startTest();
        String result = YourClassName.getJSON('test-paag-config');
        Test.stopTest();
        
        // Assert that the result matches the expected JSON string
        System.assertEquals('{"configKey": "configValue"}', result, 'The JSON string should match the expected value.');
    }
    
    // Dummy method in your class for context. Replace with your actual class name and method if necessary.
    public class YourClassName {
        @AuraEnabled
        public static String getJSON(String paagConfig) {
            return [SELECT PAAG_Configuration_Metadata__c FROM Knowledge__kav WHERE RecordType.Name = 'PAAG_Configuration' AND UrlName = :paagConfig LIMIT 1].PAAG_Configuration_Metadata__c;
        }
    }
}




@AuraEnabled
public static String getJSON(String paagConfig){
return [select id,PAAG_Configuration_Metadata__c from Knowledge__kav where RecordType.name = 'PAAG_Configuration' and urlname =: paagConfig limit 1].PAAG_Configuration_Metadata__c;
}


@isTest
private class TestGetKnowledgeArticlesFromSearch {
    
    @isTest
    static void testGetKnowledgeArticlesFromSearch() {
        // Create test data
        List<Knowledge__kav> testArticles = new List<Knowledge__kav>();
        
        for (Integer i = 0; i < 5; i++) {
            Knowledge__kav article = new Knowledge__kav(
                Title = 'Test Article ' + i,
                ArticleNumber = '0000' + i,
                Summary = 'Summary for Test Article ' + i,
                ValidationStatus = 'Draft',
                PublishStatus = 'Online',
                KnowledgeArticleId = 'KA000000000' + i,
                LastPublishedDate = System.today().addDays(-i),
                LastModifiedDate = System.today().addDays(-i),
                IsVisibleInPkb = true,
                RecordTypeId = Schema.SObjectType.Knowledge__kav.getRecordTypeInfosByName().get('FAQ').getRecordTypeId()
            );
            testArticles.add(article);
        }
        
        insert testArticles;
        
        // Set fixed search results for SOSL query
        List<List<SObject>> searchResults = new List<List<SObject>>();
        searchResults.add(testArticles);
        Test.setFixedSearchResults(searchResults);
        
        // Call the method under test
        Test.startTest();
        List<YourClassName.knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticlesFromSearch('Test Article');
        Test.stopTest();
        
        // Assert that the result is not null and contains the expected number of articles
        System.assertNotEquals(null, result, 'Result should not be null.');
        System.assertEquals(5, result.size(), 'Result size should be 5.');
        
        // Additional assertions to verify the content of the result
        for (Integer i = 0; i < result.size(); i++) {
            System.assertEquals('Test Article ' + i, result[i].Title, 'Title should match.');
            System.assertEquals('0000' + i, result[i].ArticleNumber, 'ArticleNumber should match.');
            System.assertEquals('Summary for Test Article ' + i, result[i].Summary, 'Summary should match.');
        }
    }
    
    // Dummy knowledgeArticleWrapper class as per assumption. Replace it with actual implementation if exists
    public class YourClassName {
        public class knowledgeArticleWrapper {
            public String Title;
            public String ArticleNumber;
            public String Summary;
            public String ValidationStatus;
            public String PublishStatus;
            public String KnowledgeArticleId;
            public Date LastPublishedDate;
            public Date LastModifiedDate;
            public Boolean IsVisibleInPkb;
            public String RecordTypeName;
            
            public knowledgeArticleWrapper(Knowledge__kav article) {
                this.Title = article.Title;
                this.ArticleNumber = article.ArticleNumber;
                this.Summary = article.Summary;
                this.ValidationStatus = article.ValidationStatus;
                this.PublishStatus = article.PublishStatus;
                this.KnowledgeArticleId = article.KnowledgeArticleId;
                this.LastPublishedDate = article.LastPublishedDate;
                this.LastModifiedDate = article.LastModifiedDate;
                this.IsVisibleInPkb = article.IsVisibleInPkb;
                this.RecordTypeName = 'FAQ'; // Assuming record type name is FAQ, replace as needed
            }
        }
        
        @AuraEnabled
        public static List<knowledgeArticleWrapper> getKnowledgeArticlesFromSearch(String searchText) {
            String searchQuery = 'FIND :searchText IN ALL FIELDS RETURNING Knowledge__kav(Id, Title, ArticleNumber, Summary,';
                searchQuery += ' ValidationStatus, PublishStatus, KnowledgeArticleId, LastPublishedDate, LastModifiedDate, IsVisibleInPkb,';
                searchQuery += ' RecordType.Name ';
                searchQuery += ' WHERE PublishStatus = \'Online\' ) LIMIT 10 UPDATE TRACKING';
                
            List<List<SObject>> searchList = new List<List<SObject>>();
            searchList = Search.query(searchQuery);
            if (!searchList.isEmpty()) {
                List<Knowledge__kav> knowledeArticles = (List<Knowledge__kav>) searchList[0];
                return generateWrapperData(knowledeArticles);
            }
            else return null;
        }

        private static List<knowledgeArticleWrapper> generateWrapperData(List<Knowledge__kav> articles) {
            List<knowledgeArticleWrapper> wrappers = new List<knowledgeArticleWrapper>();
            for (Knowledge__kav article : articles) {
                wrappers.add(new knowledgeArticleWrapper(article));
            }
            return wrappers;
        }
    }
}





@AuraEnabled
    public static List<knowledgeArticleWrapper> getKnowledgeArticlesFromSearch(String searchText) {

        String searchQuery = 'FIND :searchText IN ALL FIELDS RETURNING Knowledge__kav(Id, Title, ArticleNumber, Summary,';
            searchQuery += ' ValidationStatus, PublishStatus, KnowledgeArticleId, LastPublishedDate, LastModifiedDate, IsVisibleInPkb,';
            searchQuery += ' RecordType.Name ';
        /*    if (String.isNotEmpty(publishStatus)) {
                searchQuery += ' AND PublishStatus = :publishStatus';
            }
            
            if (!accountIds.isEmpty()) {
                searchQuery += ' AND ((Account__c IN :accountIds) OR (Account__c = null AND Knowledge_Type__c = \'Generic\'))';
            }
            */
            searchQuery += ' WHERE PublishStatus = \'Online\' ) LIMIT 10 UPDATE TRACKING';
            
            List<List<SObject>> searchList = new List<List<SObject>>();
            searchList = Search.query(searchQuery);
            if (!searchList.isEmpty()) {
                List<Knowledge__kav> knowledeArticles = (List<Knowledge__kav>) searchList[0];
                return generateWrapperData(knowledeArticles);
            }
            else return null;


    }



@isTest
private class TestInitializeAndLoadPlanData {

    @isTest
    static void testInitializeAndLoadPlanData() {
        // Test data setup
        String clientId = '001XXXXXXXXXXXXXXX';  // Replace with an appropriate clientId

        // Create test Account record
        Account testAccount = new Account(Name = 'Test Account', SSN__c = '123-45-6789');
        insert testAccount;

        // Create test Case record
        Case testCase = new Case(Status = 'New', Origin = 'Phone');
        insert testCase;

        // Create test CTI_Console_Pop__c record
        CTI_Console_Pop__c testCtiConsolePop = new CTI_Console_Pop__c(
            Account__c = testAccount.Id,
            CTI_Params__c = 'clientId:123-45-6789;VRUAPP:TestValue',
            DC_Serialized_Result__c = 'SerializedResult',
            Case__c = testCase.Id,
            Offer_Pop__c = 'SomeValue'
        );
        insert testCtiConsolePop;

        // Simulate the UltimatePopControllerHelper.getSFDCClientInfo method functionality
        Account client = new Account(Id = testAccount.Id, SSN__c = testAccount.SSN__c, Name = 'Mock Account');
        
        // Assuming initializeAvailablePlans is a method in UltimatePopControllerHelper
        List<UltimatePopControllerHelper.SearchResult> mockResults = new List<UltimatePopControllerHelper.SearchResult>();
        mockResults.add(new UltimatePopControllerHelper.SearchResult('Mock Plan', 'Mock Description'));

        Test.startTest();
        // Call the method under test
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAndLoadPlanData(testAccount.Id);
        Test.stopTest();

        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Add more assertions as needed to verify the correctness of the result
    }
    
    // Dummy SearchResult class as per assumption. Replace it with actual implementation if exists
    public class UltimatePopControllerHelper {
        public class SearchResult {
            public String name;
            public String description;
            
            public SearchResult(String name, String description) {
                this.name = name;
                this.description = description;
            }
        }
    }
}


Invalid type: MockUltimatePopControllerHelper

@isTest
private class TestInitializeAndLoadPlanData {

    @isTest
    static void testInitializeAndLoadPlanData() {
        // Test data setup
        String clientId = '001XXXXXXXXXXXXXXX';  // Replace with an appropriate clientId

        // Create test Account record
        Account testAccount = new Account(Name = 'Test Account', SSN__c = '123-45-6789');
        insert testAccount;

        // Create test Case record without setting CaseNumber
        Case testCase = new Case(Status = 'New', Origin = 'Phone');
        insert testCase;

        // Create test CTI_Console_Pop__c record
        CTI_Console_Pop__c testCtiConsolePop = new CTI_Console_Pop__c(
            Account__c = testAccount.Id,
            CTI_Params__c = 'clientId:123-45-6789;VRUAPP:TestValue',
            DC_Serialized_Result__c = 'SerializedResult',
            Case__c = testCase.Id,
            Offer_Pop__c = 'SomeValue'
        );
        insert testCtiConsolePop;

        // Mock the UltimatePopControllerHelper.getSFDCClientInfo method
        Test.startTest();
        Test.setMock(UltimatePopControllerHelper.class, new MockUltimatePopControllerHelper());
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAndLoadPlanData(testAccount.Id);
        Test.stopTest();

        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Add more assertions as needed to verify the correctness of the result
    }

    private class MockUltimatePopControllerHelper implements UltimatePopControllerHelper {
        public Account getSFDCClientInfo(String ssn, Boolean isActive) {
            return new Account(Id = '001XXXXXXXXXXXXXXX', SSN__c = ssn, Name = 'Mock Account');
        }

        public List<UltimatePopControllerHelper.SearchResult> initializeAvailablePlans(Case currentCase, String clientSSN, String test, Account client) {
            List<UltimatePopControllerHelper.SearchResult> mockResults = new List<UltimatePopControllerHelper.SearchResult>();
            mockResults.add(new UltimatePopControllerHelper.SearchResult('Mock Plan', 'Mock Description'));
            return mockResults;
        }
    }
}






Field is not writeable: Case.CaseNumber




@isTest
private class TestInitializeAndLoadPlanData {

    @isTest
    static void testInitializeAndLoadPlanData() {
        // Test data setup
        String clientId = '001XXXXXXXXXXXXXXX';  // Replace with an appropriate clientId

        // Create test Account record
        Account testAccount = new Account(Name = 'Test Account', SSN__c = '123-45-6789');
        insert testAccount;

        // Create test Case record
        Case testCase = new Case(Status = 'New', Origin = 'Phone', CaseNumber = '12345');
        insert testCase;

        // Create test CTI_Console_Pop__c record
        CTI_Console_Pop__c testCtiConsolePop = new CTI_Console_Pop__c(
            Account__c = testAccount.Id,
            CTI_Params__c = 'clientId:123-45-6789;VRUAPP:TestValue',
            DC_Serialized_Result__c = 'SerializedResult',
            Case__c = testCase.Id,
            Offer_Pop__c = 'SomeValue'
        );
        insert testCtiConsolePop;

        // Mock the UltimatePopControllerHelper.getSFDCClientInfo method
        Test.startTest();
        Test.setMock(UltimatePopControllerHelper.class, new MockUltimatePopControllerHelper());
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAndLoadPlanData(clientId);
        Test.stopTest();

        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Add more assertions as needed to verify the correctness of the result
    }

    private class MockUltimatePopControllerHelper implements UltimatePopControllerHelper {
        public Account getSFDCClientInfo(String ssn, Boolean isActive) {
            return new Account(Id = '001XXXXXXXXXXXXXXX', SSN__c = ssn, Name = 'Mock Account');
        }

        public List<UltimatePopControllerHelper.SearchResult> initializeAvailablePlans(Case currentCase, String clientSSN, String test, Account client) {
            List<UltimatePopControllerHelper.SearchResult> mockResults = new List<UltimatePopControllerHelper.SearchResult>();
            mockResults.add(new UltimatePopControllerHelper.SearchResult('Mock Plan', 'Mock Description'));
            return mockResults;
        }
    }
}



@AuraEnabled(cacheable=false)
public static List<UltimatePopControllerHelper.SearchResult> initializeAndLoadPlanData(String clientId){
    Case currentCase = new Case();
    //system.debug('Acc Id '+clientId);
	String clientSSN = '';
	String test = '';

  
    
    CTI_Console_Pop__c  ctiConsolePop = [select Id, Account__r.SSN__c, CTI_Params__c, DC_Serialized_Result__c, Case__c, 
        Case__r.Id, Case__r.CaseNumber, Offer_Pop__c, Offer_Pop__r.Id from CTI_Console_Pop__c 
        where account__c = :clientId order by LastModifiedDate  desc limit 1];
        String[] keyValuePairs = ctiConsolePop.CTI_Params__c.split(';');
        for(String pair : keyValuePairs) {
            String[] keyValue = pair.split(':');
            if(keyValue.size() == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
        
                if(key == 'clientId') {
                    clientSSN = value;
                } else if(key == 'VRUAPP') {
                    test = value;
                }
            }
        }
        
        //system.debug('clientSSN: ' + clientSSN);
        //system.debug('test: ' + test);
    Account client = UltimatePopControllerHelper.getSFDCClientInfo(ctiConsolePop.Account__r.SSN__c, true);
    
    List<Case> currentCaseList = [select Id, CaseNumber, Offers_Available__c from Case where Id = :ctiConsolePop.Case__r.Id];
    if(currentCaseList != null && currentCaseList.size() > 0)
        currentCase = currentCaseList.get(0);
    	
    String ctiParams = ctiConsolePop.CTI_Params__c;
    CurCase=ctiConsolePop.Case__r.Id;
    
    
    // Init Plan for Select list
    return initializeAvailablePlans(currentCase,clientSSN,test,client);
    






}




@isTest
private class TestCreateOpportunity {

    @isTest
    static void testCreateOpportunity() {
        // Test data setup
        String planId = 'TestPlan';
        String clientLastName = 'Doe';
        String campaignName = 'TestCampaign';
        String ownerId = UserInfo.getUserId();
        String response = 'Accepted';
        String responseReason = 'Valid Reason';
        String comment = 'Test Comment';

        // Create test Plan__c record
        Plan__c testPlan = new Plan__c(Name = planId);
        insert testPlan;

        // Call the method
        Test.startTest();
        String opportunityName = YourClassName.createOpportunity(planId, clientLastName, campaignName, ownerId, response, responseReason, comment);
        Test.stopTest();

        // Assert that the opportunity was created successfully
        Opportunity createdOpportunity = [SELECT Id, Name, StageName, CloseDate, Plan__c, OwnerId, Offer_Response__c, Offer_Response_Reason__c, Message_Comments__c 
                                          FROM Opportunity WHERE Name = :opportunityName];
        System.assertNotEquals(null, createdOpportunity, 'Opportunity should have been created.');
        System.assertEquals('Need Analysis', createdOpportunity.StageName, 'Opportunity StageName should be Need Analysis.');
        System.assertEquals(System.today(), createdOpportunity.CloseDate, 'Opportunity CloseDate should be today.');
        System.assertEquals(clientLastName + '-' + campaignName, createdOpportunity.Name, 'Opportunity Name should match the expected format.');
        System.assertEquals(testPlan.Id, createdOpportunity.Plan__c, 'Opportunity Plan__c should match the test plan Id.');
        System.assertEquals(ownerId, createdOpportunity.OwnerId, 'Opportunity OwnerId should match the input.');
        System.assertEquals(response, createdOpportunity.Offer_Response__c, 'Opportunity Offer_Response__c should match the input.');
        System.assertEquals(responseReason, createdOpportunity.Offer_Response_Reason__c, 'Opportunity Offer_Response_Reason__c should match the input.');
        System.assertEquals(comment, createdOpportunity.Message_Comments__c, 'Opportunity Message_Comments__c should match the input.');
    }
}




@AuraEnabled
    public static String createOpportunity( String planId,String clientLastName, String campaignName, string ownerId,string response, string responseReason, string comment) {
         List<Opportunity> oppList= new List<Opportunity>();
        system.debug('check Response '+ response);
         system.debug('check responseReason '+ responseReason);
         system.debug('check comment '+ comment);
         system.debug('check planId '+ planId);
     
         Plan__c p=(Plan__c)[Select Id,Name from Plan__c where Name=:planId Limit 1];
          system.debug('check  for Plan Id after SOQL  '+ p.Name);
        Opportunity opp= new opportunity();
        opp.StageName='Need Analysis';
        opp.CloseDate=system.today();
        opp.Name=clientLastName+'-'+campaignName;
        opp.Plan__c=p.Id;
        opp.Offer_Created_Manually__c=true;
       // opp.Plan__c=planId;
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
private class TestCreateCase {
    
    @isTest
    static void testCreateCase() {
        // Test data setup
        String comments = 'Test comments';
        String editRequestType = 'Test edit request type';
        String priority = 'High';
        Blob fileBody = Blob.valueOf('Test file content');
        String fileName = 'TestFileName.txt';
        
        // Call the method
        Test.startTest();
        Id caseId = YourClassName.createCase(comments, editRequestType, priority, fileBody, fileName);
        Test.stopTest();
        
        // Assert that the case was created successfully
        Case createdCase = [SELECT Id, Status, Origin, Description, Priority, Edit_Request_Type__c FROM Case WHERE Id = :caseId];
        System.assertNotEquals(null, createdCase, 'Case should have been created.');
        System.assertEquals('New', createdCase.Status, 'Case status should be New.');
        System.assertEquals('Call Center', createdCase.Origin, 'Case origin should be Call Center.');
        System.assertEquals(comments, createdCase.Description, 'Case description should match the input.');
        System.assertEquals(priority, createdCase.Priority, 'Case priority should match the input.');
        System.assertEquals(editRequestType, createdCase.Edit_Request_Type__c, 'Case edit request type should match the input.');
        
        // Assert that the attachment was created successfully
        Attachment createdAttachment = [SELECT Id, Name, ParentId FROM Attachment WHERE ParentId = :caseId];
        System.assertNotEquals(null, createdAttachment, 'Attachment should have been created.');
        System.assertEquals(fileName, createdAttachment.Name, 'Attachment name should match the input.');
        System.assertEquals(caseId, createdAttachment.ParentId, 'Attachment ParentId should match the created case Id.');
    }
}







@AuraEnabled
    public static Id createCase(String comments,String editRequestType,String priority,Blob fileBody, String fileName){
        List<Case> caseToInsert= new List<Case>();
         Case c =new Case();
        c.Status='New';
        c.Origin = 'Call Center' ;
         c.Description=comments;
        c.Priority=priority;
        c.Edit_Request_Type__c=editRequestType;
         caseToInsert.add(c);
        insert caseToInsert;
        System.debug('caseToInsert[0].Id'+caseToInsert[0].Id);
         List<Attachment> attachmentToInsert= new List<Attachment>();
        Attachment attachment  = new Attachment();
        attachment.Body = fileBody;
        attachment.Name = fileName;
        attachment.ParentId = caseToInsert[0].Id;  
        attachmentToInsert.add(attachment);
        insert attachmentToInsert; 
        return caseToInsert[0].Id;
    }


@isTest
private class TestGetKnowledgeArticlesFromSearch {
    
    @isTest
    static void testGetKnowledgeArticlesFromSearch() {
        // Test data setup
        String searchText = 'TestSearchText';
        
        // Create test Knowledge__kav records
        List<Knowledge__kav> testKnowledgeArticles = new List<Knowledge__kav>();
        // Add test Knowledge__kav records to testKnowledgeArticles list
        
        // Insert test Knowledge__kav records
        insert testKnowledgeArticles;
        
        // Mock the Search.query method
        Test.setFixedSearchResults(new List<List<SObject>>{testKnowledgeArticles});
        
        // Call the method
        Test.startTest();
        List<knowledgeArticleWrapper> result = YourClassName.getKnowledgeArticlesFromSearch(searchText);
        Test.stopTest();
        
        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Assert the size of the result list
        System.assertEquals(testKnowledgeArticles.size(), result.size(), 'Result list should contain the same number of elements as the test Knowledge__kav records.');
        
        // Add more assertions as needed to verify the correctness of the wrapper data
        
    }
}





@AuraEnabled
    public static List<knowledgeArticleWrapper> getKnowledgeArticlesFromSearch(String searchText) {

        String searchQuery = 'FIND :searchText IN ALL FIELDS RETURNING Knowledge__kav(Id, Title, ArticleNumber, Summary,';
            searchQuery += ' ValidationStatus, PublishStatus, KnowledgeArticleId, LastPublishedDate, LastModifiedDate, IsVisibleInPkb,';
            searchQuery += ' RecordType.Name ';
        /*    if (String.isNotEmpty(publishStatus)) {
                searchQuery += ' AND PublishStatus = :publishStatus';
            }
            
            if (!accountIds.isEmpty()) {
                searchQuery += ' AND ((Account__c IN :accountIds) OR (Account__c = null AND Knowledge_Type__c = \'Generic\'))';
            }
            */
            searchQuery += ' WHERE PublishStatus = \'Online\' ) LIMIT 10 UPDATE TRACKING';
            
            List<List<SObject>> searchList = new List<List<SObject>>();
            searchList = Search.query(searchQuery);
            if (!searchList.isEmpty()) {
                List<Knowledge__kav> knowledeArticles = (List<Knowledge__kav>) searchList[0];
                return generateWrapperData(knowledeArticles);
            }
            else return null;


    }


@isTest
private class TestGetPaagFields {
    
    @isTest
    static void testGetPaagFields() {
        // Test data setup
        String recordId = 'TestRecordId';
        String fieldNames = 'Field1,Field2,Field3'; // Add comma-separated field names
        
        // Create test paag__c record
        paag__c testPaagRecord = new paag__c();
        testPaagRecord.Field1 = 'Value1';
        testPaagRecord.Field2 = 'Value2';
        testPaagRecord.Field3 = 'Value3';
        // Add values for other required fields
        insert testPaagRecord;
        
        // Call the method
        Test.startTest();
        List<Map<String, String>> result = YourClassName.getPaagFields(recordId, fieldNames);
        Test.stopTest();
        
        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Assert the size of the result list
        System.assertEquals(1, result.size(), 'Result list should contain one map.');
        
        // Assert the values in the map
        Map<String, String> fieldValues = result[0];
        System.assertEquals('Value1', fieldValues.get('Field1'), 'Field1 value should match.');
        System.assertEquals('Value2', fieldValues.get('Field2'), 'Field2 value should match.');
        System.assertEquals('Value3', fieldValues.get('Field3'), 'Field3 value should match.');
        // Add assertions for other fields as needed
    }
}



@AuraEnabled(cacheable=true)
public static List<Map<String, String>> getPaagFields(String recordId, String fieldNames) {
    List<Map<String, String>> result = new List<Map<String, String>>();
    fieldNames = fieldNames.replaceAll('blank,', '');
    // Split the comma-separated field names into a List
    List<String> fieldsList = fieldNames.split(',');

    // Use a Set to store unique field names
    Set<String> uniqueFieldNames = new Set<String>();

    // Filter out duplicate field names and populate the Set
    for (String fieldName : fieldsList) {
        if (!uniqueFieldNames.contains(fieldName)) {
            uniqueFieldNames.add(fieldName);
        }
    }

    // Convert the Set back to a List
    List<String> filteredFieldsList = new List<String>(uniqueFieldNames);

    // Query the specified fields for the paag__c record
    String queryFields = String.join(filteredFieldsList, ',');
    String soqlQuery = 'SELECT ' + queryFields + ' FROM paag__c WHERE Id = :recordId LIMIT 1';

    List<paag__c> paagRecords = Database.query(soqlQuery);

    for (paag__c record : paagRecords) {
        Map<String, String> fieldValues = new Map<String, String>();

        // Populate the field values in the map
        for (String fieldName : filteredFieldsList) {
            String fieldValue = String.valueOf(record.get(fieldName));
            fieldValues.put(fieldName, fieldValue);
        }

        result.add(fieldValues);
    }

    return result;
}



@isTest
private class TestGetNomalizedScore {
    
    @isTest
    static void testGetNomalizedScore() {
        // Test data setup
        Set<Id> articleIds = new Set<Id>();
        // Add test article Ids to the articleIds set
        
        // Load test data for KnowledgeArticleViewStat
        List<sObject> testData = Test.loadData(KnowledgeArticleViewStat.sObjectType, 'TestDataFileName');
        
        // Call the method
        Test.startTest();
        Map<Id,String> result = YourClassName.getNomalizedScore(articleIds);
        Test.stopTest();
        
        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Assert other conditions based on your logic and test data
        // For example, check if the returned map contains expected Ids and corresponding normalized scores.
    }
}




@isTest
private class TestGetNomalizedScore {
    
    @isTest
    static void testGetNomalizedScore() {
        // Test data setup
        Set<Id> articleIds = new Set<Id>();
        // Add test article Ids to the articleIds set
        
        // Create test data for KnowledgeArticleViewStat
        List<KnowledgeArticleViewStat> kavs = new List<KnowledgeArticleViewStat>();
        // Add test KnowledgeArticleViewStat records to kavs list
        
        // Insert test KnowledgeArticleViewStat records
        insert kavs;
        
        // Call the method
        Test.startTest();
        Map<Id,String> result = YourClassName.getNomalizedScore(articleIds);
        Test.stopTest();
        
        // Assert that the result is not null
        System.assertNotEquals(null, result, 'Result should not be null.');
        
        // Assert other conditions based on your logic and test data
        // For example, check if the returned map contains expected Ids and corresponding normalized scores.
    }
}





public static Map<Id,String> getNomalizedScore(Set<Id> articleId){
  	  
      	Map<Id,Decimal> articleIdToNormScoreMap=new Map<Id,Decimal>();
          Map<Id,String> articleIdToNormScoreMapNew=new Map<Id,String>();

      if(Test.isRunningTest()){
        articleIdToNormScoreMap.put([SELECT NormalizedScore, Id, ParentId FROM KnowledgeArticleViewStat WHERE ParentId in :articleId limit 1].Id,Decimal.valueOf(100));
      }
      else {
      	List<KnowledgeArticleViewStat>  kavs = Database.query('SELECT NormalizedScore, Id, ParentId FROM KnowledgeArticleViewStat WHERE ParentId in :articleId  ORDER BY NormalizedScore desc');
      	
       
        
        
        for( KnowledgeArticleViewStat cc:kavs ){
            Integer normscore;
            String StringValue;
        normscore = cc.NormalizedScore.round().intValue();
            
      if(normscore < 20)
        
      StringValue = String.valueOf(0);
        else if(normscore >= 20 && normscore < 40 )
        StringValue =  String.valueOf(20);
        
        else if(normscore >= 40 && normscore < 60 )
          
        StringValue =  String.valueOf(40);
        else if(normscore >= 60 && normscore < 80 )
          
        StringValue =  String.valueOf(60);
        else if(normscore >= 80 && normscore <   100 )
          
        StringValue =  String.valueOf(80);
        else
          
        StringValue = String.valueOf(100);

        if (articleIdToNormScoreMap.containsKey(cc.ParentId) && articleIdToNormScoreMapNew.containsKey(cc.ParentId)) {
            system.debug('Knowledge Test '+cc.parentId+' - '+articleIdToNormScoreMap.get(cc.ParentId));
            Decimal currentScore = articleIdToNormScoreMap.get(cc.ParentId);
            if (cc.normalizedScore > currentScore) {
                articleIdToNormScoreMap.put(cc.ParentId, cc.normalizedScore);
                articleIdToNormScoreMapNew.put(cc.parentId,StringValue);
            }
        } else {
            articleIdToNormScoreMap.put(cc.ParentId, cc.normalizedScore);
            articleIdToNormScoreMapNew.put(cc.parentId,StringValue);
        }

    }
}
return articleIdToNormScoreMapNew;
  }
