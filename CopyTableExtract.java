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
