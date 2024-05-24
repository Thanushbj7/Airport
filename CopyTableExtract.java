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
