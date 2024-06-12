static testMethod void testUltimate() {
        
        //Querying for Account record Type.
        RecordType[] rt =[Select Id, IsPersonType, Name, SobjectType from RecordType 
                          where  SobjectType ='Account' 
                          and Name ='Client' 
                          and IsPersonType = true];
        
        Plan__c p1 = new Plan__c(Name = 'TEST_PLAN1', Native_Plan_ID__c = 'TEST_PLAN1', Alias_Plan_ID__c = 'TEST_PLAN1', Plan_Name_ist__c = 'TEST_PLAN1', IPS_Access_Level__c = '1', RR_Plan__c = 'Yes', Non_Qualified_Plan__c = 'Yes');
        Plan__c p2 = new Plan__c(Name = 'Test plan', Native_Plan_ID__c = '1245', Alias_Plan_ID__c = '1245', Plan_Name_ist__c = '1245', IPS_Access_Level__c = null, RR_Plan__c = 'Yes', Non_Qualified_Plan__c = 'No');
        Plan__c p3 = new Plan__c(Native_Plan_ID__c = 'TEST_PLAN3', Alias_Plan_ID__c = 'TEST_PLAN3', Plan_Name_ist__c = 'TEST_PLAN3', IPS_Access_Level__c = null, RR_Plan__c = 'Yes', Non_Qualified_Plan__c = 'No');
        Plan__c p4 = new Plan__c(Native_Plan_ID__c = 'TEST_PLAN4', Alias_Plan_ID__c = 'TEST_PLAN4', Plan_Name_ist__c = 'TEST_PLAN4', IPS_Access_Level__c = null, RR_Plan__c = 'Yes', Non_Qualified_Plan__c = 'Yes');
        insert(new List<Plan__c>{p1, p2, p3, p4});
        
        Plan_Permissions__c pp1 = new Plan_Permissions__c(Service_Description__c = 'Rollover', Special_Rollover_Instructions__c = 'Test Instructions', Planid__c = p1.Id);
        Plan_Permissions__c pp2 = new Plan_Permissions__c(Service_Description__c = 'Rollover', Special_Rollover_Instructions__c = 'Test Instructions', Planid__c = p2.Id);
        Plan_Permissions__c pp3 = new Plan_Permissions__c(Service_Description__c = 'Rollover', Special_Rollover_Instructions__c = 'Test Instructions', Planid__c = p3.Id);
        Plan_Permissions__c pp4 = new Plan_Permissions__c(Service_Description__c = 'Rollover', Special_Rollover_Instructions__c = 'Test Instructions', Planid__c = p4.Id, Targeted_Messages__c = 'No');
        insert(new List<Plan_Permissions__c>{pp1, pp2, pp3, pp4});
        
        //Test Data 1
        //creating the test record
        Account acc = new Account(firstName='null',lastName = 'test',ssn__c = '123456781', RecordTypeId = rt[0].Id, 
                                  PersonMailingCity = 'test', PersonMailingState = 'test', Batch_Id__c  = '123', RR_Eligible__pc = true,Web_Registered__c=true,Online_Planning_Indicator__c='Yes');
        acc.Plan__c = p1.Id;
        acc.Plan_ID_Billing_Group__c = p1.Native_Plan_ID__c;
        insert acc;
        
        //inserting Offer_Pop__c test record
        Offer_Pop__c testop =new Offer_Pop__c(Source__c='CTI',Client__c=acc.id,OfferPop_Transaction_ID__c='test');
        insert testop;
        
        //inserting Client_Offer__c test record
        Client_Offer__c testClient =new Client_Offer__c(Account_Ext_ID__c='123456781', Account_Last_Name__c='test',Status_Rollover__c='open',Score_Rollover__c=1.32,PlanId_rollover__c=p1.id);
        insert testClient;
        
        //inserting campaign test record
        Campaign testCam = new Campaign (Name='Rolltest',Offer_Code__c='rollover',External_ID__c='2311232131232');
        insert testCam;
        
        //inserting Rule__c test record.                
        Rule__c testRule =new Rule__c(Value_ist__c=testCam.id, Name='tDNIS', Rule_Group_ist__c='Campaign-Lead-Source-Translation');
        insert testRule;
        
        // inserting PAAG_EXCEPTION__C
        PAAG_Exceptions__c PAAGExp = new PAAG_Exceptions__c();
        PAAGExp.Plan__c = p1.Id;
        PAAGExp.Client_ID__c = 'Test';
        insert PAAGExp;
        
        //Insetting online planning
        Online_Planning__c Onlineplan = new Online_Planning__c(Client__c = acc.id);
        Onlineplan.Online_Plan_Id_RR__c = 'test';
        Onlineplan.Last_Login_In_Date_RR__c = Date.today();
        insert Onlineplan;
        User testUser = TestUtilsSBR.createUser('System Administrator'); 
          Profile p = [SELECT Id FROM Profile WHERE Name='System Administrator' limit 1];
        p = [SELECT Id FROM Profile WHERE Name='Backoffice_IST' limit 1];
            p = [SELECT Id FROM Profile WHERE Name='Bus Tech' limit 1];
            p = [SELECT Id FROM Profile WHERE Name='CSA Management' limit 1];

        System.runAs(testUser){    
            //Test 1 :This is for the Client existing only in in OLTP/PDAB
            
            
            //Setting the current page and its parameters in the url.
            Test.startTest();
            preventRecursive.testUser();
            Test.setCurrentPage(Page.UltimatePop);
            Apexpages.currentPage().getParameters().put('ClientID', '123456781');
            Apexpages.currentPage().getParameters().put('opID',testop.id);
            Apexpages.currentPage().getParameters().put('source', 'CTI');
            Apexpages.currentPage().getParameters().put('DNIS', 'tDNIS');
            
           
            ApexPages.StandardController controller = new ApexPages.StandardController(new Account());
          
            
           
            Apexpages.currentPage().getParameters().put('ClientID', '123456781');
            Apexpages.currentPage().getParameters().put('opID', testop.id);
            Apexpages.currentPage().getParameters().put('source', 'CTI');
            
            
            String external = (UserInfo.getUserId() + ConstantUtils.UNIQUE_SEPERATOR + '123456781');
            system.debug('external%%%'+external);
            CTI_Console_Pop__c CTIRecord = new CTI_Console_Pop__c(Account__c=acc.id, CTI_Params__c='clientID:123456784;Source:CTI;DNIS:5811261;VRUAPP:myvoya;EDU:12345678901;type:test;UUID:1234',
                                                                  DC_Serialized_Result__c= '[{"unformattedAccountValue":"321045","state":"Test State","ssn":null,"servicingRepName":"Test rep","servicingRepId":"12312456222","salesOutcome":null,"rrPlan":null,"rowNum":2,"roeInstructions":"ROE Passed","roe":"true","postalCode":"20136","planName":"CIRS 402(k) Savings Plan","planId":"321045","phone":null,"participantId":"545237777","outcomeDate":null,"oppStatus":null,"oppStage":null,"oppOwner":null,"oppName":null,"oppId":null,"oppAtRisk":null,"managedAccount":"N/A","lastName":"Esc last","isPAAGException":null,"ipsAccess":"-1","firstName":"Esc First","emplomentStatusCode":"Active_Eligible","emplomentStatus":"Active, Eligible","eeId":null,"duplicate":null,"dateOfBirth":"Test","createdDate":null,"clientId":"3245","city":"Test City","address3":"address3","address2":"address2","address1":"address1","address":"address1 address2 address3 Test City Test State 20136 ","accountValue":"$321,045","accountType":null,"accountOwner":null,"accountName":"Esc last, Esc First","accountId":null}]', externalID__c=external);
            insert CTIRecord;
           
            UltimatePopControllerHelper.getPlanEmployeeStatusMapFromDC('123456781');
            UltimatePopControllerHelper.initClientOffers('123456781',false,true);
            UltimatePopControllerHelper.getWebActivities(acc,'MYVOYA');
            UltimatePopControllerHelper.PlanClientObj wrapp = new UltimatePopControllerHelper.PlanClientObj();
            UltimatePopControllerHelper.PAAGWrapper pagwrap = new UltimatePopControllerHelper.PAAGWrapper();
            UltimatePopControllerHelper.callService(null, null, null, null);
            pagwrap.paag = new PAAG__c();
            pagwrap.planId = p1.Native_Plan_ID__c;
            pagwrap.planName = p1.Name;
            pagwrap.planSFDCId = p1.Id;
            pagwrap.isException = false;
            
                       
            UltimatePopControllerHelper.getPlanAccountValueMapFromDC('123455');
            list<UltimatePopControllerHelper.PlanClientObj> wrapList = new list<UltimatePopControllerHelper.PlanClientObj>();
            wrapp.clientId ='test';
            wrapp.planId = p1.Native_Plan_ID__c;
            wrapList.add(wrapp);
            
            list<UltimatePopControllerHelper.PlanClientObj> EmptywrapList = new list<UltimatePopControllerHelper.PlanClientObj>();
            
            
            PAAG__c pa = new PAAG__c();
            pa.Plan__c = p1.id;
            pa.Active__c = 'true';
            insert pa;
            
            UltimatePopControllerHelper.getPAAGPlanMap(wrapList);
            UltimatePopControllerHelper.getPAAGPlanMap(EmptywrapList);
            UltimatePopControllerHelper.ProfileInfo pwrapper = new UltimatePopControllerHelper.ProfileInfo();
            UltimatePopControllerHelper.formatAmount('3445','$');
            pwrapper.hasCSAnRelatedProfile = true;
            pwrapper.hasAdminNRelatedProfile = true;
            UltimatePopControllerHelper.getCurrentUserProfileInfo(p.id);
            
           
            UltimatePopController testUltPopController1 = new UltimatePopController(controller);
            
            testUltPopController1.targetMessageType='Rollover'; 

            
            testUltPopController1.initializePOP();
            testUltPopController1.client = acc;
            testUltPopController1.client.PersonMailingState='testState';
            testUltPopController1.saveClientChanges();
            testUltPopController1.createTargetMessage();
            testUltPopController1.recInteraction = 'No';
            testUltPopController1.doRolloverOppCreationValidationForDC();       
            system.assertEquals(testUltPopController1.client.PersonMailingState,'testState');
            //testUltPopController1.createSnapshotOpportunity();
            testUltPopController1.navigateToOfferDetails();
            testUltPopController1.logEMoneyClick();
            testUltPopController1.vfClientOfferList= null;
            testUltPopController1.createTargetMessage();
            testUltPopController1.existingOfferPopId = '123456';
            testUltPopController1.createOpportunity();
            testUltPopController1.setNewOfferDetails();
            testUltPopController1.offerPop = testop;
            
            Apexpages.currentPage().getParameters().put('ClientID', '');
            Apexpages.currentPage().getParameters().put('ssn', acc.SSN__c);
            UltimatePopController testUltPopController2 = new UltimatePopController(controller);
            
         
            
            
            
            pwrapper = new UltimatePopControllerHelper.ProfileInfo();
            UltimatePopControllerHelper.getCurrentUserProfileInfo(p.id);
            
            pwrapper = new UltimatePopControllerHelper.ProfileInfo();
            UltimatePopControllerHelper.getCurrentUserProfileInfo(p.id);
            pwrapper = new UltimatePopControllerHelper.ProfileInfo();
            UltimatePopControllerHelper.getCurrentUserProfileInfo(p.id);            
            Test.stopTest();
            
        }
        String selectedRepTIN='Test selectedRepTIN';
        RecordType producerRecType = new RecordType(
            Name = 'Producer', 
            sObjectType = 'Account', 
            DeveloperName = 'Producer'
        );
       // insert producerRecType;

        Opportunity newOpp = new Opportunity(
            Name = 'Test Opportunity',
            CloseDate = Date.today(),
            StageName = 'Prospecting',
            Agent_Name__c=acc.Id
        );
        insert newOpp;

     
        System.assertNotEquals(null,newOpp.Agent_Name__c,'cheking not null');
        
        
    } 





@isTest
public class UltimatePopControllerTest {
    @isTest
    static void testConstructorWithUrlParams() {
        // Set up test data
        Account testAccount = new Account(Name = 'Test Account');
        insert testAccount;

        // Create a standard controller with a dummy Opportunity
        Opportunity testOpportunity = new Opportunity(
            Name = 'Test Opportunity',
            AccountId = testAccount.Id,
            CloseDate = Date.today(),
            StageName = 'Prospecting'
        );
        insert testOpportunity;
        
        Test.startTest();
        
        // Scenario 1: urlPostParams.size() == 3
        PageReference pageRef1 = Page.SomePage;
        pageRef1.getParameters().put('clientID', 'clientSSN1&DNIS=dnis1&Source=source1');
        Test.setCurrentPage(pageRef1);
        ApexPages.StandardController sc1 = new ApexPages.StandardController(testOpportunity);
        UltimatePopController controller1 = new UltimatePopController(sc1);
        System.assertEquals('clientSSN1', controller1.clientSSN);
        System.assertEquals('dnis1', controller1.dnisNumber);
        System.assertEquals('source1', controller1.source);
        
        // Scenario 2: urlPostParams.size() == 5
        PageReference pageRef2 = Page.SomePage;
        pageRef2.getParameters().put('clientID', 'clientSSN2&DNIS=dnis2&ctiVRUApp=app2&ctiEDU=edu2&Source=source2');
        Test.setCurrentPage(pageRef2);
        ApexPages.StandardController sc2 = new ApexPages.StandardController(testOpportunity);
        UltimatePopController controller2 = new UltimatePopController(sc2);
        System.assertEquals('clientSSN2', controller2.clientSSN);
        System.assertEquals('dnis2', controller2.dnisNumber);
        System.assertEquals('app2', controller2.ctiVRUApp);
        System.assertEquals('edu2', controller2.ctiEDU);
        System.assertEquals('source2', controller2.source);
        
        // Scenario 3: urlPostParams.size() == 6
        PageReference pageRef3 = Page.SomePage;
        pageRef3.getParameters().put('clientID', 'clientSSN3&DNIS=dnis3&ctiVRUApp=app3&ctiEDU=edu3&securityParameter=sec3&Source=source3');
        Test.setCurrentPage(pageRef3);
        ApexPages.StandardController sc3 = new ApexPages.StandardController(testOpportunity);
        UltimatePopController controller3 = new UltimatePopController(sc3);
        System.assertEquals('clientSSN3', controller3.clientSSN);
        System.assertEquals('dnis3', controller3.dnisNumber);
        System.assertEquals('app3', controller3.ctiVRUApp);
        System.assertEquals('edu3', controller3.ctiEDU);
        System.assertEquals('sec3', controller3.securityParameter);
        System.assertEquals('source3', controller3.source);
        
        // Scenario 4: urlPostParams.size() == 7
        PageReference pageRef4 = Page.SomePage;
        pageRef4.getParameters().put('clientID', 'clientSSN4&DNIS=dnis4&ctiVRUApp=app4&ctiEDU=edu4&securityParameter=sec4&caseOrigin=origin4&Source=source4');
        Test.setCurrentPage(pageRef4);
        ApexPages.StandardController sc4 = new ApexPages.StandardController(testOpportunity);
        UltimatePopController controller4 = new UltimatePopController(sc4);
        System.assertEquals('clientSSN4', controller4.clientSSN);
        System.assertEquals('dnis4', controller4.dnisNumber);
        System.assertEquals('app4', controller4.ctiVRUApp);
        System.assertEquals('edu4', controller4.ctiEDU);
        System.assertEquals('sec4', controller4.securityParameter);
        System.assertEquals('origin4', controller4.caseOrigin);
        System.assertEquals('source4', controller4.source);
        
        Test.stopTest();
    }
}









public UltimatePopController(ApexPages.StandardController stdController) {
        
         system.debug('====================== ApexPages.currentPage().getParameters().get(id) ' + ApexPages.currentPage().getParameters().get('id'));
        
        showRRLink = false;
        
        isCreateOppInteraction = false;
        
        showPlanAuthorizationSection = false;
        
        String encPostParams = null;
        
        //Ex - ../apex/SearchPage?clientID=273143143%26DNIS%3D*%26Source%3Dcti&reason=NOT-AUTHENTICATED
        //encPostParams = ApexPages.currentPage().getParameters().get('clientID');
        
        if(ApexPages.currentPage().getParameters().get('clientID') != null && ApexPages.currentPage().getParameters().get('clientID') != '') {
            encPostParams = ApexPages.currentPage().getParameters().get('clientID');
            
        }else if(ApexPages.currentPage().getParameters().get('ssn') != null && ApexPages.currentPage().getParameters().get('ssn') != '') {
            encPostParams = ApexPages.currentPage().getParameters().get('ssn');
        }
           
        //Decoded Form ../apex/SearchPage?clientID=273143143&DNIS=*&Source=cti
        
        List<String> urlPostParams = null;
        if(encPostParams != null && encPostParams != '') {
            encPostParams = EncodingUtil.urlDecode(encPostParams, 'UTF-8');
            urlPostParams = encPostParams.split('&');
        }   
        
        if(urlPostParams != null && urlPostParams.size() == 3) {
         clientSSN = urlPostParams[0];
            dnisNumber = (urlPostParams[1].split('=').size() == 2 ? urlPostParams[1].split('=')[1] : '');
            source = (urlPostParams[2].split('=').size() == 2 ? (String)urlPostParams[2].split('=')[1] : '');
        }
        else if(urlPostParams != null && urlPostParams.size() == 5) {
         clientSSN = urlPostParams[0];
            dnisNumber = (urlPostParams[1].split('=').size() == 2 ? urlPostParams[1].split('=')[1] : '');
            this.ctiVRUApp = (urlPostParams[2].split('=').size() == 2 ? urlPostParams[2].split('=')[1] : '');
            this.ctiEDU = (urlPostParams[3].split('=').size() == 2 ? urlPostParams[3].split('=')[1] : '');
            source = (urlPostParams[4].split('=').size() == 2 ? (String)urlPostParams[4].split('=')[1] : '');
        }
        else if(urlPostParams != null && urlPostParams.size() == 6) {
         clientSSN = urlPostParams[0];
            dnisNumber = (urlPostParams[1].split('=').size() == 2 ? urlPostParams[1].split('=')[1] : '');
            this.ctiVRUApp = (urlPostParams[2].split('=').size() == 2 ? urlPostParams[2].split('=')[1] : '');
            this.ctiEDU = (urlPostParams[3].split('=').size() == 2 ? urlPostParams[3].split('=')[1] : '');
            securityParameter = (urlPostParams[4].split('=').size() == 2 ? (String)urlPostParams[4].split('=')[1] : '');
            this.source = (urlPostParams[5].split('=').size() == 2 ? (String)urlPostParams[5].split('=')[1] : '');
            
        } else if(urlPostParams != null && urlPostParams.size() == 7) {
            this.clientSSN = urlPostParams[0];
            this.dnisNumber = (urlPostParams[1].split('=').size() == 2 ? urlPostParams[1].split('=')[1] : '');
            this.ctiVRUApp = (urlPostParams[2].split('=').size() == 2 ? urlPostParams[2].split('=')[1] : '');
            this.ctiEDU = (urlPostParams[3].split('=').size() == 2 ? urlPostParams[3].split('=')[1] : '');
            this.securityParameter = (urlPostParams[4].split('=').size() == 2 ? (String)urlPostParams[4].split('=')[1] : '');
            this.caseOrigin = (urlPostParams[5].split('=').size() == 2 ? (String)urlPostParams[5].split('=')[1] : '');
            this.source = (urlPostParams[6].split('=').size() == 2 ? (String)urlPostParams[6].split('=')[1] : '');
            
        }else {
            
            //clientSSN = ApexPages.currentPage().getParameters().get('clientID');
            if(ApexPages.currentPage().getParameters().get('clientID') != null && ApexPages.currentPage().getParameters().get('clientID') != '') {
                clientSSN = ApexPages.currentPage().getParameters().get('clientID');
                
            }else if(ApexPages.currentPage().getParameters().get('ssn') != null && ApexPages.currentPage().getParameters().get('ssn') != '') {
                clientSSN = ApexPages.currentPage().getParameters().get('ssn');
            }
            dnisNumber = ApexPages.currentPage().getParameters().get('DNIS');
            source = ApexPages.currentPage().getParameters().get('Source');
            this.ctiVRUApp = ApexPages.currentPage().getParameters().get('vruApp');
            this.ctiEDU = ApexPages.currentPage().getParameters().get('ctiEDU');
            this.caseOrigin = ApexPages.currentPage().getParameters().get('type');
            this.securityParameter = ApexPages.currentPage().getParameters().get('AuthenticatedFlag');
        }
        
        //To Avoid Null Pointers
        if(clientSSN != null)
            clientSSN = clientSSN.replace('*','');
        if(dnisNumber != null)
            dnisNumber = dnisNumber.replace('*','');
        if(source != null)
            source = source.replace('*','');
        
        if(ApexPages.currentPage().getParameters().get('opID') != null && ApexPages.currentPage().getParameters().get('opID').length() > 0) {
            List<Offer_Pop__c> offerList = [Select o.User__c, o.User_Role__c, o.User_Profile__c, o.Top_Offer__c, o.Source__c, o.Opportunity__c, o.Offers_Available__c, o.Name, o.Lead_Source__c, o.Client__c, o.Client_ID__c, o.CTI_DNIS_Number__c, o.Action__c From Offer_Pop__c o Where Id =: ApexPages.currentPage().getParameters().get('opID')];
            if(offerList.size() > 0)
                offerPop = offerList[0];
        }
        
        //Initialize variables
        dcSearchResults = new List<UltimatePopControllerHelper.SearchResult>();
        opportunitySearchResults = new List<UltimatePopControllerHelper.oppWrapper>();
        targetedMessageResults = new List<Client_Offer__c>();
        
        /////////////// SECURITY - Profile Checks //////////////////
        initProfileVars();
        /////////////////////////////////////////////////////////////
        
        if(recordTypeMap.containsKey('Retirement Readiness'+ '-' +'Opportunity')) {
            RRRecordType = recordTypeMap.get('Retirement Readiness'+ '-' +'Opportunity');
        }
        
        loggedInUser = [select eMoney_User_Type__c, eMoney_ID__c, UserRole.Name from User where id = :UserInfo.getUserId()];
        
    }










@isTest
public class GlobalSearchAndReplace_Test {
    @isTest
    static void testGlobalSearchAndReplace() {
        // Create test data
        List<sObject> batchArticles = new List<sObject>();
        
        // Create a test article with fields to match the search pattern
        Knowledge__kav testArticle1 = new Knowledge__kav();
        testArticle1.ArticleNumber = 'A001';
        testArticle1.Title = 'Test Article 1';
        testArticle1.KnowledgeArticleId = 'KA001';
        testArticle1.IsMasterLanguage = true;
        testArticle1.SomeField__c = 'This is a test field containing the search string.';
        batchArticles.add(testArticle1);
        
        // Create another test article without the search pattern
        Knowledge__kav testArticle2 = new Knowledge__kav();
        testArticle2.ArticleNumber = 'A002';
        testArticle2.Title = 'Test Article 2';
        testArticle2.KnowledgeArticleId = 'KA002';
        testArticle2.IsMasterLanguage = false;
        testArticle2.SomeField__c = 'This field does not contain the search string.';
        batchArticles.add(testArticle2);
        
        // Initialize the class with the necessary context and parameters
        MyClass myClassInstance = new MyClass();
        myClassInstance.strSearchString = 'search string';
        myClassInstance.listSearchFields = new List<String>{'SomeField__c'};
        myClassInstance.bSearchOnly = false;
        myClassInstance.strApexSearchJobId = 'TestJobId';
        myClassInstance.strArticleType = 'Knowledge__kav';
        myClassInstance.strPublishStatus = 'Draft';
        myClassInstance.bPublishNewVersion = true;
        myClassInstance.strLanguage = 'en_US';
        myClassInstance.strReplacementString = 'replacement string';
        myClassInstance.bMultiLingualKB = true;
        
        // Invoke the method
        Test.startTest();
        myClassInstance.globalSearchAndReplace(batchArticles);
        Test.stopTest();
        
        // Verify the results
        List<KB_Global_Search_And_Replace__c> gsRecords = [SELECT ArticleNumber__c, Field_Names__c, Search_String__c, Replacement_String__c FROM KB_Global_Search_And_Replace__c];
        
        // There should be one record created for testArticle1
        System.assertEquals(1, gsRecords.size(), 'One KB_Global_Search_And_Replace__c record should be created.');
        KB_Global_Search_And_Replace__c gsRecord = gsRecords[0];
        System.assertEquals('A001', gsRecord.ArticleNumber__c, 'Article number should match the first article.');
        System.assertEquals('SomeField__c', gsRecord.Field_Names__c, 'Field name should match the search field.');
        System.assertEquals('search string', gsRecord.Search_String__c, 'Search string should match.');
        System.assertEquals('replacement string', gsRecord.Replacement_String__c, 'Replacement string should match.');
    }
}






public void globalSearchAndReplace(List<sObject> batchArticles){
    	// Compile the user-defined search expression
        String strRegEx = this.strSearchString;
        Pattern strSearchStringPattern = Pattern.compile(strRegEx);  
        
        // Initialize list of replacement tasks (work items) for this batch scope
        List<KB_Global_Search_And_Replace__c> listGSR = new List<KB_Global_Search_And_Replace__c>();
        
        // Iterate across all articles queried in a single batch scope
        for(sObject article : batchArticles) {
            this.TotalCount++;
            
            // Determine if one (or more) selected field values contains the user-defined search expression.
            // This logic is used to identify which field(s) in a specific article require replacement.
            // The physical replacement is performed by a separate class (BatchKnowledgeGlobalReplace) to
            // support the much smaller scope size (50) required due to governor constraints specific to
            // knowledge article updates (which are not bulk-enabled) 
            String strReplacementFields = '';              
            if (listSearchFields.size() > 0) {
                for (String strFieldName: listSearchFields) {
                	// Apply pattern to next field value
                    String strFieldValue = (String)article.get(strFieldName);
                    if (strFieldValue == null) continue;
                    Matcher matcher = strSearchStringPattern.matcher(strFieldValue);

					// If this field value contains at least one occurrence of pattern, add to list
                    if (matcher.find()) {
                        if (strReplacementFields.length()>0) strReplacementFields += ',';
                        strReplacementFields +=strFieldName;
                    }
                }
            }
            
            // If at least one field has been identified, add article to replacement queue and audit log
            if (strReplacementFields.length()>0) {
                this.UpdateCount++;  

				// Add selected article to audit log
                this.listSelectedArticles.add('Article Number='+(String)article.get('ArticleNumber')+
                    ' Title='+(String)article.get('Title')+' Language='+strLanguage+'\n');
                
                // If performing a physical replacement, add new record (replacement task) to work queue.  This record
                // serves to identify those articles to be processed in the BatchKnowledgeGlobalReplace class.
                // These records stand independently and identify which field(s) in a specific article require replacement.
                if (!bSearchOnly) {         
                    KB_Global_Search_And_Replace__c modifyArticle = new KB_Global_Search_And_Replace__c();
                    
                    // Copy metadata from batch article to work queue
                    modifyArticle.Apex_BatchId__c = this.strApexSearchJobId;
                    modifyArticle.ArticleId__c = article.Id;
                    modifyArticle.Article_Type__c = this.strArticleType;
                    modifyArticle.ArticleNumber__c = (String)article.get('ArticleNumber'); 
                    modifyArticle.KnowledgeArticleId__c = (String)article.get('KnowledgeArticleId'); 
                    modifyArticle.PublishStatus__c = this.strPublishStatus;
                    modifyArticle.PublishNewVersion__c = String.valueOf(this.bPublishNewVersion);
                    modifyArticle.Language__c = String.valueOf(this.strLanguage);
                    modifyArticle.Field_Names__c = strReplacementFields;
                    modifyArticle.Search_String__c = this.strSearchString;
                    modifyArticle.Replacement_String__c = this.strReplacementString;
					if (bMultiLingualKB) {
						modifyArticle.IsMasterLanguage__c = String.valueOf((Boolean)article.get('IsMasterLanguage'));
					} else {
						modifyArticle.IsMasterLanguage__c = 'NA';
					}                

					// Add replacement task to list
                  	listGSR.add(modifyArticle);
                }                
            }
        }
        
        // If at least one replacement task has been identified, insert into physical work queue
        if (!bSearchOnly && listGSR != null && listGSR.size() > 0) {
        	this.bExecuteReplacement = true;
            try {
                Database.insert(listGSR);      
            } catch (Exception ex){
                String errMsg = ex.getMessage();
                system.Debug(errMsg);
            } 
         }        

    }







@isTest
public class OpportunityCreation_Test {

    @isTest
    static void testOpportunityCreation() {
        // Create a test user with System Administrator profile
        Profile p = [SELECT Id FROM Profile WHERE Name = 'System Administrator'];
        User testUser = new User(
            Alias = 'tUser',
            Email = 'testuser@example.com',
            EmailEncodingKey = 'UTF-8',
            LastName = 'Testing',
            LanguageLocaleKey = 'en_US',
            LocaleSidKey = 'en_US',
            ProfileId = p.Id,
            TimeZoneSidKey = 'America/Los_Angeles',
            UserName = 'testuser@example.com'
        );
        insert testUser;
        
        System.runAs(testUser) {
            // Create test Account
            Account testAccount = new Account(
                Name = 'Test Account',
                LastName = 'Test',
                RecordTypeId = Schema.SObjectType.Account.getRecordTypeInfosByName().get('Client').getRecordTypeId()
            );
            insert testAccount;
            
            // Create test Plan
            Plan__c testPlan = new Plan__c(Name = 'Test Plan', Producer_TIN_ist__c = '121231234');
            insert testPlan;
            
            // Create a RecordType for Opportunity
            RecordType oppRecordType = [SELECT Id FROM RecordType WHERE SObjectType = 'Opportunity' AND Name = 'Snapshot' LIMIT 1];
            
            // Mock selectedAccountValue
            String selectedAccountValue = '10000.00';
            
            // Mock selectedPlanId
            Id selectedPlanId = testPlan.Id;
            
            // Mock selectedRepTIN
            String selectedRepTIN = '123456789';
            
            // Mock dnisNumber
            String dnisNumber = '12345';
            
            // Create the Opportunity
            Opportunity newOpp = new Opportunity();
            newOpp.recordtypeid = oppRecordType.Id;
            newOpp.AccountId = testAccount.Id;
            newOpp.Name = testAccount.LastName + ' - Snapshot';
            newOpp.CloseDate = Date.today().addMonths(1);
            newOpp.StageName = 'Engage';
            newOpp.Plan__c = selectedPlanId;
            newOpp.OwnerId = UserInfo.getUserId();
            
            if (!String.isBlank(selectedAccountValue)) {
                newOpp.at_Risk__c = Decimal.valueOf(selectedAccountValue);
            }
                    
            newOpp.Offer_Plan_Number__c = selectedPlanId;
            newOpp.LeadSource = 'Test Lead Source'; // Assuming doLeadSourceTranslation returns 'Test Lead Source'
            newOpp.CampaignId = null; // Assuming getCampaignInfo returns null for simplicity
            newOpp.AgentTIN__c = selectedRepTIN;
            
            // Insert the Opportunity
            Test.startTest();
            UltimatePopControllerHelper.doTransaction(newOpp, UltimatePopControllerHelper.TRANSACTION_INSERT);
            Test.stopTest();
            
            // Assertions to verify the Opportunity was created correctly
            Opportunity insertedOpp = [SELECT Id, Name, AccountId, CloseDate, StageName, Plan__c, at_Risk__c, Offer_Plan_Number__c, LeadSource, CampaignId, AgentTIN__c FROM Opportunity WHERE Id = :newOpp.Id];
            System.assertEquals(testAccount.Id, insertedOpp.AccountId);
            System.assertEquals(testAccount.LastName + ' - Snapshot', insertedOpp.Name);
            System.assertEquals(Date.today().addMonths(1), insertedOpp.CloseDate);
            System.assertEquals('Engage', insertedOpp.StageName);
            System.assertEquals(selectedPlanId, insertedOpp.Plan__c);
            System.assertEquals(Decimal.valueOf(selectedAccountValue), insertedOpp.at_Risk__c);
            System.assertEquals(selectedPlanId, insertedOpp.Offer_Plan_Number__c);
            System.assertEquals('Test Lead Source', insertedOpp.LeadSource);
            System.assertEquals(null, insertedOpp.CampaignId);
            System.assertEquals(selectedRepTIN, insertedOpp.AgentTIN__c);
        }
    }
}






Opportunity newOpp = new Opportunity();
            newOpp.recordtypeid = snapshotOppRecordType.Id;
            newOpp.AccountId = this.client.Id;
            newOpp.Name = this.client.LastName + ' - Snapshot';
            //newOpp.Opportunity_Status__c = 'New';
            newOpp.CloseDate = Date.today().addMonths(1);
            newOpp.StageName = 'Engage';
            newOpp.Plan__c = sfdcPlanId;
            newOpp.OwnerId = UserInfo.getUserId();
            
            if(!String.isBlank(this.selectedAccountValue))
                    newOpp.at_Risk__c = Decimal.valueOf(this.selectedAccountValue);
                    
            newOpp.Offer_Plan_Number__c = selectedPlanId;
            newOpp.LeadSource = doLeadSourceTranslation(dnisNumber);
            newOpp.CampaignId = getCampaignInfo(dnisNumber, this.client);
            newOpp.AgentTIN__c = selectedRepTIN;
            
            //Insert the Opportunity
            UltimatePopControllerHelper.doTransaction(newOpp, UltimatePopControllerHelper.TRANSACTION_INSERT);
            
            ref = new Pagereference('/' + newOpp.Id);
            ref.setRedirect(true);




@isTest
public class OpportunityOfferUpdaterTest {
    
    @isTest
    static void testEdeliveryOffer() {
        Opportunity opportunity = new Opportunity();
        ClientOffer clientOffer = new ClientOffer();
        clientOffer.Reg_online_access_Edelivery__c = true;
        
        OpportunityOfferUpdater.updateOpportunityWithOffer('edelivery', opportunity, clientOffer);
        
        System.assertEquals(true, opportunity.Offer_File_Registered_for_Online_Access__c);
    }
    
    @isTest
    static void testInccontOffer() {
        Opportunity opportunity = new Opportunity();
        ClientOffer clientOffer = new ClientOffer();
        
        clientOffer.Last_Contrib_Rate_Change_Date_Inccont__c = Date.today();
        clientOffer.Last_Hardship_Withdrawal_Date_Inccont__c = Date.today().addDays(-1);
        clientOffer.Max_Employer_Match_Pct_Inccont__c = 5;
        clientOffer.Partic_Auto_Increase_Inccont__c = true;
        clientOffer.Plan_Auto_Increase_Inccont__c = true;
        clientOffer.Plan_Max_Deferral_Amt_Inccont__c = 10000;
        clientOffer.Plan_Max_Deferral_Pct_Inccont__c = 15;
        clientOffer.PostTax_Deferral_Amt_Inccont__c = 500;
        clientOffer.PostTax_Deferral_Pct_Inccont__c = 5;
        clientOffer.PreTax_Deferral_Amt_Inccont__c = 1000;
        clientOffer.PreTax_Deferral_Pct_Inccont__c = 10;
        clientOffer.Current_Roth_Deferral_Pct_Inccont__c = 8;
        clientOffer.Current_Roth_Deferral_Amt_Inccont__c = 200;

        OpportunityOfferUpdater.updateOpportunityWithOffer('inccont', opportunity, clientOffer);
        
        System.assertEquals(Date.today(), opportunity.Offer_File_Last_Contrib_Rate_Change_Date__c);
        System.assertEquals(Date.today().addDays(-1), opportunity.Offer_File_Last_Hardship_Withdrawal_Date__c);
        System.assertEquals(5, opportunity.Offer_File_Max_Employer_Match_Pct__c);
        System.assertEquals(true, opportunity.Offer_File_Partic_Auto_Increase__c);
        System.assertEquals(true, opportunity.Offer_File_Plan_Offers_Auto_Increase__c);
        System.assertEquals(10000, opportunity.Offer_File_Plan_Max_Deferral_Amount__c);
        System.assertEquals(15, opportunity.Offer_File_Plan_Max_Deferral_Pct__c);
        System.assertEquals(500, opportunity.Offer_File_Current_Post_Tax_Deferral_Amt__c);
        System.assertEquals(5, opportunity.Offer_File_Current_Post_Tax_Deferral_Pct__c);
        System.assertEquals(1000, opportunity.Offer_File_Current_Pre_Tax_Deferral_Amt__c);
        System.assertEquals(10, opportunity.Offer_File_Current_Pre_Tax_Deferral_Pct__c);
        System.assertEquals(8, opportunity.Offer_File_Current_Roth_Deferral_Pct__c);
        System.assertEquals(200, opportunity.Offer_File_Current_Roth_Deferral_Amount__c);
    }
    
    @isTest
    static void testCatchupOffer() {
        Opportunity opportunity = new Opportunity();
        ClientOffer clientOffer = new ClientOffer();
        
        clientOffer.Catch_Up_Amt_Catchup__c = 1000;
        clientOffer.Catch_Up_Pct_Catchup__c = 10;
        clientOffer.Last_Contrib_Rate_Change_Date_Catchup__c = Date.today().addDays(-10);
        clientOffer.Partic_Auto_Increase_Catchup__c = false;
        clientOffer.Plan_Auto_Increase_Catchup__c = false;
        clientOffer.Plan_Max_Deferral_Amt_Catchup__c = 15000;
        clientOffer.Plan_Max_Deferral_Pct_Catchup__c = 20;
        clientOffer.PreTax_Deferral_Amt_Catchup__c










@isTest
public class ClientOfferResponseControllerTest {
    @testSetup
    static void setup() {
        // Create and insert necessary data: Account, Opportunity, Campaign, etc.
        // Insert necessary records...
    }

    @isTest
    static void testClientOfferResponseController() {
        Test.startTest();

        // Retrieve data set in @testSetup
        // Retrieve necessary records...

        // Call the method under test
        ClientOfferResponseController controller = new ClientOfferResponseController();
        controller.handleOfferResponse(
            clientOffer.Id,
            campaign.Id,
            offerPop.Id,
            'response',
            'responseReason',
            'comment',
            plan.Id,
            true,
            'dnisNumber'
        );

        Test.stopTest();

        // Verify the results
        Opportunity updatedOpportunity = [SELECT Id, StageName, Opportunity_Status__c, OwnerId, RR_Campaign__c FROM Opportunity WHERE Id = :opportunity.Id];
        System.assertEquals('Needs Analysis', updatedOpportunity.StageName, 'Opportunity stage name should be updated to "Needs Analysis"');
        System.assertEquals('Closed - No Sale', updatedOpportunity.Opportunity_Status__c, 'Opportunity status should be updated to "Closed - No Sale"');

        Offer_Pop__c updatedOfferPop = [SELECT Opportunity__c, Action__c FROM Offer_Pop__c WHERE Id = :offerPop.Id];
        System.assertEquals(opportunity.Id, updatedOfferPop.Opportunity__c, 'Offer Pop should be associated with the updated Opportunity');
        System.assertEquals(UltimatePopControllerHelper.OFFERPOP_STATUS_RTM, updatedOfferPop.Action__c, 'Offer Pop action should be updated to "Record Targeted Message"');

        Account updatedAccount = [SELECT RR_Eligible__pc, OwnerId FROM Account WHERE Id = :account.Id];
        System.assertEquals(true, updatedAccount.RR_Eligible__pc, 'RR Eligible flag on the Account should be updated to true');
        System.assertEquals(Label.I_ENV_DefaultISTAccountOwner, updatedAccount.OwnerId, 'Account Owner should be updated to the default IST Account Owner');
    }
}















@isTest
static void testCreateOpportunityWithPlanAndCampaign() {
    boolean Manual = true;         

    // Insert Campaign
    Campaign campaign = new Campaign(
        Name = 'Test Campaign',
        Offer_Code__c = 'rr', // Ensure 'rr' to trigger RR logic
        Offer_Priority__c = 1.777,
        Offer_Opportunity_Record_Type_ID__c = '012xxxxxxxxxxxxxxx'
    );
    insert campaign;

    // Insert Plan
    Plan__c plan = new Plan__c(
        Name = 'Test Plan',
        Native_Plan_ID__c = 'TestPlanID'
    );
    insert plan;

    // Insert Account
    Account account = new Account(
        LastName = 'Doe',
        FirstName = 'John',
        SSN__c = '123456789',
        PersonBirthdate = Date.today().addYears(-30),
        PersonMailingStreet = '123 Test St',
        PersonMailingCity = 'Test City',
        PersonMailingState = 'TX',
        PersonMailingPostalCode = '12345',
        PersonEmail = 'john.doe@test.com',
        PersonHomePhone = '555-555-5555',
        Sex__c = 'M',
        OwnerId = UserInfo.getUserId() // Initially set to current user
    );
    insert account;

    // Insert Client Offer
    Client_Offer__c clientOffer = new Client_Offer__c(
        Account_Last_Name__c = 'Doe',
        Account_First_Name__c = 'John',
        Account_Ext_Id__c = '123456789',
        Account_Birthdate__c = Date.today().addYears(-30),
        Account_Address1__c = '123 Test St',
        Account_Address2__c = 'Apt 101',
        Account_City__c = 'Test City',
        Account_State__c = 'TX',
        Account_Zip__c = '12345',
        Account_Email__c = 'john.doe@test.com',
        Account_Phone__c = '555-555-5555',
        Account_Gender__c = 'M',
        Account_Country__c = 'USA',
        PlanId_testcode__c = plan.Id,
        OfferDate_rr__c = Date.today(),
        AccountBalance_rr__c = 100.0
    );
    insert clientOffer;

    // Insert CTI Console Pop
    CTI_Console_Pop__c ctiPop = new CTI_Console_Pop__c(
        Account__c = account.Id,
        CTI_Params__c = 'param1;param2;param3:testDNIS',
        ExternalID__c = '123456789'
    );
    insert ctiPop;

    // Retrieve records for assertions
    Campaign campaignRecord = [SELECT Id, Name FROM Campaign WHERE Name = 'Test Campaign' LIMIT 1];
    Plan__c planRecord = [SELECT Id, Name FROM Plan__c WHERE Name = 'Test Plan' LIMIT 1];
    Account accountRecord = [SELECT Id, LastName, FirstName FROM Account WHERE LastName = 'Doe' LIMIT 1];
    Client_Offer__c clientOfferRecord = [SELECT Id FROM Client_Offer__c LIMIT 1];

    Test.startTest();
    String opportunityName = cTargetedMessage.createOpportunityWithPlanAndCampaign(
        campaignRecord.Name, planRecord.Name, accountRecord.LastName, UserInfo.getUserId(), 'Response', 'Reason', 'Comment', accountRecord.Id, Manual
    );
    Test.stopTest();

    // Verify the results
    Opportunity createdOpportunity = [SELECT Id, Name, AccountId, CampaignId, OwnerId, StageName, Offer_Response_Reason__c FROM Opportunity WHERE Name = :opportunityName LIMIT 1];
    System.assertNotEquals(null, createdOpportunity);
    System.assertEquals(accountRecord.Id, createdOpportunity.AccountId);
    System.assertEquals(campaignRecord.Id, createdOpportunity.CampaignId);
    System.assertEquals('Needs Analysis', createdOpportunity.StageName);

    // Additional Assertions to verify Account field updates
    Account updatedAccount = [SELECT LastName, FirstName, SSN__c, PersonBirthdate, PersonMailingStreet, PersonMailingCity, PersonMailingState, PersonMailingPostalCode, PersonEmail, PersonMailingCountry, PersonHomePhone, Sex__c, RR_Eligible__pc, OwnerId FROM Account WHERE Id = :account.Id];
    System.assertEquals('Doe', updatedAccount.LastName);
    System.assertEquals('John', updatedAccount.FirstName);
    System.assertEquals('123456789', updatedAccount.SSN__c);
    System.assertEquals(clientOffer.Account_Birthdate__c, updatedAccount.PersonBirthdate);
    System.assertEquals('123 Test St Apt 101', updatedAccount.PersonMailingStreet);
    System.assertEquals('Test City', updatedAccount.PersonMailingCity);
    System.assertEquals('TX', updatedAccount.PersonMailingState);
    System.assertEquals('12345', updatedAccount.PersonMailingPostalCode);
    System.assertEquals('john.doe@test.com', updatedAccount.PersonEmail);
    System.assertEquals('USA', updatedAccount.PersonMailingCountry);
    System.assertEquals('555-555-5555', updatedAccount.PersonHomePhone);
    System.assertEquals('M', updatedAccount.Sex__c);
    System.assertEquals(true, updatedAccount.RR_Eligible__pc);
    System.assertEquals(Label.I_ENV_DefaultISTAccountOwner, updatedAccount.OwnerId);

    // Verify Offer Pop
    Offer_Pop__c offerPop = [SELECT Id, Opportunity__c, Action__c, Offers_Available__c, Top_Offer__c, CTI_DNIS_Number__c, Client__c, Campaign__c, Lead_Source__c FROM Offer_Pop__c WHERE Opportunity__c = :createdOpportunity.Id LIMIT 1];
    System.assertNotEquals(null, offerPop);
    System.assertEquals(createdOpportunity.Id, offerPop.Opportunity__c);
    System.assertEquals(UltimatePopControllerHelper.OFFERPOP_STATUS_RTM, offerPop.Action__c);
    System.assertEquals('Yes', offerPop.Offers_Available__c);
    System.assertEquals(campaignRecord.Id, offerPop.Top_Offer__c);
    System.assertEquals('testDNIS', offerPop.CTI_DNIS_Number__c);
    System.assertEquals(account.Id, offerPop.Client__c);
    System.assertNotEquals(null, offerPop.Campaign__c);
    System.assertNotEquals(null, offerPop.Lead_Source__c);

    // Verify Client Offer status update
    Client_Offer__c updatedClientOffer = [SELECT Id, Status_rr__c FROM Client_Offer__c WHERE Id = :clientOffer.Id];
    System.assertEquals('Closed', updatedClientOffer.Status_rr__c);
}

















@isTest
static void testCreateOpportunityWithPlanAndCampaign() {
    boolean Manual = true;         

    // Insert Campaign
    Campaign campaign = new Campaign(
        Name = 'Test Campaign',
        Offer_Code__c = 'rr',
        Offer_Priority__c = 1.777,
        Offer_Opportunity_Record_Type_ID__c = '012xxxxxxxxxxxxxxx'
    );
    insert campaign;

    // Insert Plan
    Plan__c plan = new Plan__c(
        Name = 'Test Plan',
        Native_Plan_ID__c = 'TestPlanID'
    );
    insert plan;

    // Insert Account
    Account account = new Account(
        LastName = 'Doe',
        FirstName = 'John',
        SSN__c = '123456789',
        PersonBirthdate = Date.today().addYears(-30),
        PersonMailingStreet = '123 Test St',
        PersonMailingCity = 'Test City',
        PersonMailingState = 'TX',
        PersonMailingPostalCode = '12345',
        PersonEmail = 'john.doe@test.com',
        PersonHomePhone = '555-555-5555',
        Sex__c = 'M',
        OwnerId = UserInfo.getUserId() // Initially set to current user
    );
    insert account;

    // Insert Client Offer
    Client_Offer__c clientOffer = new Client_Offer__c(
        Account_Last_Name__c = 'Doe',
        Account_First_Name__c = 'John',
        Account_Ext_Id__c = '123456789',
        Account_Birthdate__c = Date.today().addYears(-30),
        Account_Address1__c = '123 Test St',
        Account_Address2__c = 'Apt 101',
        Account_City__c = 'Test City',
        Account_State__c = 'TX',
        Account_Zip__c = '12345',
        Account_Email__c = 'john.doe@test.com',
        Account_Phone__c = '555-555-5555',
        Account_Gender__c = 'M',
        Account_Country__c = 'USA',
        PlanId_testcode__c = plan.Id,
        OfferDate_rr__c = Date.today(),
        AccountBalance_rr__c = 100.0
    );
    insert clientOffer;

    // Insert CTI Console Pop
    CTI_Console_Pop__c ctiPop = new CTI_Console_Pop__c(
        Account__c = account.Id,
        CTI_Params__c = 'param1;param2;param3:testDNIS',
        ExternalID__c = '123456789'
    );
    insert ctiPop;

    // Retrieve records for assertions
    Campaign campaignRecord = [SELECT Id, Name FROM Campaign WHERE Name = 'Test Campaign' LIMIT 1];
    Plan__c planRecord = [SELECT Id, Name FROM Plan__c WHERE Name = 'Test Plan' LIMIT 1];
    Account accountRecord = [SELECT Id, LastName, FirstName FROM Account WHERE LastName = 'Doe' LIMIT 1];
    Client_Offer__c clientOfferRecord = [SELECT Id FROM Client_Offer__c LIMIT 1];

    Test.startTest();
    String opportunityName = cTargetedMessage.createOpportunityWithPlanAndCampaign(
        campaignRecord.Name, planRecord.Name, accountRecord.LastName, UserInfo.getUserId(), 'Response', 'Reason', 'Comment', accountRecord.Id, Manual
    );
    Test.stopTest();

    // Verify the results
    Opportunity createdOpportunity = [SELECT Id, Name, AccountId, CampaignId, OwnerId, StageName, Offer_Response_Reason__c FROM Opportunity WHERE Name = :opportunityName LIMIT 1];
    System.assertNotEquals(null, createdOpportunity);
    System.assertEquals(accountRecord.Id, createdOpportunity.AccountId);
    System.assertEquals(campaignRecord.Id, createdOpportunity.CampaignId);
    System.assertEquals('Needs Analysis', createdOpportunity.StageName);

    // Additional Assertions to verify Account field updates
    Account updatedAccount = [SELECT LastName, FirstName, SSN__c, PersonBirthdate, PersonMailingStreet, PersonMailingCity, PersonMailingState, PersonMailingPostalCode, PersonEmail, PersonMailingCountry, PersonHomePhone, Sex__c, RR_Eligible__pc, OwnerId FROM Account WHERE Id = :account.Id];
    System.assertEquals('Doe', updatedAccount.LastName);
    System.assertEquals('John', updatedAccount.FirstName);
    System.assertEquals('123456789', updatedAccount.SSN__c);
    System.assertEquals(clientOffer.Account_Birthdate__c, updatedAccount.PersonBirthdate);
    System.assertEquals('123 Test St Apt 101', updatedAccount.PersonMailingStreet);
    System.assertEquals('Test City', updatedAccount.PersonMailingCity);
    System.assertEquals('TX', updatedAccount.PersonMailingState);
    System.assertEquals('12345', updatedAccount.PersonMailingPostalCode);
    System.assertEquals('john.doe@test.com', updatedAccount.PersonEmail);
    System.assertEquals('USA', updatedAccount.PersonMailingCountry);
    System.assertEquals('555-555-5555', updatedAccount.PersonHomePhone);
    System.assertEquals('M', updatedAccount.Sex__c);
    System.assertEquals(true, updatedAccount.RR_Eligible__pc);
    System.assertEquals(Label.I_ENV_DefaultISTAccountOwner, updatedAccount.OwnerId);

    // Verify Offer Pop
    Offer_Pop__c offerPop = [SELECT Id, Opportunity__c, Action__c, Offers_Available__c, Top_Offer__c, CTI_DNIS_Number__c, Client__c, Campaign__c, Lead_Source__c FROM Offer_Pop__c WHERE Opportunity__c = :createdOpportunity.Id LIMIT 1];
    System.assertNotEquals(null, offerPop);
    System.assertEquals(createdOpportunity.Id, offerPop.Opportunity__c);
    System.assertEquals(UltimatePopControllerHelper.OFFERPOP_STATUS_RTM, offerPop.Action__c);
    System.assertEquals('Yes', offerPop.Offers_Available__c);
    System.assertEquals(campaignRecord.Id, offerPop.Top_Offer__c);
    System.assertEquals('testDNIS', offerPop.CTI_DNIS_Number__c);
    System.assertEquals(account.Id, offerPop.Client__c);
    System.assertNotEquals(null, offerPop.Campaign__c);
    System.assertNotEquals(null, offerPop.Lead_Source__c);

    // Verify Client Offer status update
    Client_Offer__c updatedClientOffer = [SELECT Id, Status_rr__c FROM Client_Offer__c WHERE Id = :clientOffer.Id];
    System.assertEquals('Closed', updatedClientOffer.Status_rr__c);
}














@isTest
static void testCreateOpportunityWithPlanAndCampaign() {
    boolean Manual = true;         

    // Insert Campaign
    Campaign campaign = new Campaign(
        Name = 'Test Campaign',
        Offer_Code__c = 'testcode',
        Offer_Priority__c = 1.777,
        Offer_Opportunity_Record_Type_ID__c = '012xxxxxxxxxxxxxxx'
    );
    insert campaign;

    // Insert Plan
    Plan__c plan = new Plan__c(
        Name = 'Test Plan',
        Native_Plan_ID__c = 'TestPlanID'
    );
    insert plan;

    // Insert Account
    Account account = new Account(
        LastName = 'Doe',
        FirstName = 'John',
        SSN__c = '123456789',
        PersonBirthdate = Date.today().addYears(-30),
        PersonMailingStreet = '123 Test St',
        PersonMailingCity = 'Test City',
        PersonMailingState = 'TX',
        PersonMailingPostalCode = '12345',
        PersonEmail = 'john.doe@test.com',
        PersonHomePhone = '555-555-5555',
        Sex__c = 'M'
    );
    insert account;

    // Insert Client Offer
    Client_Offer__c clientOffer = new Client_Offer__c(
        Account_Last_Name__c = 'Doe',
        Account_First_Name__c = 'John',
        Account_Ext_Id__c = '123456789',
        Account_Birthdate__c = Date.today().addYears(-30),
        Account_Address1__c = '123 Test St',
        Account_Address2__c = 'Apt 101',
        Account_City__c = 'Test City',
        Account_State__c = 'TX',
        Account_Zip__c = '12345',
        Account_Email__c = 'john.doe@test.com',
        Account_Phone__c = '555-555-5555',
        Account_Gender__c = 'M',
        Account_Country__c = 'USA',
        PlanId_testcode__c = plan.Id // Ensure the relationship is set correctly
    );
    insert clientOffer;

    // Insert CTI Console Pop
    CTI_Console_Pop__c ctiPop = new CTI_Console_Pop__c(
        Account__c = account.Id,
        CTI_Params__c = 'param1;param2;param3:testDNIS',
        ExternalID__c = '123456789'
    );
    insert ctiPop;

    // Retrieve records for assertions
    Campaign campaignRecord = [SELECT Id, Name FROM Campaign WHERE Name = 'Test Campaign' LIMIT 1];
    Plan__c planRecord = [SELECT Id, Name FROM Plan__c WHERE Name = 'Test Plan' LIMIT 1];
    Account accountRecord = [SELECT Id, LastName, FirstName FROM Account WHERE LastName = 'Doe' LIMIT 1];
    Client_Offer__c clientOfferRecord = [SELECT Id FROM Client_Offer__c LIMIT 1];

    Test.startTest();
    String opportunityName = cTargetedMessage.createOpportunityWithPlanAndCampaign(
        campaignRecord.Name, planRecord.Name, accountRecord.LastName, UserInfo.getUserId(), 'Response', 'Reason', 'Comment', accountRecord.Id, Manual
    );
    Test.stopTest();

    // Verify the results
    Opportunity createdOpportunity = [SELECT Id, Name, AccountId, CampaignId, OwnerId, StageName FROM Opportunity WHERE Name = :opportunityName LIMIT 1];
    System.assertNotEquals(null, createdOpportunity);
    System.assertEquals(accountRecord.Id, createdOpportunity.AccountId);
    System.assertEquals(campaignRecord.Id, createdOpportunity.CampaignId);
    System.assertEquals('Needs Analysis', createdOpportunity.StageName);

    // Additional Assertions to verify Account field updates
    Account updatedAccount = [SELECT LastName, FirstName, SSN__c, PersonBirthdate, PersonMailingStreet, PersonMailingCity, PersonMailingState, PersonMailingPostalCode, PersonEmail, PersonMailingCountry, PersonHomePhone, Sex__c FROM Account WHERE Id = :account.Id];
    System.assertEquals('Doe', updatedAccount.LastName);
    System.assertEquals('John', updatedAccount.FirstName);
    System.assertEquals('123456789', updatedAccount.SSN__c);
    System.assertEquals(clientOffer.Account_Birthdate__c, updatedAccount.PersonBirthdate);
    System.assertEquals('123 Test St Apt 101', updatedAccount.PersonMailingStreet);
    System.assertEquals('Test City', updatedAccount.PersonMailingCity);
    System.assertEquals('TX', updatedAccount.PersonMailingState);
    System.assertEquals('12345', updatedAccount.PersonMailingPostalCode);
    System.assertEquals('john.doe@test.com', updatedAccount.PersonEmail);
    System.assertEquals('USA', updatedAccount.PersonMailingCountry);
    System.assertEquals('555-555-5555', updatedAccount.PersonHomePhone);
    System.assertEquals('M', updatedAccount.Sex__c);
}












if (clientOffer.account_last_name__c != null) {
                    account.lastname = I_AIFUtils.properCaseConvert((clientOffer.account_last_name__c.length() > 80) ? clientOffer.account_last_name__c.substring(0, 80) : clientOffer.account_last_name__c);
                }
                if (clientOffer.account_first_name__c != null) {
                    account.firstname = I_AIFUtils.properCaseConvert((clientOffer.account_first_name__c.length() > 40) ? clientOffer.account_first_name__c.substring(0, 40) : clientOffer.account_first_name__c);
                }
                account.ssn__c = clientOffer.account_ext_id__c;
                account.PersonBirthdate = clientOffer.Account_Birthdate__c;
                account.PersonMailingStreet = I_AIFUtils.properCaseConvert(clientOffer.Account_Address1__c);
                if (clientOffer.Account_Address2__c != null) {
                    account.PersonMailingStreet += I_AIFUtils.properCaseConvert(' ' + clientOffer.Account_Address2__c);   
                }  
                account.PersonMailingCity = I_AIFUtils.properCaseConvert(clientOffer.Account_City__c);
                account.PersonMailingState = I_AIFUtils.properCaseConvert(clientOffer.Account_State__c);
                account.PersonMailingPostalCode = clientOffer.Account_Zip__c;
                account.PersonEmail = clientOffer.Account_Email__c; 
                account.PersonMailingCountry = clientOffer.Account_Country__c;
                account.PersonHomePhone = (account.PersonHomePhone == null) ? clientOffer.Account_Phone__c : account.PersonHomePhone;
                account.Sex__c = clientOffer.Account_Gender__c;
                account.OwnerId = Label.I_ENV_DefaultISTAccountOwner;
                
                
                //Code of first name splitting. 
                I_ConversionUtility cu = new I_ConversionUtility();
                account = cu.firstNameConversionSingleRecord(account);
                
                insert(account); 






opportunity.Offer_File_Hardship_Suspension_End_Date__c = clientOffer.Hardship_Suspension_Ends_Hardship__c;
                opportunity.Offer_File_Last_Hardship_Withdrawal_Date__c = clientOffer.Last_Hardship_Withdrawal_Date_Hardship__c;
                
                if(campaign.name == 'Retirement Readiness') {
                    opportunity.Name = account.lastname + ' - Planning';
                    opportunity.Type = 'Retirement Snapshot';
                } else if(campaign.name == 'Retirement Readiness -  Managed Account Eligible') {
                    opportunity.Name = account.lastname + ' - Planning -  Managed Account Eligible';
                } else {
                    opportunity.Name = account.lastname + ' - ' + campaign.name;
                }
                opportunity.at_Risk__c = (Decimal)clientOffer.get('accountbalance_' + campaign.offer_code__c + '__c');
                opportunity.StageName = 'Needs Analysis';
                opportunity.Opportunity_Status__c = 'Closed - No Sale';
                
                //Added as Part of RS Data feed - Requested by Melissa Deane.
                opportunity.Offer_Plan_Number__c = (String)clientOffer.get('PlanId_' + campaign.offer_code__c + '__c');
                
                //Case # 00011325 : to save value in the ?Active Mailer? will to the Opportunity field ?Offer Active Mailer? 
                //which will allow the business to report when a Targeted Message has been pitched and if there was an Active Mailer.
                opportunity.Offer_Active_Mailer__c = (String)clientOffer.get('Active_Mailer_' + campaign.offer_code__c + '__c');
                
                String agentTin = (String)clientOffer.get('AgentId_' + campaign.offer_code__c + '__c');
                if (agentTin!=null && agentTin.length()==9) {
                    opportunity.AgentTIN__c = (String)clientOffer.get('AgentId_' + campaign.offer_code__c + '__c');
                    
                    // Ultimate ROE Requirements - 07/17/2012
                    RecordType producerRecType = [Select Id From RecordType Where sObjectType = 'Account' and Name = 'Producer' limit 1];
                    List<Account> producerObjList = [select Id from Account where Producer_SSN__c = :agentTin and RecordTypeId = :producerRecType.Id];
                    if(producerObjList != null && producerObjList.size() > 0)
                        opportunity.Agent_Name__c = producerObjList[0].Id;
                }
                
                
                for (Offer_Opportuntiy_Status_Rule__c rule : [Select Opportunity_Stagename__c, Opportunity_Status__c, Opportunity_Sales_Outcome__c, Opportunity_Outcome_Reason__c from Offer_Opportuntiy_Status_Rule__c where offer_code__c = :campaign.offer_code__c and Opportunity_Offer_Response__c = :opportunity.Offer_Response__c and (Opportunity_Offer_Response_Reason__c = :opportunity.Offer_Response_Reason__c or Opportunity_Offer_Response_Reason__c = null)  limit 1]) {
                  //  system.debug('rule'+rule);
                    opportunity.StageName = rule.Opportunity_Stagename__c;
                    opportunity.Opportunity_status__c = rule.opportunity_status__c;
                    
                    //Rahul Sahay - Added for CTI Console Pop (06/16/2014)
                    opportunity.Sales_Outcome__c = rule.Opportunity_Sales_Outcome__c;
                    opportunity.Outcome_Reason__c = rule.Opportunity_Outcome_Reason__c;
                } 
                opportunity.Offer_Response__c=response;
                opportunity.Offer_Response_Reason__c=responseReason;
                opportunity.Message_Comments__c=comment;
                
                opportunity.LeadSource = 'Participant Offer';//TO-DO Task - UltimatePopController.doLeadSourceTranslation();
                opportunity.closeDate = Date.today();
                opportunity.CampaignId = campaign.id;
                opportunity.AccountId = account.id;
               /* for (Plan__c p1 : [Select Native_Plan_ID__c, id from Plan__c where Native_Plan_ID__c = :(String)clientOffer.get('planid_' + campaign.offer_code__c + '__c') and native_Plan_ID__c != null limit 1]) {
                    opportunity.plan__c = p1.id;
                    offerPlan = p1;
                }
                if (opportunity.plan__c == null) {
                    
                    for (Plan__c p2 : [Select Native_Plan_ID__c, id from Plan__c where Native_Plan_ID__c = 'None' limit 1]) {
                        opportunity.plan__c = p2.id;
                    }
                }*/
                opportunity.plan__c = p.id;
                 offerPlan = p;
                
            /*  inserting and then change the owner (if needed) in update below.
            */
                String temp = opportunity.OwnerId;
                // if(opportunity.OwnerId == null)
                opportunity.OwnerId = UserInfo.getUserId(); //keep the ownerId as logged in user
                
                if (offerPop != null) {
                    opportunity.Offer_Pop__c = offerPop.id;
                }
                
   				//create manual
   				system.debug('Manual'+Manual);
   				if(Manual==true)
                opportunity.Offer_Created_Manually__c = true;
                
                else
                 opportunity.Offer_Created_Manually__c = false;
                //Code added for RS Data feed functionality.
                if(offerCode == 'edelivery'){
                
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Edelivery__c;
                    
                }else if(offerCode == 'inccont'){
                    
                    opportunity.Offer_File_Last_Contrib_Rate_Change_Date__c = clientOffer.Last_Contrib_Rate_Change_Date_Inccont__c;
                    opportunity.Offer_File_Last_Hardship_Withdrawal_Date__c = clientOffer.Last_Hardship_Withdrawal_Date_Inccont__c;     
                    opportunity.Offer_File_Max_Employer_Match_Pct__c = clientOffer.Max_Employer_Match_Pct_Inccont__c;   
                    opportunity.Offer_File_Partic_Auto_Increase__c = clientOffer.Partic_Auto_Increase_Inccont__c;   
                    opportunity.Offer_File_Plan_Offers_Auto_Increase__c = clientOffer.Plan_Auto_Increase_Inccont__c;        
                    opportunity.Offer_File_Plan_Max_Deferral_Amount__c = clientOffer.Plan_Max_Deferral_Amt_Inccont__c;      
                    opportunity.Offer_File_Plan_Max_Deferral_Pct__c = clientOffer.Plan_Max_Deferral_Pct_Inccont__c;             
                    opportunity.Offer_File_Current_Post_Tax_Deferral_Amt__c = clientOffer.PostTax_Deferral_Amt_Inccont__c;              
                    opportunity.Offer_File_Current_Post_Tax_Deferral_Pct__c = clientOffer.PostTax_Deferral_Pct_Inccont__c;  
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Amt__c = clientOffer.PreTax_Deferral_Amt_Inccont__c;    
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Pct__c = clientOffer.PreTax_Deferral_Pct_Inccont__c;
                    
                    //Code added for RS Data feed functionality: case # 00008723.
                    opportunity.Offer_File_Current_Roth_Deferral_Pct__c = clientOffer.Current_Roth_Deferral_Pct_Inccont__c;
                    opportunity.Offer_File_Current_Roth_Deferral_Amount__c = clientOffer.Current_Roth_Deferral_Amt_Inccont__c;
                    
                }else if(offerCode == 'catchup'){
                    
                    opportunity.Offer_File_Catch_Up_Amt__c = clientOffer.Catch_Up_Amt_Catchup__c;                           
                    opportunity.Offer_File_Catch_Up_Pct__c = clientOffer.Catch_Up_Pct_Catchup__c;                           
                    opportunity.Offer_File_Last_Contrib_Rate_Change_Date__c = clientOffer.Last_Contrib_Rate_Change_Date_Catchup__c; 
                    opportunity.Offer_File_Partic_Auto_Increase__c = clientOffer.Partic_Auto_Increase_Catchup__c;                                   
                    opportunity.Offer_File_Plan_Offers_Auto_Increase__c = clientOffer.Plan_Auto_Increase_Catchup__c;                            
                    opportunity.Offer_File_Plan_Max_Deferral_Amount__c = clientOffer.Plan_Max_Deferral_Amt_Catchup__c;                              
                    opportunity.Offer_File_Plan_Max_Deferral_Pct__c = clientOffer.Plan_Max_Deferral_Pct_Catchup__c;                                     
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Amt__c = clientOffer.PreTax_Deferral_Amt_Catchup__c;                                
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Pct__c = clientOffer.PreTax_Deferral_Pct_Catchup__c;
                    
                    //Code added for RS Data feed functionality: case # 00008723.
                    opportunity.Offer_File_Current_Roth_Deferral_Pct__c = clientOffer.Current_Roth_Deferral_Pct_Catchup__c;
                    opportunity.Offer_File_Current_Roth_Deferral_Amount__c = clientOffer.Current_Roth_Deferral_Amt_Catchup__c;
                    opportunity.Offer_CurrentCatchUp_RothDeferral_Pct__c = clientOffer.Cur_Catch_Up_Roth_Deferral_Pct_Catchup__c;
                    opportunity.Offer_CurrentCatchUp_RothDeferral_Amt__c = clientOffer.Cur_Catch_Up_Roth_Deferral_Amt_Catchup__c;
                    
                }else if(offerCode == 'manacct'){
                    
                    opportunity.Offer_File_Manage_Acct_Plan_Offers__c = clientOffer.Manage_Acct_Plan_Offered_Manacct__c;
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Manacct__c;  
                    
                }else if(offerCode == 'manactips' || offerCode == 'manactadv'){
                    
                    opportunity.Offer_File_Manage_Acct_Plan_Offers__c = clientOffer.Manage_Acct_Plan_Offered_Manactips__c;      
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Manactips__c;
                    
                }else if(offerCode == 'rr' || offerCode == 'rrma'){//Case # 00011254/00011255:For Retirement Readiness Offer
                    
                    system.debug('=================================== offerCode ' + offerCode);
                    
                    Boolean isRRMarketingNumber = false;
                    
                    if(offerPop != null && offerPop.CTI_DNIS_Number__c != null) {
                        List<Rule__c> ruleList = [Select Value_ist__c, Name, Rule_Group_ist__c, Description_ist__c From Rule__c where Name =:offerPop.CTI_DNIS_Number__c and Rule_Group_ist__c = 'Campaign-Lead-Source-Translation'];
                        if(ruleList != null && ruleList.size() > 0) {
                            for(Rule__c rule : ruleList) {
                                if(rule.Description_ist__c != null && rule.Description_ist__c.contains(UltimatePopControllerHelper.CAMPAIGN_LEAD_SOURCE_TYPE_RR)) {
                                    isRRMarketingNumber = true;
                                    break;
                                }
                                else
                                    isRRMarketingNumber = false;
                            }    
                        }
                        
                        
                        //opportunity.LeadSource = UltimatePopController.doLeadSourceTranslation(offerPop.CTI_DNIS_Number__c);
                        // Rahul Sahay - June 2013 Rel - Remove this functionality when call is coming from Retirement Readiness service numbers
                        if(!isRRMarketingNumber)
                            opportunity.RR_Campaign__c = CTIPopController.getCampaignInfo(offerPop.CTI_DNIS_Number__c, account);
                    }
                    
                    if(!isRRMarketingNumber && opportunity.RR_Campaign__c == null) {
                        //call client profile helper class to get the RR campaign info for this client
                        List<Account> clientList = [Select Id, Name, PersonContactId from Account Where Id =: account.Id];
                        if(clientList.size() > 0 && clientList[0].PersonContactId != null) {
                            
                            List<Selectoption> rrCampaigns = ClientProfileHelper.getRRCampaigns(clientList[0].PersonContactId, false);
                            if(rrCampaigns.size() > 0)
                                opportunity.RR_Campaign__c = rrCampaigns[0].getValue();
                        }
                    }
                    
                    system.debug('=================================== isRRMarketingNumber ' + isRRMarketingNumber);
                    system.debug('=================================== opportunity.RR_Campaign__c ' + opportunity.RR_Campaign__c);
                    
                }
                
                insert(opportunity);
                
                
                try {
                   
                    // Use temp Opportunity to update as after insert the value for Stage name is changed by trigger and again while updating the Opportunity 
                    // the trigger tries to update the Stage name again. To prevent this, use the old state of Opportunity to use the same values as of insert
                    
                    if(opportunity != null && opportunity.Id != null && opportunity.OwnerId != temp) {
                        
                        system.debug('Previous Owner : ' + opportunity.OwnerId);
                        system.debug('New Owner : ' + temp);
                        
                        Opportunity tempOpp = new Opportunity();
                        tempOpp.Id = opportunity.Id;
                        tempOpp.OwnerId = temp;
                        
                        
                        update(tempOpp);
                    }
                }
                catch(Exception e) {
                    system.debug('Error while changing the OwnerId for the Opportunity due to : ' + e);
                }
                
                //Update RR eligible flag on the Client if Client is RR eligible and call the RR Batch process to create the
                //Financial account and financial account team for the client
                if((offerCode == 'rr' || offerCode == 'rrma') && offerPlan != null){
                    if (account.RR_Eligible__pc==false || account.RR_Eligible__pc==null || account.OwnerId!=Label.I_ENV_DefaultISTAccountOwner){
                        account.RR_Eligible__pc = true;
                        account.OwnerId=Label.I_ENV_DefaultISTAccountOwner;
                        update account;
                    }
                    RRBatchProcessHelper.ClientWrapper clientW = new RRBatchProcessHelper.ClientWrapper(account, (Double)clientOffer.get('accountbalance_' + offerCode + '__c'), (Date)clientOffer.get('OfferDate_' + offerCode+ '__c'));
                    RRBatchProcessHelper.executeRRBatchForClient(clientW, offerPlan);
                    //Call RR Agent Permission Helper to give access to RR Producers to Client and Financial Clients create for the above client offers.
                    RRAgentPermissionBatchProcessHelper.acessAssignmentForRRProducerByPlanIds(new Set<String> {offerPlan.Native_Plan_ID__c}, new Set<String> {account.SSN__c});
                }
                
            }       
            
            // When a user is navigated to the offer pop page and records a targeted message add 
            // the text of "Record Targeted Message" and associate the opportunity to the offer pop.
            if(offerPop != null) {
                offerPop.Opportunity__c = opportunity.Id;
                offerPop.Action__c = UltimatePopControllerHelper.OFFERPOP_STATUS_RTM;
                update offerPop;
            }
            
              
            // calling recordCaseAction    *******************************             
            recordCaseAction(opportunity,cp.Case__c, account.SSN__c);
            // END recordCaseAction  ***************************
            
            //updateofferpop starts********************************
            
            
            if(offerPop == null)
                offerPop = new Offer_Pop__c(); 
            
            
            offerPop.OfferPop_Transaction_ID__c = account.SSN__c + ConstantUtils.UNIQUE_SEPERATOR + UserInfo.getSessionId()+campaign.id;
            offerPop.Client_ID__c = account.SSN__c;
            offerPop.User__c = Userinfo.getUserId();
            
            
            if(campaign != null) {
                offerPop.Offers_Available__c = 'Yes';
                offerPop.Top_Offer__c = campaign.id;
            }
            else {
                offerPop.Offers_Available__c = 'No';
            }
            
            
            offerPop.CTI_DNIS_Number__c = dnisNumber;
            offerPop.Client__c = account.Id;
            offerPop.Campaign__c = CTIPopController.getCampaignInfo(dnisNumber, account); 
            offerPop.Lead_Source__c = CTIPopController.doLeadSourceTranslation(dnisNumber);
            
            
            //offerPop.Source__c = UltimatePopControllerHelper.SOURCE_CTI;
            offerPop.Source__c = UltimatePopControllerHelper.SOURCE_CTI;
           
            
            offerPop.Action__c = UltimatePopControllerHelper.OFFERPOP_STATUS_RTM;
            offerPop.Opportunity__c = opportunity.Id;
            
            system.debug('><<><><><><><><><><<><><><><><><><><><><><> offerPop : ' + offerPop);
            
            if(offerPop.Id == null)
                DataBase.insert(offerPop);
            else
                DataBase.update(offerPop); 
            
            // offerpop ends********************************8
            
          
            if (opportunity.id != null && (opportunity.offer_response_reason__c != 'Ask me again next time' && opportunity.offer_response_reason__c != 'Misdirected Call' && opportunity.offer_response_reason__c != 'Security Not Validated/Other Caller' && opportunity.offer_response_reason__c != 'Escalated Call' && opportunity.offer_response_reason__c != 'Does Not Meet Criteria' && opportunity.offer_response_reason__c != 'Send email' && opportunity.offer_response_reason__c != 'Send me materials')) {
                clientOffer.put('status_' + campaign.offer_code__c + '__c','Closed');
                update(clientOffer);











@AuraEnabled
    public static String createOpportunityWithPlanAndCampaign(String messageName, String planId, String clientLastName, string ownerId,string response, string responseReason, string comment, string Clientid,boolean Manual) 
    { 
        System.debug('messageName'+ messageName);
        System.debug('ClientidClientid'+ Clientid);
        System.debug('planId'+ planId);
        Client_Offer__c clientOffer ;
        opportunity opportunity = new opportunity();
        Campaign campaign = [select id, name, offer_code__c, offer_priority__c,Offer_Opportunity_Record_Type_ID__c from campaign where Name=:messageName  Limit 1];
        
        string offerCode = campaign.offer_code__c;        
        
        Plan__c p=(Plan__c)[Select Id,Name,Native_Plan_ID__c from Plan__c where Name=:planId Limit 1];
        
        Account account = [SELECT Id, lastname,firstname,ssn__c FROM Account  WHERE Id = :Clientid];
        Offer_Pop__c  offerPop;
        
        CTI_Console_Pop__c cp= [select Id, Case__c,CTI_Params__c from CTI_Console_Pop__c where account__c = :clientId  order by LastModifiedDate desc  limit 1];
        String dnisNumber;
        String ctiParams = cp.CTI_Params__c;
        if(ctiParams != null && ctiParams != '') {
            String[] ctiParamArr = ctiParams.split(';');            
            dnisNumber = (ctiParamArr[2].split(':').size() == 2 ? ctiParamArr[2].split(':')[1] : ' ');
        }
        
        if(offerPop == null) {
            String externalId = (UserInfo.getUserId() + ConstantUtils.UNIQUE_SEPERATOR + account.ssn__c);
            List<CTI_Console_Pop__c> ctiConsolePopList = [select Id, Offer_Pop__c, Offer_Pop__r.Id from CTI_Console_Pop__c where ExternalID__c = :externalId];
            
            if(ctiConsolePopList != null && ctiConsolePopList.size() > 0) {
                if(ctiConsolePopList[0].Offer_Pop__c != null) {
                    offerPop = [select id, CTI_DNIS_Number__c from offer_pop__c where id = :ctiConsolePopList[0].Offer_Pop__r.Id limit 1];
                    // system.debug('offerPop'+offerPop);
                }
            }
        }
        
       
        
        Savepoint sp = Database.setSavepoint();
        
        String lastModifiedDate;
        
        List<RecordType> mAIpsTypeList = [Select Id from RecordType where (Name ='Managed Accounts-IPS' AND SobjectType ='Opportunity')];
       
        
        List<Client_Offer__c> co =  dynamicClientOfferQuery(new Set<String>{account.ssn__c});
        if(co != null && co.size() > 0)
            clientOffer = co[0];
      
        
        if(clientOffer != null)
            lastModifiedDate = String.valueOf((clientOffer.LastModifiedDate).format('MM/dd/yyyy'));
        
        try {
            if (account.id == null) {
                
                if (clientOffer.account_last_name__c != null) {
                    account.lastname = I_AIFUtils.properCaseConvert((clientOffer.account_last_name__c.length() > 80) ? clientOffer.account_last_name__c.substring(0, 80) : clientOffer.account_last_name__c);
                }
                if (clientOffer.account_first_name__c != null) {
                    account.firstname = I_AIFUtils.properCaseConvert((clientOffer.account_first_name__c.length() > 40) ? clientOffer.account_first_name__c.substring(0, 40) : clientOffer.account_first_name__c);
                }
                account.ssn__c = clientOffer.account_ext_id__c;
                account.PersonBirthdate = clientOffer.Account_Birthdate__c;
                account.PersonMailingStreet = I_AIFUtils.properCaseConvert(clientOffer.Account_Address1__c);
                if (clientOffer.Account_Address2__c != null) {
                    account.PersonMailingStreet += I_AIFUtils.properCaseConvert(' ' + clientOffer.Account_Address2__c);   
                }  
                account.PersonMailingCity = I_AIFUtils.properCaseConvert(clientOffer.Account_City__c);
                account.PersonMailingState = I_AIFUtils.properCaseConvert(clientOffer.Account_State__c);
                account.PersonMailingPostalCode = clientOffer.Account_Zip__c;
                account.PersonEmail = clientOffer.Account_Email__c; 
                account.PersonMailingCountry = clientOffer.Account_Country__c;
                account.PersonHomePhone = (account.PersonHomePhone == null) ? clientOffer.Account_Phone__c : account.PersonHomePhone;
                account.Sex__c = clientOffer.Account_Gender__c;
                account.OwnerId = Label.I_ENV_DefaultISTAccountOwner;
                
                
               
                I_ConversionUtility cu = new I_ConversionUtility();
                account = cu.firstNameConversionSingleRecord(account);
                
                insert(account); 
            } else {
                update(account); 
            }
            
            
            
            if (account.id != null) {
                
                
                Plan__c offerPlan = null;
                
                
               
                opportunity.RecordTypeId = (offerCode == 'rrma' && mAIpsTypeList != null && mAIpsTypeList.size() > 0 ? mAIpsTypeList[0].Id : campaign.Offer_Opportunity_Record_Type_ID__c);
                
               
                
                
                opportunity.Offer_File_Hardship_Suspension_End_Date__c = clientOffer.Hardship_Suspension_Ends_Hardship__c;
                opportunity.Offer_File_Last_Hardship_Withdrawal_Date__c = clientOffer.Last_Hardship_Withdrawal_Date_Hardship__c;
                
                if(campaign.name == 'Retirement Readiness') {
                    opportunity.Name = account.lastname + ' - Planning';
                    opportunity.Type = 'Retirement Snapshot';
                } else if(campaign.name == 'Retirement Readiness -  Managed Account Eligible') {
                    opportunity.Name = account.lastname + ' - Planning -  Managed Account Eligible';
                } else {
                    opportunity.Name = account.lastname + ' - ' + campaign.name;
                }
                opportunity.at_Risk__c = (Decimal)clientOffer.get('accountbalance_' + campaign.offer_code__c + '__c');
                opportunity.StageName = 'Needs Analysis';
                opportunity.Opportunity_Status__c = 'Closed - No Sale';
                
                
                opportunity.Offer_Plan_Number__c = (String)clientOffer.get('PlanId_' + campaign.offer_code__c + '__c');
                
              
                opportunity.Offer_Active_Mailer__c = (String)clientOffer.get('Active_Mailer_' + campaign.offer_code__c + '__c');
                
                String agentTin = (String)clientOffer.get('AgentId_' + campaign.offer_code__c + '__c');
                if (agentTin!=null && agentTin.length()==9) {
                    opportunity.AgentTIN__c = (String)clientOffer.get('AgentId_' + campaign.offer_code__c + '__c');
                    
                   
                    RecordType producerRecType = [Select Id From RecordType Where sObjectType = 'Account' and Name = 'Producer' limit 1];
                    List<Account> producerObjList = [select Id from Account where Producer_SSN__c = :agentTin and RecordTypeId = :producerRecType.Id];
                    if(producerObjList != null && producerObjList.size() > 0)
                        opportunity.Agent_Name__c = producerObjList[0].Id;
                }
                
                
                for (Offer_Opportuntiy_Status_Rule__c rule : [Select Opportunity_Stagename__c, Opportunity_Status__c, Opportunity_Sales_Outcome__c, Opportunity_Outcome_Reason__c from Offer_Opportuntiy_Status_Rule__c where offer_code__c = :campaign.offer_code__c and Opportunity_Offer_Response__c = :opportunity.Offer_Response__c and (Opportunity_Offer_Response_Reason__c = :opportunity.Offer_Response_Reason__c or Opportunity_Offer_Response_Reason__c = null)  limit 1]) {
               
                    opportunity.StageName = rule.Opportunity_Stagename__c;
                    opportunity.Opportunity_status__c = rule.opportunity_status__c;
                    
                   
                    opportunity.Sales_Outcome__c = rule.Opportunity_Sales_Outcome__c;
                    opportunity.Outcome_Reason__c = rule.Opportunity_Outcome_Reason__c;
                } 
                opportunity.Offer_Response__c=response;
                opportunity.Offer_Response_Reason__c=responseReason;
                opportunity.Message_Comments__c=comment;
                
                opportunity.LeadSource = 'Participant Offer';//TO-DO Task - UltimatePopController.doLeadSourceTranslation();
                opportunity.closeDate = Date.today();
                opportunity.CampaignId = campaign.id;
                opportunity.AccountId = account.id;
               
                opportunity.plan__c = p.id;
                 offerPlan = p;
                
           
                String temp = opportunity.OwnerId;
                // if(opportunity.OwnerId == null)
                opportunity.OwnerId = UserInfo.getUserId(); //keep the ownerId as logged in user
                
                if (offerPop != null) {
                    opportunity.Offer_Pop__c = offerPop.id;
                }
                
   				
   				system.debug('Manual'+Manual);
   				if(Manual==true)
                opportunity.Offer_Created_Manually__c = true;
                
                else
                 opportunity.Offer_Created_Manually__c = false;
               
                if(offerCode == 'edelivery'){
                
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Edelivery__c;
                    
                }else if(offerCode == 'inccont'){
                    
                    opportunity.Offer_File_Last_Contrib_Rate_Change_Date__c = clientOffer.Last_Contrib_Rate_Change_Date_Inccont__c;
                    opportunity.Offer_File_Last_Hardship_Withdrawal_Date__c = clientOffer.Last_Hardship_Withdrawal_Date_Inccont__c;     
                    opportunity.Offer_File_Max_Employer_Match_Pct__c = clientOffer.Max_Employer_Match_Pct_Inccont__c;   
                    opportunity.Offer_File_Partic_Auto_Increase__c = clientOffer.Partic_Auto_Increase_Inccont__c;   
                    opportunity.Offer_File_Plan_Offers_Auto_Increase__c = clientOffer.Plan_Auto_Increase_Inccont__c;        
                    opportunity.Offer_File_Plan_Max_Deferral_Amount__c = clientOffer.Plan_Max_Deferral_Amt_Inccont__c;      
                    opportunity.Offer_File_Plan_Max_Deferral_Pct__c = clientOffer.Plan_Max_Deferral_Pct_Inccont__c;             
                    opportunity.Offer_File_Current_Post_Tax_Deferral_Amt__c = clientOffer.PostTax_Deferral_Amt_Inccont__c;              
                    opportunity.Offer_File_Current_Post_Tax_Deferral_Pct__c = clientOffer.PostTax_Deferral_Pct_Inccont__c;  
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Amt__c = clientOffer.PreTax_Deferral_Amt_Inccont__c;    
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Pct__c = clientOffer.PreTax_Deferral_Pct_Inccont__c;
                    
                    //Code added for RS Data feed functionality: case # 00008723.
                    opportunity.Offer_File_Current_Roth_Deferral_Pct__c = clientOffer.Current_Roth_Deferral_Pct_Inccont__c;
                    opportunity.Offer_File_Current_Roth_Deferral_Amount__c = clientOffer.Current_Roth_Deferral_Amt_Inccont__c;
                    
                }else if(offerCode == 'catchup'){
                    
                    opportunity.Offer_File_Catch_Up_Amt__c = clientOffer.Catch_Up_Amt_Catchup__c;                           
                    opportunity.Offer_File_Catch_Up_Pct__c = clientOffer.Catch_Up_Pct_Catchup__c;                           
                    opportunity.Offer_File_Last_Contrib_Rate_Change_Date__c = clientOffer.Last_Contrib_Rate_Change_Date_Catchup__c; 
                    opportunity.Offer_File_Partic_Auto_Increase__c = clientOffer.Partic_Auto_Increase_Catchup__c;                                   
                    opportunity.Offer_File_Plan_Offers_Auto_Increase__c = clientOffer.Plan_Auto_Increase_Catchup__c;                            
                    opportunity.Offer_File_Plan_Max_Deferral_Amount__c = clientOffer.Plan_Max_Deferral_Amt_Catchup__c;                              
                    opportunity.Offer_File_Plan_Max_Deferral_Pct__c = clientOffer.Plan_Max_Deferral_Pct_Catchup__c;                                     
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Amt__c = clientOffer.PreTax_Deferral_Amt_Catchup__c;                                
                    opportunity.Offer_File_Current_Pre_Tax_Deferral_Pct__c = clientOffer.PreTax_Deferral_Pct_Catchup__c;
                    
                    //Code added for RS Data feed functionality: case # 00008723.
                    opportunity.Offer_File_Current_Roth_Deferral_Pct__c = clientOffer.Current_Roth_Deferral_Pct_Catchup__c;
                    opportunity.Offer_File_Current_Roth_Deferral_Amount__c = clientOffer.Current_Roth_Deferral_Amt_Catchup__c;
                    opportunity.Offer_CurrentCatchUp_RothDeferral_Pct__c = clientOffer.Cur_Catch_Up_Roth_Deferral_Pct_Catchup__c;
                    opportunity.Offer_CurrentCatchUp_RothDeferral_Amt__c = clientOffer.Cur_Catch_Up_Roth_Deferral_Amt_Catchup__c;
                    
                }else if(offerCode == 'manacct'){
                    
                    opportunity.Offer_File_Manage_Acct_Plan_Offers__c = clientOffer.Manage_Acct_Plan_Offered_Manacct__c;
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Manacct__c;  
                    
                }else if(offerCode == 'manactips' || offerCode == 'manactadv'){
                    
                    opportunity.Offer_File_Manage_Acct_Plan_Offers__c = clientOffer.Manage_Acct_Plan_Offered_Manactips__c;      
                    opportunity.Offer_File_Registered_for_Online_Access__c = clientOffer.Reg_online_access_Manactips__c;
                    
                }else if(offerCode == 'rr' || offerCode == 'rrma'){//Case # 00011254/00011255:For Retirement Readiness Offer
                    
                    system.debug('=================================== offerCode ' + offerCode);
                    
                    Boolean isRRMarketingNumber = false;
                    
                    if(offerPop != null && offerPop.CTI_DNIS_Number__c != null) {
                        List<Rule__c> ruleList = [Select Value_ist__c, Name, Rule_Group_ist__c, Description_ist__c From Rule__c where Name =:offerPop.CTI_DNIS_Number__c and Rule_Group_ist__c = 'Campaign-Lead-Source-Translation'];
                        if(ruleList != null && ruleList.size() > 0) {
                            for(Rule__c rule : ruleList) {
                                if(rule.Description_ist__c != null && rule.Description_ist__c.contains(UltimatePopControllerHelper.CAMPAIGN_LEAD_SOURCE_TYPE_RR)) {
                                    isRRMarketingNumber = true;
                                    break;
                                }
                                else
                                    isRRMarketingNumber = false;
                            }    
                        }
                        
                        
                      
                        if(!isRRMarketingNumber)
                            opportunity.RR_Campaign__c = CTIPopController.getCampaignInfo(offerPop.CTI_DNIS_Number__c, account);
                    }
                    
                    if(!isRRMarketingNumber && opportunity.RR_Campaign__c == null) {
                       
                        List<Account> clientList = [Select Id, Name, PersonContactId from Account Where Id =: account.Id];
                        if(clientList.size() > 0 && clientList[0].PersonContactId != null) {
                            
                            List<Selectoption> rrCampaigns = ClientProfileHelper.getRRCampaigns(clientList[0].PersonContactId, false);
                            if(rrCampaigns.size() > 0)
                                opportunity.RR_Campaign__c = rrCampaigns[0].getValue();
                        }
                    }
                    
                    system.debug('=================================== isRRMarketingNumber ' + isRRMarketingNumber);
                    system.debug('=================================== opportunity.RR_Campaign__c ' + opportunity.RR_Campaign__c);
                    
                }
                
                insert(opportunity);
                
                
                try {
                   
                   
                    
                    if(opportunity != null && opportunity.Id != null && opportunity.OwnerId != temp) {
                        
                        system.debug('Previous Owner : ' + opportunity.OwnerId);
                        system.debug('New Owner : ' + temp);
                        
                        Opportunity tempOpp = new Opportunity();
                        tempOpp.Id = opportunity.Id;
                        tempOpp.OwnerId = temp;
                        
                        
                        update(tempOpp);
                    }
                }
                catch(Exception e) {
                    system.debug('Error while changing the OwnerId for the Opportunity due to : ' + e);
                }
                
               
                if((offerCode == 'rr' || offerCode == 'rrma') && offerPlan != null){
                    if (account.RR_Eligible__pc==false || account.RR_Eligible__pc==null || account.OwnerId!=Label.I_ENV_DefaultISTAccountOwner){
                        account.RR_Eligible__pc = true;
                        account.OwnerId=Label.I_ENV_DefaultISTAccountOwner;
                        update account;
                    }
                    RRBatchProcessHelper.ClientWrapper clientW = new RRBatchProcessHelper.ClientWrapper(account, (Double)clientOffer.get('accountbalance_' + offerCode + '__c'), (Date)clientOffer.get('OfferDate_' + offerCode+ '__c'));
                    RRBatchProcessHelper.executeRRBatchForClient(clientW, offerPlan);
                    
                    RRAgentPermissionBatchProcessHelper.acessAssignmentForRRProducerByPlanIds(new Set<String> {offerPlan.Native_Plan_ID__c}, new Set<String> {account.SSN__c});
                }
                
            }       
            
           
            if(offerPop != null) {
                offerPop.Opportunity__c = opportunity.Id;
                offerPop.Action__c = UltimatePopControllerHelper.OFFERPOP_STATUS_RTM;
                update offerPop;
            }
            
              
                       
            recordCaseAction(opportunity,cp.Case__c, account.SSN__c);
           
            
            
            if(offerPop == null)
                offerPop = new Offer_Pop__c(); 
            
            
            offerPop.OfferPop_Transaction_ID__c = account.SSN__c + ConstantUtils.UNIQUE_SEPERATOR + UserInfo.getSessionId()+campaign.id;
            offerPop.Client_ID__c = account.SSN__c;
            offerPop.User__c = Userinfo.getUserId();
            
            
            if(campaign != null) {
                offerPop.Offers_Available__c = 'Yes';
                offerPop.Top_Offer__c = campaign.id;
            }
            else {
                offerPop.Offers_Available__c = 'No';
            }
            
            
            offerPop.CTI_DNIS_Number__c = dnisNumber;
            offerPop.Client__c = account.Id;
            offerPop.Campaign__c = CTIPopController.getCampaignInfo(dnisNumber, account); 
            offerPop.Lead_Source__c = CTIPopController.doLeadSourceTranslation(dnisNumber);
            
            
            
            offerPop.Source__c = UltimatePopControllerHelper.SOURCE_CTI;
           
            
            offerPop.Action__c = UltimatePopControllerHelper.OFFERPOP_STATUS_RTM;
            offerPop.Opportunity__c = opportunity.Id;
            
            system.debug('><<><><><><><><><><<><><><><><><><><><><><> offerPop : ' + offerPop);
            
            if(offerPop.Id == null)
                DataBase.insert(offerPop);
            else
                DataBase.update(offerPop); 
            
           
            
          
            if (opportunity.id != null && (opportunity.offer_response_reason__c != 'Ask me again next time' && opportunity.offer_response_reason__c != 'Misdirected Call' && opportunity.offer_response_reason__c != 'Security Not Validated/Other Caller' && opportunity.offer_response_reason__c != 'Escalated Call' && opportunity.offer_response_reason__c != 'Does Not Meet Criteria' && opportunity.offer_response_reason__c != 'Send email' && opportunity.offer_response_reason__c != 'Send me materials')) {
                clientOffer.put('status_' + campaign.offer_code__c + '__c','Closed');
                update(clientOffer);  
               
            }
        } catch (Exception e) {
            Database.rollback(sp); //Rollback
            try {
                Client_Share_Audit__c csaEx = new Client_Share_Audit__c();          
                csaEx.Exception__c = String.valueOf(e); 
                csaEx.Row_cause__c = 'ERROR PROCESSING OFFER FOR USER : ' + UserInfo.getName();
                Database.insert(csaEx, false);
            }
            catch (Exception ex) {
             
            }
        }
        
       
        
        List<Opportunity> oppList = [select Id, Track_Summary__c, Summary_Code__c,SSN_TIN__c ,Offer_Plan_Number__c,
                                     CreatedDate, Offer_Response_Reason__c, AccountId, Offer_Response__c, offer_code__c ,Account.SSN_TIN__c
                                     from Opportunity where Id = :opportunity.Id];
       
        
        Opportunity opp = oppList[0];
        
      
        
        String planIdText = (String)clientOffer.get('planid_' + campaign.offer_code__c + '__c');
        if(planIdText == null || planIdText == '')
            planIdText = 'None';
        
        Savepoint sp1 = Database.setSavepoint();
        
        try {
            if(opp.Track_Summary__c == 'Yes' && account.SSN__c != null && account.SSN__c != ''){
                
                Campaign_Offer_Summary__c cos = null;
                List<Campaign_Offer_Summary__c> cosList = [select id,Account_Name__c,Summary_Code__c,Phone_Message_History__c,Phone_Message_Count__c,Last_Phone_Opportunity__c from Campaign_Offer_Summary__c where Summary_Code__c=:opp.Summary_Code__c];
                
                if(cosList != null && cosList.size() > 0)
                    cos = cosList[0];
                
                
              
                String history = null;
                
                if(cos == null){
                    cos = new Campaign_Offer_Summary__c();
                    cos.Summary_Code__c = opp.Summary_Code__c;// account.SSN__c + plan.Native_Plan_ID__c + campaign.offer_code__c;
                    cos.Account_Name__c = account.id;
                    cos.Customer_SSN__c = account.SSN__c;
                    cos.OfferCode__c = campaign.offer_code__c;
                    
                    cos.Planid_Text__c = planIdText;
                    cos.Last_Phone_Opportunity__c = opp.id;
                    cos.Phone_Message_Count__c = 1;
                    cos.Phone_Message_History__c = opp.CreatedDate.format('MM/dd/yyyy') + '-' + opp.Offer_Response_Reason__c + '; ';
                    if(cos.Account_Name__c == null)
                        cos.Account_Name__c = opp.AccountId; 
                    
                    insert(cos);    
                }
                else {
                    cos.Last_Phone_Opportunity__c=opp.id;
                    
                    if(cos.Phone_Message_Count__c == null)
                        cos.Phone_Message_Count__c = 0;
                    
                    cos.Phone_Message_Count__c+=1;
                    
                    
                    history = opp.CreatedDate.format('MM/dd/yyyy') + '-' + opp.Offer_Response_Reason__c + '; ' + (cos.Phone_Message_History__c == null ? '' : cos.Phone_Message_History__c);
                    if(history.length() >= 300)
                        history = history.substring(0, 300);
                    
                    cos.Phone_Message_History__c = history;
                    if(cos.Account_Name__c==null)
                        cos.Account_Name__c=opp.AccountId;
                    
                    update(cos); 
                }
                
               
                I_OfferController.updateDB2PhoneSection(account.SSN__c, planIdText, opp.offer_code__c, opp.Offer_Response__c, opp.CreatedDate);
                
                system.debug('=================================================== In insertCampaignOfferSummary() : After Insert/Update cos ' + cos);
            }
        }
        catch(Exception e) {
            system.debug('Error while Insert/Update record in Campaign_Offer_Summary__c due to : ' + e);
            
            Database.rollback(sp1); //Rollback
            
            ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.FATAL, 'An error occurred while processing the request.'));
        }
        
        
        return opportunity.name;
    } 






static testMethod void testCreateOpportunityWithPlanAndCampaign() {
         
    
boolean Manual=true;         
     Campaign campaign = new Campaign(
            Name = 'Test Campaign',
            Offer_Code__c = 'testcode',
            Offer_Priority__c = 1.777,
            Offer_Opportunity_Record_Type_ID__c = '012xxxxxxxxxxxxxxx'
        );
        insert campaign;

        
        Plan__c plan = new Plan__c(
            Name = 'Test Plan',
            Native_Plan_ID__c = 'TestPlanID'
        );
        insert plan;

       
        Account account = new Account(
            LastName = 'Doe',
            FirstName = 'John',
            SSN__c = '123456789',
            PersonBirthdate = Date.today().addYears(-30),
            PersonMailingStreet = '123 Test St',
            PersonMailingCity = 'Test City',
            PersonMailingState = 'TX',
            PersonMailingPostalCode = '12345',
            PersonEmail = 'john.doe@test.com',
            PersonHomePhone = '555-555-5555',
            Sex__c = 'M'
        );
        insert account;

        
        Client_Offer__c clientOffer = new Client_Offer__c(
            Account_Last_Name__c = 'Doe',
            Account_First_Name__c = 'John',
            Account_Ext_Id__c = '123456789',
            Account_Birthdate__c = Date.today().addYears(-30),
            Account_Address1__c = '123 Test St',
            Account_City__c = 'Test City',
            Account_State__c = 'TX',
            Account_Zip__c = '12345',
            Account_Email__c = 'john.doe@test.com',
            Account_Phone__c = '555-555-5555',
            Account_Gender__c = 'M'
            //PlanId_testcode__c = plan.Id
        );
        insert clientOffer;

        
        CTI_Console_Pop__c ctiPop = new CTI_Console_Pop__c(
            Account__c = account.Id,
            CTI_Params__c = 'param1;param2;param3:testDNIS',
            ExternalID__c='123456789'
        );
        insert ctiPop;
     
          Campaign campaignName = [SELECT Id, Name FROM Campaign WHERE Name = 'Test Campaign' LIMIT 1];
        Plan__c planName = [SELECT Id, Name FROM Plan__c WHERE Name = 'Test Plan' LIMIT 1];
        Account accountName = [SELECT Id, LastName, FirstName FROM Account WHERE LastName = 'Doe' LIMIT 1];
        Client_Offer__c clientOfferId = [SELECT Id FROM Client_Offer__c LIMIT 1];
      // Opportunity OpportunityNamee=[SELECT ID,NAME FROM Opportunity Where NAME='Test Name' LIMIT 1];
        
        
        Test.startTest();
        String opportunityName = cTargetedMessage.createOpportunityWithPlanAndCampaign(
            campaignName.Name, planName.Name, accountName.LastName, UserInfo.getUserId(), 'Response', 'Reason', 'Comment', accountName.Id,Manual
        );
        Test.stopTest();
        
        // Verify the results
        Opportunity createdOpportunity = [SELECT Id, Name, AccountId, CampaignId, OwnerId, StageName FROM Opportunity WHERE Name = :opportunityName LIMIT 1];
        System.assertEquals(planName.Id, createdOpportunity.AccountId);
        System.assertEquals(campaignName.Id, createdOpportunity.CampaignId);
        System.assertEquals('Needs Analysis', createdOpportunity.StageName);
         
         
         

     }
