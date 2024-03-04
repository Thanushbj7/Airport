import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';
const columns = [
    //{ label: 'PlanId', fieldName: 'linkName', type:'url' ,typeAttributes: { label: {fieldName: 'caseNumber'}, target:'_blank'}},
    //{ label: 'Date', fieldName: 'createdDate' },
    { label: 'PlanId', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', type: 'clickmess', typeAttributes: { tmMessName: { fieldName: 'offerName' } } },



];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;
    connectedCallback() {
        // Parse the URL and get the 'passedValue' parameter				 
        const urlParams = new URLSearchParams(window.location.search);
        this.clientSSN = urlParams.get('clientSSN');
        console.log('ssn should appear here' + this.clientSSN);
        console.log('inside connected callback');
    }



    @wire(initClientOffers, { clientSSN: '$clientSSN' }) wiredCases(value) {
        //console.log("Columns stringified 1st  --" , this.columns);
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            this.data = data;

        }

    }
}








import { LightningElement, wire, track } from 'lwc';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';

const columns = [
    { label: 'Plan Id', fieldName: 'planId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', columnKey: 'inq' },
];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;

    @wire(initClientOffers)
    wiredCases({ data, error }) {
        if (data) {
            // Assuming data is an array of objects with properties planId, offerPlanName, activeMailer, and offerName
            this.data = data.map(row => ({
                planId: row.planId,
                offerPlanName: row.offerPlanName,
                activeMailer: row.activeMailer,
                offerName: row.offerName,
            }));
        }
        if (error) {
            console.error("Error Occurred!", error);
        }
    }
}










import { LightningElement, track, wire } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';

const columns = [
    { label: 'Plan Id', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', columnKey: 'inq' },
];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(initClientOffers)
    wiredCases(result) {
        this.wiredRecords = result;
        const { data, error } = result;
        if (data) {
            // Assuming your Apex method returns a List<vfClientOffer>
            this.data = data.map(row => ({
                offerPlanId: row.offerPlanId,
                offerPlanName: row.offerPlanName,
                activeMailer: row.activeMailer,
                offerName: row.offerName,
            }));
        } else if (error) {
            console.error('Error fetching data:', error);
        }
    }

    // You can add refresh functionality if needed
    handleRefresh() {
        return refreshApex(this.wiredRecords);
    }
}





import { LightningElement, track, wire } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';

const columns = [
    { label: 'Plan Id', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', columnKey: 'inq' },
];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(initClientOffers)
    wiredCases(result) {
        this.wiredRecords = result;
        const { data, error } = result;
        if (data) {
            // Assuming your Apex method returns a List<vfClientOffer>
            this.data = data;
        } else if (error) {
            console.error('Error fetching data:', error);
        }
    }

    // You can add refresh functionality if needed
    handleRefresh() {
        return refreshApex(this.wiredRecords);
    }
}







public with sharing class TargetedMessage {
     ///Code for Targeted message
       //
        @AuraEnabled(cacheable=true)
       public static List<VfClientOffer> initClientOffers(String clientSSN ) {
           
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
           public String activeMailer {get; set;}
       }
       
       
   }











import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';
const columns = [
    //{ label: 'PlanId', fieldName: 'linkName', type:'url' ,typeAttributes: { label: {fieldName: 'caseNumber'}, target:'_blank'}},
    //{ label: 'Date', fieldName: 'createdDate' },
    { label: 'Plan Id', fieldName: 'planId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', columnKey: 'inq' },



];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;
    @wire(initClientOffers) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            let tempRecords = JSON.parse(JSON.stringify(data));
            tempRecords = tempRecords.map(row => {

            })
            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }
        if (error) {
            console.log("error Occurred!", error);
        }
    }


}





import { LightningElement, track, wire } from 'lwc';
import { refreshApex } from '@salesforce/apex';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';

const columns = [
    { label: 'Plan Id', fieldName: 'planId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', columnKey: 'inq' },
];

export default class CTargetedMessage extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(initClientOffers)
    wiredCases(result) {
        this.wiredRecords = result;
        const { data, error } = result;
        if (data) {
            // Assuming your Apex method returns an array of vfClientOffer objects
            this.data = data.map(row => ({
                planId: row.offerPlanId,
                offerPlanName: row.offerPlanName,
                activeMailer: row.activeMailer,
                offerName: row.offerName,
            }));
        } else if (error) {
            console.error('Error fetching data:', error);
        }
    }

    // You can add refresh functionality if needed
    handleRefresh() {
        return refreshApex(this.wiredRecords);
    }
}
