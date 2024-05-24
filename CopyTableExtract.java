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
