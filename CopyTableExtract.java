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
