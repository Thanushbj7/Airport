List<knowledgeArticleWrapper> kaWrapperList = new List<knowledgeArticleWrapper>();
    for (Knowledge__kav kav : knowledeArticles) {
        knowledgeArticleWrapper kaWrap = new knowledgeArticleWrapper();
        kaWrap.id = kav.Id;
        kaWrap.title = kav.Title;
        kaWrap.articleNumber = kav.ArticleNumber;
        kaWrap.validationStatus = kav.ValidationStatus;
        kaWrap.publishStatus = kav.PublishStatus;
        kaWrap.knowledgeArticleId = kav.KnowledgeArticleId;
        kaWrap.summary = kav.Summary;
        kaWrap.recordTypeName = kav.RecordType.Name;
        kaWrap.visibleInPublicKnowledgeBase = kav.IsVisibleInPkb;
        kaWrap.viewCountDS = scoreMap.get(kav.KnowledgeArticleId);
       
        
            kaWrap.displayGenericKnowArticles = true;
        
        
            kaWrap.displayCopytoClipboardIcon = false;
        
         

        if (kav.PublishStatus == 'Draft') {
            kaWrap.articleAttachedToCase = false;
        }
        
        if (kav.PublishStatus.equalsIgnoreCase('Online') && kaWrap.lastPublishedDate!=null) {
            kaWrap.lastPublishedDate = kav.LastPublishedDate.format('MMM d, yyyy, HH:mm a');
        } 
        
        if (kav.PublishStatus.equalsIgnoreCase('Draft')) {
            kaWrap.lastModifiedDate = kav.LastModifiedDate.format('MMM d, yyyy, HH:mm a');
        }
        
        kaWrapperList.add(kaWrap); 
    }

    return kaWrapperList;
}


@isTest
private class UltimatePopControllerHelperTest {

    @isTest
    static void testInitializeAvailablePlans() {
        // Arrange
        Case currentCase = new Case(
            Id = '5001X00001234AB',
            CaseNumber = '12345'
        );
        
        String clientSSN = '123-45-6789';
        String ctiVRUApp = 'TestApp';
        
        Account client = new Account(
            Id = '0011X00001234AB',
            SSN__c = '123-45-6789'
        );
        
        // Insert sample Plan records
        Plan__c plan1 = new Plan__c(
            Id = 'a001X00001234AB',
            Native_Plan_ID__c = 'None'
        );
        
        Plan__c plan2 = new Plan__c(
            Id = 'a001X00005678CD',
            Native_Plan_ID__c = 'MYVOYA'
        );
        
        insert new List<Plan__c>{ plan1, plan2 };

        // Insert mock data for CTI_Console_Pop__c
        CTI_Console_Pop__c ctiConsolePop = new CTI_Console_Pop__c(
            ExternalID__c = (UserInfo.getUserId() + ConstantUtils.UNIQUE_SEPERATOR + clientSSN),
            DC_Serialized_Result__c = '[{"planId":"Plan123"}]',
            Case__c = currentCase.Id
        );
        
        insert ctiConsolePop;

        // Mock the loadPlans method
        Test.startTest();
        Test.setMock(ApexMocks.class, new UltimatePopControllerHelperMock());
        
        // Act
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAvailablePlans(currentCase, clientSSN, ctiVRUApp, client);
        Test.stopTest();
        
        // Assert
        System.assertNotEquals(null, result, 'The result list should not be null.');
        System.assertEquals(1, result.size(), 'The result list should contain 1 element.');
        System.assertEquals('Plan123', result.get(0).planId, 'Plan ID should match.');
    }
    
    // Mock class for the loadPlans method
    private class UltimatePopControllerHelperMock implements HttpCalloutMock {
        public HTTPResponse respond(HTTPRequest req) {
            HttpResponse res = new HttpResponse();
            res.setStatusCode(200);
            res.setBody('[{"planId":"Plan123"}]');
            return res;
        }
    }
}





@isTest
private class UltimatePopControllerHelperTest {
    
    private static Case createSampleCase() {
        return new Case(
            Id = '5001X00001234AB',
            CaseNumber = '12345'
        );
    }
    
    private static Account createSampleAccount() {
        return new Account(
            Id = '0011X00001234AB',
            SSN__c = '123-45-6789'
        );
    }

    private static List<Plan__c> createSamplePlans() {
        List<Plan__c> plans = new List<Plan__c>();
        
        Plan__c plan1 = new Plan__c(
            Id = 'a001X00001234AB',
            Native_Plan_ID__c = 'None'
        );
        
        Plan__c plan2 = new Plan__c(
            Id = 'a001X00005678CD',
            Native_Plan_ID__c = 'MYVOYA'
        );
        
        plans.add(plan1);
        plans.add(plan2);

        insert plans;

        return plans;
    }

    @isTest
    static void testInitializeAvailablePlans() {
        // Arrange
        Case currentCase = createSampleCase();
        String clientSSN = '123-45-6789';
        String ctiVRUApp = 'TestApp';
        Account client = createSampleAccount();
        List<Plan__c> samplePlans = createSamplePlans();

        // Mock the loadPlans method
        Test.startTest();
        Test.setMock(ApexMocks.class, new UltimatePopControllerHelperMock());

        // Insert mock data for CTI_Console_Pop__c
        CTI_Console_Pop__c ctiConsolePop = new CTI_Console_Pop__c(
            ExternalID__c = (UserInfo.getUserId() + ConstantUtils.UNIQUE_SEPERATOR + clientSSN),
            DC_Serialized_Result__c = '[{"planId":"Plan123"}]',
            Case__c = currentCase.Id
        );
        insert ctiConsolePop;

        // Act
        List<UltimatePopControllerHelper.SearchResult> result = YourClassName.initializeAvailablePlans(currentCase, clientSSN, ctiVRUApp, client);
        Test.stopTest();
        
        // Assert
        System.assertNotEquals(null, result, 'The result list should not be null.');
        System.assertEquals(1, result.size(), 'The result list should contain 1 element.');
        System.assertEquals('Plan123', result[0].planId, 'Plan ID should match.');
    }
    
    // Mock class for the loadPlans method
    private class UltimatePopControllerHelperMock implements HttpCalloutMock {
        public HTTPResponse respond(HTTPRequest req) {
            HttpResponse res = new HttpResponse();
            res.setStatusCode(200);
            res.setBody('[{"planId":"Plan123"}]');
            return res;
        }
    }
}




public static List<UltimatePopControllerHelper.SearchResult> initializeAvailablePlans(Case currentCase,String clientSSN,String ctiVRUApp,Account client ) {
    List<Selectoption> planIdSelectList = new List<Selectoption>();
    List<Plan__c> p  = [Select Native_Plan_ID__c, id from Plan__c where Native_Plan_ID__c in ('None')];
    String paPlanId; 
    CTI_Console_Pop__c ctiConsolePop = new CTI_Console_Pop__c();
    Participant_Alert__c newPAlert = new Participant_Alert__c();
    String externalId = (UserInfo.getUserId() + ConstantUtils.UNIQUE_SEPERATOR + clientSSN); 
    List<UltimatePopControllerHelper.SearchResult> dcSearchResults;
    try {
        ctiConsolePop = [select Id, Account__r.SSN__c, DC_Serialized_Result__c,
         Case__c, Case__r.Id, Case__r.CaseNumber from CTI_Console_Pop__c where ExternalID__c = :externalId limit 1];
        dcSearchResults = (List<UltimatePopControllerHelper.SearchResult>)JSON.deserialize(ctiConsolePop.DC_Serialized_Result__c, List<UltimatePopControllerHelper.SearchResult>.class);
    }
    catch(Exception e) {
        //system.debug('initializeAvailablePlans() failed due to : ' + e + '[' + externalId + ']');
    }
    if(dcSearchResults != null && dcSearchResults.size() > 0) {
        //this.planIdSelectList.add(new Selectoption('', '--None--'));
        planIdSelectList.add(new Selectoption('All Plans', 'All Plans'));
        for(UltimatePopControllerHelper.SearchResult sr : dcSearchResults) {
            if(sr.planId !=null) {
                planIdSelectList.add(new Selectoption(sr.planId, sr.planId));
            }
        }
        //system.debug('Vru App '+ctiVRUApp);
        // Start -Added as part of MY VOYA chnages.
       /* if(ctiVRUApp.equalsIgnoreCase('MYVOYA'))
        {
            for(Plan__c pc:p)
            {
               if(pc.Native_Plan_ID__c.equalsIgnoreCase('MYVOYA'))
                   planIdSelectList.add(new Selectoption(pc.Native_Plan_ID__c, pc.Native_Plan_ID__c));
            }
        }*/
        
        // End-Added as part of MY VOYA chnages.
        //system.debug('================================================ this.planIdSelectList > ' + planIdSelectList);
        
        //PlanId will be defaulted if only 1 Plan is returned from OLTP/PDAB
        if(dcSearchResults != null && dcSearchResults.size() == 1 && dcSearchResults.get(0).planId != null ) {
            
            paPlanId = dcSearchResults.get(0).planId;
            
            if(newPAlert != null )
                newPAlert.PlanID_Text__c = dcSearchResults.get(0).planId;
            
        }
        
        //system.debug('================================================ initializeAvailablePlans() - this.paPlanId > ' + paPlanId);
    }
    else {
        /*  Plan__c p  = [Select Native_Plan_ID__c, id from Plan__c where Native_Plan_ID__c = 'None' limit 1]; 
this.planIdSelectList.add(new Selectoption(p.Native_Plan_ID__c, 'None'));
this.planIdSelectList.add(new Selectoption('All Plans', 'All Plans')); */
        planIdSelectList.add(new Selectoption('', '--None--'));
        for(Plan__c pc:p)
        {
            if(pc.Native_Plan_ID__c =='None')
                planIdSelectList.add(new Selectoption(pc.Native_Plan_ID__c, 'None'));
          //  if(pc.Native_Plan_ID__c =='MYVOYA' && ctiVRUApp =='MYVOYA')
            //    planIdSelectList.add(new Selectoption(pc.Native_Plan_ID__c, 'MYVOYA'));
        }
        
    }
    List<UltimatePopControllerHelper.SearchResult> dcSearchResults2 = loadPlans(ctiConsolePop,currentCase,client);
    //system.debug('dcSearchResults2 '+dcSearchResults2);
    return dcSearchResults2;
}



@isTest
private class KnowledgeArticleWrapperTest {
    
    private static List<Knowledge__kav> createSampleKnowledgeArticles() {
        List<Knowledge__kav> knowledgeArticles = new List<Knowledge__kav>();
        
        // Creating sample knowledge articles
        Knowledge__kav article1 = new Knowledge__kav(
            Id = '0011X00001234AB',
            Title = 'Sample Article 1',
            ArticleNumber = '0001',
            ValidationStatus = 'Validated',
            PublishStatus = 'Online',
            KnowledgeArticleId = 'KA0001',
            Summary = 'Summary of Article 1',
            RecordTypeId = '0123456789',
            IsVisibleInPkb = true,
            LastPublishedDate = DateTime.now().addDays(-10),
            LastModifiedDate = DateTime.now().addDays(-5)
        );

        Knowledge__kav article2 = new Knowledge__kav(
            Id = '0011X00005678CD',
            Title = 'Sample Article 2',
            ArticleNumber = '0002',
            ValidationStatus = 'Validated',
            PublishStatus = 'Draft',
            KnowledgeArticleId = 'KA0002',
            Summary = 'Summary of Article 2',
            RecordTypeId = '0123456789',
            IsVisibleInPkb = false,
            LastPublishedDate = DateTime.now().addDays(-15),
            LastModifiedDate = DateTime.now().addDays(-2)
        );

        knowledgeArticles.add(article1);
        knowledgeArticles.add(article2);

        return knowledgeArticles;
    }
    
    @isTest
    static void testGenerateWrapperData() {
        // Arrange
        List<Knowledge__kav> sampleArticles = createSampleKnowledgeArticles();
        
        // Mock the getNomalizedScore method
        Test.startTest();
        Test.setMock(ApexMocks.class, new KnowledgeArticleWrapperMock());
        
        // Act
        List<knowledgeArticleWrapper> result = YourClassName.generateWrapperData(sampleArticles);
        Test.stopTest();
        
        // Assert
        System.assertEquals(2, result.size(), 'The result list should contain 2 elements.');
        
        knowledgeArticleWrapper article1 = result[0];
        System.assertEquals('0011X00001234AB', article1.id, 'ID should match.');
        System.assertEquals('Sample Article 1', article1.title, 'Title should match.');
        System.assertEquals('0001', article1.articleNumber, 'Article number should match.');
        System.assertEquals('Validated', article1.validationStatus, 'Validation status should match.');
        System.assertEquals('Online', article1.publishStatus, 'Publish status should match.');
        System.assertEquals('KA0001', article1.knowledgeArticleId, 'Knowledge Article ID should match.');
        System.assertEquals('Summary of Article 1', article1.summary, 'Summary should match.');
        System.assertEquals('0123456789', article1.recordTypeName, 'Record Type Name should match.');
        System.assertEquals(true, article1.visibleInPublicKnowledgeBase, 'Visibility in public knowledge base should match.');
        System.assertNotEquals(null, article1.viewCountDS, 'View Count should not be null.');
        System.assertEquals(true, article1.displayGenericKnowArticles, 'Display Generic Knowledge Articles should be true.');
        System.assertEquals(false, article1.displayCopytoClipboardIcon, 'Display Copy to Clipboard Icon should be false.');
        System.assertEquals(false, article1.articleAttachedToCase, 'Article should not be attached to case.');
        System.assertNotEquals(null, article1.lastPublishedDate, 'Last Published Date should not be null.');
        System.assertEquals(null, article1.lastModifiedDate, 'Last Modified Date should be null for online articles.');
        
        knowledgeArticleWrapper article2 = result[1];
        System.assertEquals('0011X00005678CD', article2.id, 'ID should match.');
        System.assertEquals('Sample Article 2', article2.title, 'Title should match.');
        System.assertEquals('0002', article2.articleNumber, 'Article number should match.');
        System.assertEquals('Validated', article2.validationStatus, 'Validation status should match.');
        System.assertEquals('Draft', article2.publishStatus, 'Publish status should match.');
        System.assertEquals('KA0002', article2.knowledgeArticleId, 'Knowledge Article ID should match.');
        System.assertEquals('Summary of Article 2', article2.summary, 'Summary should match.');
        System.assertEquals('0123456789', article2.recordTypeName, 'Record Type Name should match.');
        System.assertEquals(false, article2.visibleInPublicKnowledgeBase, 'Visibility in public knowledge base should match.');
        System.assertNotEquals(null, article2.viewCountDS, 'View Count should not be null.');
        System.assertEquals(true, article2.displayGenericKnowArticles, 'Display Generic Knowledge Articles should be true.');
        System.assertEquals(false, article2.displayCopytoClipboardIcon, 'Display Copy to Clipboard Icon should be false.');
        System.assertEquals(false, article2.articleAttachedToCase, 'Article should not be attached to case.');
        System.assertEquals(null, article2.lastPublishedDate, 'Last Published Date should be null for draft articles.');
        System.assertNotEquals(null, article2.lastModifiedDate, 'Last Modified Date should not be null.');
    }
    
    // Mock class for the getNomalizedScore method
    private class KnowledgeArticleWrapperMock implements HttpCalloutMock {
        public HTTPResponse respond(HTTPRequest req) {
            HttpResponse res = new HttpResponse();
            res.setStatusCode(200);
            res.setBody('{"KA0001":"10","KA0002":"5"}');
            return res;
        }
    }
}




private static List<knowledgeArticleWrapper> generateWrapperData(List<Knowledge__kav> knowledeArticles) {
    // Add the existing Articles attached to Case
    Set<Id> existingKAIds = new Set<Id>();
    Set<Id> storeIdForView = new Set<Id>();
    for (Knowledge__kav kav : knowledeArticles) {
        storeIdForView.add(kav.KnowledgeArticleId);
    }
    map<Id,String> scoreMap = new map<Id,String>();
    
    scoreMap = getNomalizedScore(storeIdForView);
    

    List<knowledgeArticleWrapper> kaWrapperList = new List<knowledgeArticleWrapper>();
    for (Knowledge__kav kav : knowledeArticles) {
        knowledgeArticleWrapper kaWrap = new knowledgeArticleWrapper();
        kaWrap.id = kav.Id;
        kaWrap.title = kav.Title;
        kaWrap.articleNumber = kav.ArticleNumber;
        kaWrap.validationStatus = kav.ValidationStatus;
        kaWrap.publishStatus = kav.PublishStatus;
        kaWrap.knowledgeArticleId = kav.KnowledgeArticleId;
        kaWrap.summary = kav.Summary;
        kaWrap.recordTypeName = kav.RecordType.Name;
        kaWrap.visibleInPublicKnowledgeBase = kav.IsVisibleInPkb;
        kaWrap.viewCountDS = scoreMap.get(kav.KnowledgeArticleId);
       
        
            kaWrap.displayGenericKnowArticles = true;
        
        
            kaWrap.displayCopytoClipboardIcon = false;
        
         

        if (kav.PublishStatus == 'Draft') {
            kaWrap.articleAttachedToCase = false;
        }
        
        if (kav.PublishStatus.equalsIgnoreCase('Online') && kaWrap.lastPublishedDate!=null) {
            kaWrap.lastPublishedDate = kav.LastPublishedDate.format('MMM d, yyyy, HH:mm a');
        } 
        
        if (kav.PublishStatus.equalsIgnoreCase('Draft')) {
            kaWrap.lastModifiedDate = kav.LastModifiedDate.format('MMM d, yyyy, HH:mm a');
        }
        
        kaWrapperList.add(kaWrap); 
    }

    return kaWrapperList;
}








@isTest
    static void testgetKnowledgeId() {
        String Id='Test Id';
        String tmArticleName='Test Title';
        Knowledge__kav testKnowledge=new Knowledge__kav(Title=tmArticleName);
        testKnowledge.UrlName='https:voyasmartworks--accpcc2.sandbox.my.salesforce.com/_ui/common/apex/debug/ApexCSIPage?action=selectTests';
        insert testKnowledge;
         Test.startTest();
        Id KnowledgeId=cTargetedMessage.getKnowledgeId(tmArticleName);
        Test.stopTest();
         Knowledge__kav createdKnowledge=[select Id,Title from Knowledge__kav where Title=:tmArticleName];
        System.assertNotEquals(null, createdKnowledge, 'Knowledge__kav should have been created.');
        
    }


System.DmlException: Insert failed. First exception on row 0; first error: FIELD_INTEGRITY_EXCEPTION, Invalid URL name. The URL name can include only unicode characters and hyphens, and it can't begin or end with a hyphen.: [UrlName]


@isTest
public class CaseActionWrapperTest {

    @isTest
    static void testCaseActionWrapper() {
        // Step 1: Setup test data
        Case_Actions__c caseAction = new Case_Actions__c(
            // Initialize required fields here
            Name = 'Test Case Action'
            // Add other required fields if necessary
        );
        
        insert caseAction; // Insert the record to simulate real scenario (if necessary)

        String createdDate = '2024-05-24 12:34:56';
        
        // Step 2: Instantiate CaseActionWrapper
        CaseActionWrapper wrapper = new CaseActionWrapper(caseAction, createdDate);
        
        // Step 3: Assert results
        System.assertNotEquals(null, wrapper.caseAction, 'caseAction should not be null');
        System.assertEquals(caseAction.Id, wrapper.caseAction.Id, 'caseAction ID should match');
        System.assertEquals('Test Case Action', wrapper.caseAction.Name, 'caseAction Name should match');
        System.assertEquals(createdDate, wrapper.createdDate, 'createdDate should match');
    }
}





public Case_Actions__c caseAction {get;set;}
    public String createdDate {get;set;}
    
    public CaseActionWrapper(Case_Actions__c caseAction, string createdDate) {
        this.caseAction = caseAction;
        this.createdDate = createdDate;
    }



@isTest
public class KnowledgeArticleWrapperTest {

    @isTest
    static void testGetNomalizedScore() {
        // Step 1: Setup test data
        List<KnowledgeArticleViewStat> viewStats = new List<KnowledgeArticleViewStat>();
        
        Id articleId1 = 'kaId1';
        Id articleId2 = 'kaId2';
        
        // Create KnowledgeArticleViewStat records
        KnowledgeArticleViewStat stat1 = new KnowledgeArticleViewStat();
        stat1.Id = 'statId1';
        stat1.ParentId = articleId1;
        stat1.NormalizedScore = 15;
        viewStats.add(stat1);
        
        KnowledgeArticleViewStat stat2 = new KnowledgeArticleViewStat();
        stat2.Id = 'statId2';
        stat2.ParentId = articleId1;
        stat2.NormalizedScore = 45;
        viewStats.add(stat2);
        
        KnowledgeArticleViewStat stat3 = new KnowledgeArticleViewStat();
        stat3.Id = 'statId3';
        stat3.ParentId = articleId2;
        stat3.NormalizedScore = 75;
        viewStats.add(stat3);
        
        KnowledgeArticleViewStat stat4 = new KnowledgeArticleViewStat();
        stat4.Id = 'statId4';
        stat4.ParentId = articleId2;
        stat4.NormalizedScore = 95;
        viewStats.add(stat4);
        
        insert viewStats;
        
        // Step 2: Set up the article IDs
        Set<Id> articleIds = new Set<Id>{articleId1, articleId2};
        
        // Step 3: Call the method
        Test.startTest();
        Map<Id, String> result = YourClassName.getNomalizedScore(articleIds);
        Test.stopTest();
        
        // Step 4: Assert results
        System.assertEquals(2, result.size(), 'Expected two entries in the result map');
        
        // Verify the normalized scores
        System.assertEquals('40', result.get(articleId1), 'Expected normalized score for articleId1 to be 40');
        System.assertEquals('100', result.get(articleId2), 'Expected normalized score for articleId2 to be 100');
    }
    
    // Mock class for the getNomalizedScore method when running tests
    @isTest
    private static void testGetNomalizedScoreWhenRunningTest() {
        // Step 1: Setup test data
        List<KnowledgeArticleViewStat> viewStats = new List<KnowledgeArticleViewStat>();
        
        Id articleId1 = 'kaId1';
        
        // Create KnowledgeArticleViewStat records
        KnowledgeArticleViewStat stat1 = new KnowledgeArticleViewStat();
        stat1.Id = 'statId1';
        stat1.ParentId = articleId1;
        stat1.NormalizedScore = 15;
        viewStats.add(stat1);
        
        insert viewStats;
        
        // Step 2: Set up the article IDs
        Set<Id> articleIds = new Set<Id>{articleId1};
        
        // Step 3: Call the method
        Test.startTest();
        Map<Id, String> result = YourClassName.getNomalizedScore(articleIds);
        Test.stopTest();
        
        // Step 4: Assert results
        System.assertEquals(1, result.size(), 'Expected one entry in the result map');
        
        // Verify the normalized scores
        System.assertEquals('100', result.get(articleId1), 'Expected normalized score for articleId1 to be 100 due to test context');
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








@isTest
public class KnowledgeArticleWrapperTest {
    
    @isTest
    static void testGenerateWrapperData() {
        // Step 1: Setup test data
        List<Knowledge__kav> knowledgeArticles = new List<Knowledge__kav>();
        
        Knowledge__kav kav1 = new Knowledge__kav();
        kav1.Id = 'kavId1';
        kav1.Title = 'Test Title 1';
        kav1.ArticleNumber = '000001';
        kav1.ValidationStatus = 'Approved';
        kav1.PublishStatus = 'Draft';
        kav1.KnowledgeArticleId = 'kaId1';
        kav1.Summary = 'Test Summary 1';
        kav1.RecordTypeId = Schema.SObjectType.Knowledge__kav.getRecordTypeInfosByName().get('RecordType1').getRecordTypeId();
        kav1.IsVisibleInPkb = true;
        kav1.LastModifiedDate = System.now();
        
        Knowledge__kav kav2 = new Knowledge__kav();
        kav2.Id = 'kavId2';
        kav2.Title = 'Test Title 2';
        kav2.ArticleNumber = '000002';
        kav2.ValidationStatus = 'Pending';
        kav2.PublishStatus = 'Online';
        kav2.KnowledgeArticleId = 'kaId2';
        kav2.Summary = 'Test Summary 2';
        kav2.RecordTypeId = Schema.SObjectType.Knowledge__kav.getRecordTypeInfosByName().get('RecordType2').getRecordTypeId();
        kav2.IsVisibleInPkb = false;
        kav2.LastPublishedDate = System.now();
        
        knowledgeArticles.add(kav1);
        knowledgeArticles.add(kav2);
        
        // Step 2: Mock the getNomalizedScore method
        Test.startTest();
        
        // Mocking the getNomalizedScore method call
        // Here we assume that this method is in the same class, you might need to adjust the class name
        // Replace 'YourClassName' with the actual class name that contains the generateWrapperData method
        Test.setMock(HttpCalloutMock.class, new MockNomalizedScoreResponse());
        
        // Step 3: Call the method
        List<YourClassName.knowledgeArticleWrapper> result = YourClassName.generateWrapperData(knowledgeArticles);
        
        Test.stopTest();
        
        // Step 4: Assert results
        System.assertEquals(2, result.size(), 'Expected two knowledgeArticleWrapper objects');
        
        // Verify the first wrapper
        YourClassName.knowledgeArticleWrapper kaWrap1 = result[0];
        System.assertEquals('kavId1', kaWrap1.id);
        System.assertEquals('Test Title 1', kaWrap1.title);
        System.assertEquals('000001', kaWrap1.articleNumber);
        System.assertEquals('Approved', kaWrap1.validationStatus);
        System.assertEquals('Draft', kaWrap1.publishStatus);
        System.assertEquals('kaId1', kaWrap1.knowledgeArticleId);
        System.assertEquals('Test Summary 1', kaWrap1.summary);
        System.assertTrue(kaWrap1.visibleInPublicKnowledgeBase);
        System.assertEquals(false, kaWrap1.articleAttachedToCase);
        System.assertEquals(kav1.LastModifiedDate.format('MMM d, yyyy, HH:mm a'), kaWrap1.lastModifiedDate);
        
        // Verify the second wrapper
        YourClassName.knowledgeArticleWrapper kaWrap2 = result[1];
        System.assertEquals('kavId2', kaWrap2.id);
        System.assertEquals('Test Title 2', kaWrap2.title);
        System.assertEquals('000002', kaWrap2.articleNumber);
        System.assertEquals('Pending', kaWrap2.validationStatus);
        System.assertEquals('Online', kaWrap2.publishStatus);
        System.assertEquals('kaId2', kaWrap2.knowledgeArticleId);
        System.assertEquals('Test Summary 2', kaWrap2.summary);
        System.assertFalse(kaWrap2.visibleInPublicKnowledgeBase);
        System.assertEquals(kav2.LastPublishedDate.format('MMM d, yyyy, HH:mm a'), kaWrap2.lastPublishedDate);
    }
    
    // Mock class for the getNomalizedScore method
    private class MockNomalizedScoreResponse implements HttpCalloutMock {
        public HTTPResponse respond(HTTPRequest req) {
            HttpResponse res = new HttpResponse();
            res.setHeader('Content-Type', 'application/json');
            res.setBody('{"kaId1": "10", "kaId2": "20"}');
            res.setStatusCode(200);
            return res;
        }
    }
}





private static List<knowledgeArticleWrapper> generateWrapperData(List<Knowledge__kav> knowledeArticles) {
    // Add the existing Articles attached to Case
    Set<Id> existingKAIds = new Set<Id>();
    Set<Id> storeIdForView = new Set<Id>();
    for (Knowledge__kav kav : knowledeArticles) {
        storeIdForView.add(kav.KnowledgeArticleId);
    }
    map<Id,String> scoreMap = new map<Id,String>();
    
    scoreMap = getNomalizedScore(storeIdForView);
    

    List<knowledgeArticleWrapper> kaWrapperList = new List<knowledgeArticleWrapper>();
    for (Knowledge__kav kav : knowledeArticles) {
        knowledgeArticleWrapper kaWrap = new knowledgeArticleWrapper();
        kaWrap.id = kav.Id;
        kaWrap.title = kav.Title;
        kaWrap.articleNumber = kav.ArticleNumber;
        kaWrap.validationStatus = kav.ValidationStatus;
        kaWrap.publishStatus = kav.PublishStatus;
        kaWrap.knowledgeArticleId = kav.KnowledgeArticleId;
        kaWrap.summary = kav.Summary;
        kaWrap.recordTypeName = kav.RecordType.Name;
        kaWrap.visibleInPublicKnowledgeBase = kav.IsVisibleInPkb;
        kaWrap.viewCountDS = scoreMap.get(kav.KnowledgeArticleId);
       
        
            kaWrap.displayGenericKnowArticles = true;
        
        
            kaWrap.displayCopytoClipboardIcon = false;
        
         

        if (kav.PublishStatus == 'Draft') {
            kaWrap.articleAttachedToCase = false;
        }
        
        if (kav.PublishStatus.equalsIgnoreCase('Online') && kaWrap.lastPublishedDate!=null) {
            kaWrap.lastPublishedDate = kav.LastPublishedDate.format('MMM d, yyyy, HH:mm a');
        } 
        
        if (kav.PublishStatus.equalsIgnoreCase('Draft')) {
            kaWrap.lastModifiedDate = kav.LastModifiedDate.format('MMM d, yyyy, HH:mm a');
        }
        
        kaWrapperList.add(kaWrap); 
    }

    return kaWrapperList;
}
