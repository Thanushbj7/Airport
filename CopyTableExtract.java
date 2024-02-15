private static void getPlanDefaultValues(Plan__c plan){
        //B-113462 KM-Platform Opt Phase 2 - Plan Automation -> Update logic to hardcode INGWIN to Client_Id__c field when PAAG Configuration is not LMP - Alex Church
         try{
             if(plan.PAAG_Configuration__c == ConstantUtils.PAAG_LMP){                 
                 plan.Client_Id__c = clientId.toUpperCase();
                 System.debug('Client Id -> ' + clientId.toUpperCase());
             }else{
                 plan.Client_Id__c = 'INGWIN';
                 System.debug('Client Id -> ' + plan.Client_Id__c);
             }
             
             if(plan.PAAG_Configuration__c == null  || String.isBlank(plan.PAAG_Configuration__c))
                plan.PAAG_Configuration__c = 'ILIAC';
          }catch(Exception e){
             System.debug('Exception Error: ' + e);
         }
     }




private static void createResultInfo(String planName, String objectName, String status, String errMessage){
        ResultInfo resultInfo;
         if(paagResultMap.containsKey(planName)){
            resultInfo = paagResultMap.get(planName);
         }
         else {
            resultInfo = new ResultInfo();
            resultInfo.planID = planName;
         }
         if(objectName == 'plan')
            resultInfo.planStatus = status;
         if(objectName == 'paag')
            resultInfo.paagStatus = status;
        if(objectName == 'GW')
            resultInfo.GWStatus = status;
         if(!String.isBlank(errMessage))
            resultInfo.errorMessage = errMessage;
            
         paagResultMap.put( planName, resultInfo);
    }








@isTest
private class YourApexClass_Test {

    @isTest
    static void testParseLocalOfficedNodeBooleanTrue() {
        // Set up necessary data or objects

        // Create a test instance of the class containing parseLocalOfficedNode method
        YourApexClass instance = new YourApexClass();

        // Create a mock XMLNode with a specific child node and text
        Dom.XMLNode mockChild = // Create a mock XMLNode with a specific child node and text

        // Call the method you want to test
        instance.parseLocalOfficedNode(mockChild, localOffice);

        // Assert the expected result, checking if localOffice has the expected Boolean value (true)
        System.assertEquals(true, localOffice.get(child.getName()));
    }

    @isTest
    static void testParseLocalOfficedNodeBooleanFalse() {
        // Set up necessary data or objects

        // Create a test instance of the class containing parseLocalOfficedNode method
        YourApexClass instance = new YourApexClass();

        // Create a mock XMLNode with a specific child node and text
        Dom.XMLNode mockChild = // Create a mock XMLNode with a specific child node and text

        // Call the method you want to test
        instance.parseLocalOfficedNode(mockChild, localOffice);

        // Assert the expected result, checking if localOffice has the expected Boolean value (false)
        System.assertEquals(false, localOffice.get(child.getName()));
    }

    // Add more test methods for other scenarios if needed
}







private static void parseLocalOfficedNode(Dom.XMLNode child,PAAG_Local_Office__c localOffice){
        if(child.getName() != null && localOfficeFieldMap.containsKey(child.getName().toLowerCase()) && (child.getText().trim()!='')){

            if(localOfficeFieldMap.get(child.getName().toLowerCase()).getType() == Schema.DisplayType.Boolean){
                 localOffice.put(child.getName(), Boolean.valueOf(child.getText().trim()));
            }
            else {
                 localOffice.put(child.getName(),child.getText().trim());      
            }
         }
    }










static testMethod void testRest_LMP() {
      
        String xml = '<sfplans>'
                    +'<plan>'
                    +'<planid> 971119</planid>'
                    +'<Plan_Name_ist__c>TestLMPPlan</Plan_Name_ist__c>'
            		+'<Market_ist__c>testMarket</Market_ist__c>'
                    +'<Display_Local_Office_Subsection>testMarket</Display_Local_Office_Subsection>'
                    +'<Display_Rep_Information_Subsection>testMarket1</Display_Rep_Information_Subsection>'
                    +'<Timeframes__c>'
                    +'</Timeframes__c>'
            		+'<Client_Id__c>testLMP</Client_Id__c>'
                    +'<PAAG_Configuration__c>LMP</PAAG_Configuration__c>'
                    +'</plan>'
                    +'</sfplans>';      
        
        RestRequest req = new RestRequest(); // Build the REST Request for testing
       
        req.addHeader('Content-Type', 'application/xml'); // Add a JSON Header as it is validated
        req.requestURI = '/services/apexrest/SETIT-Conversion/*';  
        req.httpMethod = 'POST';        // Perform a POST
        req.requestBody = Blob.valueof(xml); // Add JSON Message as a POST
        
        RestResponse res = new RestResponse();
        RestContext.request = req;
        RestContext.response = res;
        
                 SetITDataConversion.populatePAAG();
       
    }






@isTest
private class YourTestClass {

    @isTest
    static void parsePlanChildNodeTest() {
        // Assuming you have already set up necessary data or mocks for Plan__c and other dependencies

        // Creating a test instance of the class containing parsePlanChildNode method
        YourClassInstance instance = new YourClassInstance();

        // Creating a sample XMLNode for testing
        Dom.Document doc = new Dom.Document();
        Dom.XmlNode root = doc.createRootElement('root', null, null);
        Dom.XmlNode child = root.addChildElement('child', null, null);
        Dom.XmlNode marketNode = child.addChildElement('Market_ist__c', null, null);
        marketNode.addTextNode('H');

        // Mocking the planMap for the test
        Map<String, Plan__c> planMap = new Map<String, Plan__c>();

        // Invoking the method to test
        instance.parsePlanChildNode(child, planMap);

        // Asserting that 'Market_ist__c' is correctly set in the Plan__c object
        Plan__c testPlan = planMap.values().get(0); // Assuming there's only one plan in the map
        System.assertEquals('H', testPlan.Market_ist__c, 'Market_ist__c should be set to "H"');
    }
}











private static void parsePlanChildNode(Dom.XMLNode child,PAAG__c paag,Map<String ,PAAG__c> paagMap){
        String planName;
         for(DOM.XmlNode awr : child.getchildren()){
            Plan__c plan;
            System.debug('NAMENODE>>>>>>>>>>>>>>>>'+awr.getName());      
            if(!String.isBlank(awr.getName()) && awr.getName() == 'planid'){
                planName=awr.getText().trim();
                if(planMap.containsKey(planName))
                    plan = planMap.get(planName);
                else{
                    plan = new Plan__c();
                    plan.Name = planName;
                    planMap.put(planName, plan);
                }
                
            }
            else if(!String.isBlank(awr.getName()) &&  awr.getName() == 'Plan_Name_ist__c' ){
                String nodeValue = awr.getText().trim();
                if(planMap.containsKey(planName)){  
                    plan = planMap.get(planName);
                    plan.put('Plan_Name_ist__c', nodeValue);
                    planMap.put(planName, plan);    
                }
            }
            //B-113462 KM-Platform Opt Phase 2 - Plan Automation -> parse client Id for getPlanDefaultValues - Alex Church
            else if(!String.isBlank(awr.getName()) && awr.getName() == 'Client_Id__c'){
                String nodeValue = awr.getText().trim();
                clientId = nodeValue;
            }
            else if(!String.isBlank(awr.getName()) &&  awr.getName() == 'PAAG_Configuration__c' ){
                String nodeValue = awr.getText().trim();
                if(planMap.containsKey(planName)){  
                    plan = planMap.get(planName);
                    plan.put('PAAG_Configuration__c', nodeValue);
                    planMap.put(planName, plan);
                }
            }
            else if(!String.isBlank(awr.getName()) &&  awr.getName() == 'Market_ist__c'){
                String nodeValue = awr.getText().trim();
                if(planMap.containsKey(planName)){
                    plan = planMap.get(planName);
                    if(nodeValue == 'H' || nodeValue == 'E' || nodeValue == 'G' )
                        plan.put('Market_ist__c', nodeValue);
                    else if(nodeValue == 'C')
                        plan.put('Market_ist__c', nodeValue);
                    
                    planMap.put(planName, plan);    
                }
            }








import { LightningElement, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';

const columns = [
    { label: 'Case Number', fieldName: 'CaseNumber' },
    { label: 'Date', fieldName: 'CreatedDate' },
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Inquiry', fieldName: 'Call_Type__c' },
    { label: 'Transactions', fieldName: 'Call_Type__c' },
    { label: 'Account Maintenance', fieldName: 'Call_Type__c' },
    { label: 'Forms', fieldName: 'Call_Type__c'},
    { label: 'Others', fieldName: 'Call_Type__c'},
];

export default class CaseHistoryLWC extends LightningElement {
    @wire(MessageContext) messageContext;
    @wire(getObject) wiredCases;

    get data() {
        if (this.wiredCases.data) {
            return this.wiredCases.data.map(row => ({
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate
            }));
        }
        return [];
    }

    get columns() {
        return columns;
    }

    connectedCallback() {
        // ... existing code
    }

    sendMessage() {
        // ... existing code
    }

    isInquiry(callActivity) {
        return callActivity === 'Inquiry';
    }
  }
