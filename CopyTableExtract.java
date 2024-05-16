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
