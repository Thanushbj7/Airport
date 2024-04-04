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
