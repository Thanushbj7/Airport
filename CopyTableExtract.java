@isTest
private class TestOpportunityUpdate {

    @isTest
    static void testUpdateOpportunityAgreementSignaturesPending() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName 'Agreement Signatures Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of opptoupdate
        // Assert other outcomes as needed
    }
    
    @isTest
    static void testUpdateOpportunityPlanDeliveryPending() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName 'Plan Delivery Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of opptoupdate
        // Assert other outcomes as needed
    }
    
    @isTest
    static void testOpportunityCannotBeSubmittedForReview() {
        List<Opportunity> oppList = new List<Opportunity>();
        // Create Opportunity records with StageName other than 'Agreement Signatures Pending' or 'Plan Delivery Pending'
        // Insert Opportunity records into database
        
        // Call the method under test
        Test.startTest();
        // Execute your method here
        Test.stopTest();
        
        // Assert the values of MaptoReturn
        // Assert other outcomes as needed
    }
}











Opportunity opptoupdate=new Opportunity();
                for(Opportunity tempopp:oppList) { 
                    
                    if(tempopp.StageName=='Agreement Signatures Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;                
                        opptoupdate.Client_Sign_Date__c=tempopp.Client_Sign_Date__c;
                    }
                    else if(tempopp.StageName=='Plan Delivery Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;  
                    }
                    else { 
                        MaptoReturn.put('CanNotChange','Cannot be submitted for review at this stage.');
                    }
                }





@IsTest
public class OpptSendForApprovalCntrlrTest {
    
    @testSetup 
    static void setup() {
        RecordType rTypeopportunity = TestUtils.getRecordType('Opportunity', 'BD Financial Agreement');
        RecordType rTypeopportunitychild = TestUtils.getRecordType('Opportunity', 'BD Financial Agreement Amendment');    
        RecordType rTypeAccount = TestUtils.getRecordType('Account', 'Other Firms');
        
        Account client = new Account(Name = 'Test Account', RecordTypeId = rTypeAccount.Id);
        insert client;
        
        User testuser = [select Id from User Where Id =: UserInfo.getUserId()];  
        Plan__c plan = TestUtils.createPlan('IPS', null, false);
        plan.RR_Email_Permission_Level__c = 'Opt In';
        
        insert plan;

        // Create Opportunities
        List<Opportunity> opportunities = new List<Opportunity>{
            new Opportunity(Name = 'Test OpportunityTest123', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule_Type__c = 'Hourly', One_Time_Total_Fee__c = 30.00, 
                            Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today()),                                             
            new Opportunity(Name = 'Test OpportunityTest1234', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', I_agree_I_am_licensed__c = 'Yes', Term__c = 'Annual', Fee_Schedule__c = 'Monthly', 
                            payment_type__c = 'ACH', CloseDate = System.today(), Intial_Flat_Fee__c = 20.00),
            new Opportunity(Name = 'Test OpportunityTest12345', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today()), 
            new Opportunity(Name = 'Test OpportunityTest12346', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Plan Delivery Pending', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'New', I_agree_I_am_licensed__c = 'Yes', Term__c = 'Annual', Fee_Schedule__c = 'Monthly', 
                            payment_type__c = 'ACH', CloseDate = System.today(), Intial_Flat_Fee__c = 20.00),
            new Opportunity(Name = 'Test OpportunityTest12347', AccountId = client.Id, Plan__c = plan.Id, 
                            OwnerId = testuser.Id, StageName = 'Closed Won', RecordTypeId = rTypeopportunity.Id, 
                            Opportunity_Status__c = 'Closed', Term__c = 'One-Time', One_Time_Total_Fee__c = 40.00, Fee_Schedule__c = 'Split', payment_type__c = 'ACH', CloseDate = System.today())
        };
        insert opportunities;
        
        // Create Attachments
        List<Attachment> attachments = new List<Attachment>{
            new Attachment(Name = 'Agreement', Body = Blob.valueOf('Unit Test Attachment Body'), ParentId = opportunities[1].Id),
            new Attachment(Name = 'Agreement', Body = Blob.valueOf('Unit Test Attachment Body'), ParentId = opportunities[3].Id)
        };
        insert attachments;
        
        // Create Content Document and Link
        ContentVersion contentVersion = new ContentVersion(
            Title = 'FinancialAgreement',
            PathOnClient = 'Penguins.jpg',
            VersionData = Blob.valueOf('Test Content'),
            IsMajorVersion = true
        );
        insert contentVersion;
        
        List<ContentDocument> documents = [SELECT Id, Title, LatestPublishedVersionId FROM ContentDocument];
        ContentDocumentLink cdl = new ContentDocumentLink(
            LinkedEntityId = opportunities[0].Id,
            ContentDocumentId = documents[0].Id,
            ShareType = 'V'
        );
        insert cdl;

        // Create Financial Plan
        Financial_Plan__c testplan = new Financial_Plan__c(
            BD_Financial_Agreement__c = opportunities[3].Id,
            TRP_Plan_Approval_Status__c = 'Not Started'
        );
        insert testplan;
    }

    @isTest
    public static void opportunity123() {
        Test.startTest(); 
        List<Opportunity> fecttempdata1 = [select Id from Opportunity where Name = 'Test OpportunityTest123'];
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata1[0].Id, 'Theme4d');                   
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Opportunity should be successfully updated.');
        System.assertEquals('Opportunity sent for TRP Approval and stage is changed to TRP Agreement Approval Pending.', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity1234() {
        Test.startTest(); 
        List<Opportunity> fecttempdata2 = [select Id from Opportunity where Name = 'Test OpportunityTest1234']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata2[0].Id, '');                
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Opportunity should be successfully updated.');
        System.assertEquals('Opportunity sent for TRP Approval and stage is changed to TRP Agreement Approval Pending.', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity12345() {
        Test.startTest(); 
        List<Opportunity> fecttempdata3 = [select Id from Opportunity where Name = 'Test OpportunityTest12345']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata3[0].Id, '');                
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Attachment'), 'There should be an error for missing agreement attachment.');
        System.assertEquals('There should be at least one Attachment having "Agreement" keyword in the name before Submitting for Approval.', resultMap.get('Attachment'));
    }
	
    @isTest
    public static void opportunity12346() {
        Test.startTest(); 
        List<Opportunity> fecttempdata4 = [select Id from Opportunity where Name = 'Test OpportunityTest12346']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata4[0].Id, '');           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Success'), 'Financial Plan should be successfully sent for TRP Approval.');
        System.assertEquals('Financial Plan is sent for TRP Approval', resultMap.get('Success'));
    }
    
    @isTest
    public static void opportunity12347() {
        Test.startTest(); 
        List<Opportunity> fecttempdata = [select Id from Opportunity where Name = 'Test OpportunityTest12347']; 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata[0].Id, '');           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('CanNotChange'), 'Opportunity should not be submitted for review at this stage.');
        System.assertEquals('Cannot be submitted for review at this stage.', resultMap.get('CanNotChange'));
    }
    
    @isTest
    public static void opportunityError() {
        Test.startTest(); 
        Map<String, String> resultMap = OpptSendForApprovalCntrlr.FetchOpportunity(null, null);           
        Test.stopTest();
        
        System.assert(resultMap.containsKey('Exception'), 'There should be an exception for null opportunity ID.');
        System.assertNotEquals('', resultMap.get('Exception'), 'Exception message should not be empty.');
    }
		}




















@IsTest
public class OpptSendForApprovalCntrlrTest {
    
    @testSetup 
    static void setup() {
        RecordType rTypeopportunity=TestUtils.getRecordType('Opportunity', 'BD Financial Agreement');
        RecordType rTypeopportunitychild=TestUtils.getRecordType('Opportunity', 'BD Financial Agreement Amendment');    
        RecordType rTypeAccount=TestUtils.getRecordType('Account', 'Other Firms');
        
        Account client = new Account(Name='Test Account', RecordTypeId = rTypeAccount.Id );
        
        insert client;
        
        User testuser = [select Id from User Where Id =: UserInfo.getUserId()];  
        Plan__c plan = TestUtils.createPlan('IPS', null, false);
        plan.RR_Email_Permission_Level__c = 'Opt In';
        
        //Create Opportunity
        
        Opportunity pOpportunity = new Opportunity(Name = 'Test OpportunityTest123', AccountId = client.Id, Plan__c = plan.Id, 
                                                   OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                   Opportunity_Status__c = 'New', Term__c = 'One-Time',Fee_Schedule_Type__c = 'Hourly',One_Time_Total_Fee__c=30.00, Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        Opportunity pOpportunity1 = new Opportunity(Name = 'Test OpportunityTest1234', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', I_agree_I_am_licensed__c='Yes',Term__c = 'Annual', Fee_Schedule__c = 'Monthly', payment_type__c='ACH', CloseDate = System.today(),Intial_Flat_Fee__c=20.00);                                             
        
        Opportunity pOpportunity2 = new Opportunity(Name = 'Test OpportunityTest12345', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', Term__c = 'One-Time', Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        Opportunity pOpportunity3 = new Opportunity(Name = 'Test OpportunityTest12346', AccountId = client.Id, Plan__c = plan.Id, 
                                                    OwnerId = testuser.Id,StageName = 'Plan Delivery Pending', RecordTypeId = rTypeopportunity.ID, 
                                                    Opportunity_Status__c = 'New', I_agree_I_am_licensed__c='Yes',Term__c = 'Annual', Fee_Schedule__c = 'Monthly', payment_type__c='ACH', CloseDate = System.today(),Intial_Flat_Fee__c=20.00);                                               
        
        Opportunity pOpportunity4 = new Opportunity(Name = 'Test OpportunityTest12347', AccountId = client.Id, Plan__c = plan.Id, 
                                                   OwnerId = testuser.Id,StageName = 'Agreement Signatures Pending', RecordTypeId = rTypeopportunity.ID, 
                                                   Opportunity_Status__c = 'New', Term__c = 'One-Time', One_Time_Total_Fee__c=40.00, Fee_Schedule__c = 'Split', payment_type__c='ACH', CloseDate = System.today());                                             
        
        List<Opportunity> tempopport=new List<Opportunity>();
        tempopport.add(pOpportunity) ;
        tempopport.add(pOpportunity1) ;
        tempopport.add(pOpportunity2) ;
        tempopport.add(pOpportunity3) ;
        tempopport.add(pOpportunity4) ;
        insert tempopport;
        
        Attachment attach=new Attachment();     
        attach.Name='Agreement';
        Blob bodyBlob=Blob.valueOf('Unit Test Attachment Body');
        attach.body=bodyBlob;
        attach.parentId=tempopport[1].Id;             
        insert attach; 
        
        Attachment attach2=new Attachment();     
        attach2.Name='Agreement';
        Blob bodyBlob2=Blob.valueOf('Unit Test Attachment Body');
        attach2.body=bodyBlob2;
        attach2.parentId=tempopport[3].Id;             
        insert attach2;
                
        // Content Document creation
        
        ContentVersion contentVersion = new ContentVersion(
            Title = 'FinancialAgreement',
            PathOnClient = 'Penguins.jpg',
            VersionData = Blob.valueOf('Test Content'),
            IsMajorVersion = true
        );
        insert contentVersion;
        
        List<ContentDocument> documents = [SELECT Id, Title, LatestPublishedVersionId FROM ContentDocument];
        
        //create ContentDocumentLink  record 
        ContentDocumentLink cdl = New ContentDocumentLink();
        cdl.LinkedEntityId = tempopport[0].id;
        cdl.ContentDocumentId = documents[0].Id;
        cdl.shareType = 'V';
        insert cdl;
        
        System.debug('0. CDL ' + cdl);
        
        Financial_Plan__c testplan=new Financial_Plan__c();
        testplan.BD_Financial_Agreement__c=tempopport[3].Id;
        testplan.TRP_Plan_Approval_Status__c='Not Started';
        insert testplan;
        
    }

    @isTest
    public static void opportunity123() {
        Test.startTest(); 
        List<Opportunity> fecttempdata1=[select Id from Opportunity where Name=:'Test OpportunityTest123'];
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata1[0].Id,'Theme4d');                   
        Test.stopTest();
    }
    
    @isTest
    public static void opportunity1234() {
        Test.startTest(); 
        List<Opportunity> fecttempdata2=[select Id from Opportunity where Name=:'Test OpportunityTest1234']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata2[0].Id,'');                
        Test.stopTest();
    }
    
    @isTest
    public static void opportunity12345() {
        Test.startTest(); 
        List<Opportunity> fecttempdata3=[select Id from Opportunity where Name=:'Test OpportunityTest12345']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata3[0].Id,'');                
        Test.stopTest();
    }
	
    @isTest
	public static void opportunity123456() {
        Test.startTest(); 
        List<Opportunity> fecttempdata4=[select Id from Opportunity where Name=:'Test OpportunityTest12346']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata4[0].Id,'');           
        Test.stopTest();
    }
    
    @isTest
	public static void opportunity12347() {
        Test.startTest(); 
        List<Opportunity> fecttempdata = [select Id from Opportunity where Name=:'Test OpportunityTest12347']; 
        OpptSendForApprovalCntrlr.FetchOpportunity(fecttempdata[0].Id,'');           
        Test.stopTest();
    }
    
    @isTest
	public static void opportunityError() {
        Test.startTest(); 
        OpptSendForApprovalCntrlr.FetchOpportunity(null,null);           
        Test.stopTest();
    }
}















@AuraEnabled
    public static Map<string,string> FetchOpportunity(string Id,string optionaltheme) {
        
        Map<String,String> MaptoReturn = new Map<String,String>(); 
        try{
            String theme;
            if(optionaltheme!='') {  
                theme=optionaltheme;
            }
            else {
                theme = UserInfo.getUiThemeDisplayed();  
            }

            integer countagreement=0;
            integer countotherdoc=0;
            List<Opportunity> oppList =[SELECT Id, IsDeleted, AccountId, RecordTypeId, Name, Description, StageName, Amount, OwnerId, 
                                        Opportunity_Status__c,  Plan_Name__c, Plan__c,  Opportunity_Type__c, Plan_Number__c, Action_Plan_Delivery_Method__c, Client_Profiler_Link__c, Count__c, 
                                        ParentOpportunity__c, Plan_Accepted__c, Plan_Delivered__c, Amendment_Parent_Opportunity__c,DP_Doc_RecordId__c,Client_Sign_Date__c,
                                        Fee_Schedule_Type__c,I_agree_I_am_licensed__c,payment_type__c,Term__c,One_Time_Total_Fee__c,Fee_Per_Hour__c,Number_of_Hours__c,Split_Amt_Pd_After_Delivery__c,Split_Amt_Pd_Upon_Sig__c,
                                        Intial_Flat_Fee__c,CreatedById ,Fee_Schedule__c FROM Opportunity where Id=:id]; 
            System.debug('Opportunityfetched-----'+oppList);
            
            Map<String,Opportunity> oppMap = new Map<String,Opportunity>();
            Map<String, List<Attachment>> oppAttchMap = new Map<String, List<Attachment>>();

            for(Opportunity tempopp:oppList) {
                
                MaptoReturn.put('Opportunity',tempopp.Name);
                
                User tempuser=[Select Name from User where Id=:tempopp.CreatedById];
                
                MaptoReturn.put('CreatedBy',tempuser.Name);  
            }                             
            
            String agreementDocName=Label.I_APP_FP_AGREEMENT_DOC_NAME;
            System.debug('Agreementfetched-----'+agreementDocName);
            if (String.IsBlank(agreementDocName))
                agreementDocName = '%agreement%';
            else
                agreementDocName = '%'+agreementDocName+'%';

            Boolean flag=false;
            ID BDRecordId=Schema.SObjectType.Opportunity.RecordTypeInfosByName.get('BD Financial Agreement').RecordTypeId;
            ID BDRecordIdchild=Schema.SObjectType.Opportunity.RecordTypeInfosByName.get('BD Financial Agreement Amendment').RecordTypeId;
            
            for(Opportunity OppIter : OppList) {
                if(OppIter.Amendment_Parent_Opportunity__c==null) {
                    if(OppIter.recordtypeid==BDRecordId) {
                        oppMap.put(String.valueOf(OppIter.Id), OppIter);
                    }
                }
                
                else {
                    if(OppIter.recordtypeid==BDRecordIdchild) {
                        oppMap.put(String.valueOf(OppIter.Id), OppIter);
                    }  
                }
            }
            
            List<Attachment> attachList =[select id,name,parentid from Attachment where name LIKE :agreementDocName and parentid IN: oppMap.KeySet()];
            for(Attachment attach : attachList) {   
                List<Attachment> attachLists ;
                if(oppAttchMap.containsKey(attach.parentid)){
                    attachLists = oppAttchMap.get(attach.parentid);
                    attachLists.add(attach);
                    
                }
                else{
                    attachLists = new List<Attachment>();
                    attachLists.add(attach);
                }
                oppAttchMap.put(attach.parentid,attachLists);
            }

            List<ContentDocumentLink> CDL= [SELECT ContentDocumentId FROM ContentDocumentLink WHERE LinkedEntityId =: Id]; 
            List<Id> tempId= new List<Id>();
            for(ContentDocumentLink tempcontentid:CDL) {
                tempId.add(tempcontentid.ContentDocumentId) ; 
            }
            
            List<ContentDocument> CD=  [Select Id, Title, parentid from ContentDocument Where Id IN :tempId]; 
            for(ContentDocument tempcontentdoc:CD) {
                string title=tempcontentdoc.Title; 
                if(title.containsIgnoreCase('agreement')) {
                    
                    countagreement++;
                    countotherdoc++;
                }
                else {
                    countotherdoc++;
                }
            }

            for(Opportunity opp : oppMap.Values()) {
                
                system.debug('oppAttchMap.get(opp.Id) ### '+oppAttchMap.get(opp.Id));
                system.debug('opp.DP_Doc_RecordId__c ### '+opp.DP_Doc_RecordId__c);
                
                //  if(oppAttchMap.get(opp.Id) == null || (oppAttchMap.get(opp.Id) != null && String.IsBlank(opp.DP_Doc_RecordId__c)))
                if(oppAttchMap.get(opp.Id) == null && String.IsBlank(opp.DP_Doc_RecordId__c) && countagreement==0)
                    // opp.adderror('There should be at least one Attachment having "Agreement" keyword in the name before entering the client sign date.'); 
                    MaptoReturn.put('Attachment','There should be at least one Attachment having "Agreement" keyword in the name before Submitting for Approval.'); 
                
                String errMsg = IsValidAfterClientSignDt(opp);  
                
                if(!String.isBlank(errMsg)) {
                    system.debug('all validations--'+ errMsg);
                    //opp.adderror(errMsg);
                    MaptoReturn.put('OtherValidation',errMsg);
                }
                
                MaptoReturn.put('Theme',theme); 
            }
            if(MaptoReturn.containsKey('Attachment') || MaptoReturn.containsKey('OtherValidation')) {
                System.debug('Last Opportunity details errors--'+MaptoReturn);
            }
            else {
                Opportunity opptoupdate=new Opportunity();
                for(Opportunity tempopp:oppList) { 
                    
                    if(tempopp.StageName=='Agreement Signatures Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;                
                        opptoupdate.Client_Sign_Date__c=tempopp.Client_Sign_Date__c;
                    }
                    else if(tempopp.StageName=='Plan Delivery Pending') {
                        
                        opptoupdate.Id=tempopp.Id;
                        opptoupdate.StageName=tempopp.StageName;  
                    }
                    else { 
                        MaptoReturn.put('CanNotChange','Cannot be submitted for review at this stage.');
                    }
                }
                
                if(opptoupdate.Id!=null &&  opptoupdate.StageName=='Agreement Signatures Pending') {
                    
                    if(opptoupdate.Client_Sign_Date__c==null) {
                        opptoupdate.StageName='TRP Agreement Approval Pending'; 
                        opptoupdate.Client_Sign_Date__c=Date.today();
                        update opptoupdate;
                        MaptoReturn.put('Success','Opportunity sent for  TRP Approval and stage is changed to TRP Agreement Approval Pending.');
                    }
                    else {
                        opptoupdate.StageName='TRP Agreement Approval Pending';
                        update opptoupdate;  
                    }
                    
                    MaptoReturn.put('Success','Opportunity sent for  TRP Approval and stage is changed to TRP Agreement Approval Pending.');
                }
                
                else if(opptoupdate.Id!=null &&  opptoupdate.StageName=='Plan Delivery Pending') {
                    List<Financial_Plan__C> tempplan= [select Id,TRP_Plan_Approval_Status__c from Financial_Plan__c where BD_Financial_Agreement__c=:opptoupdate.Id  order by CreatedDate desc ] ; 
                    if(tempplan.size()>0) {
                        Financial_Plan__C tempfinacialplan=tempplan[0];
                        if(tempfinacialplan.TRP_Plan_Approval_Status__c=='Not Started') {
                            Financial_Plan__C plantoupdate=new Financial_Plan__C();
                            plantoupdate.Id=tempfinacialplan.Id;
                            plantoupdate.TRP_Plan_Approval_Status__c='Approval Pending';
                            update plantoupdate;
                            MaptoReturn.put('Success','Financial Plan is sent for TRP  Approval');
                        }
                        else
                            MaptoReturn.put('OtherValidation','No Action Performed');  
                    }
                    else {
                        MaptoReturn.put('OtherValidation','No Action Performed');
                    }
                    
                }
                
                else {
                    
                }

            }  
            return MaptoReturn;
        }
        catch(Exception ex) {
            MaptoReturn.put('Exception',ex.getMessage()); 
            return MaptoReturn;
        }
        
    }
