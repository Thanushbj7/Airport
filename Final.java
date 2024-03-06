// Import the schema for the Offer_Response__c field
import OFFER_RESPONSE from '@salesforce/schema/Opportunity.Offer_Response__c';

// ...

export default class CTargetedMessage extends LightningElement {

    // ...

    @wire(getPicklistValues, {
        recordTypeId: '012000000000000AAA', // Replace with your actual record type Id
        fieldApiName: RESPONSE,
    })
    wiredResponsePickListValue({ data, error }) {
        if (data) {
            console.log(`Response Picklist values are ${data.values}`);
            this.responsePickListValues = data.values;
            this.error = undefined;
        }
        if (error) {
            console.log(`Error while fetching Response Picklist values: ${error}`);
            this.error = error;
            this.responsePickListValues = undefined;
        }
    }

    // ...
}










import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';
import getLightningOppController from '@salesforce/apex/LightningOppController.getLightningOppController';
import { getPicklistValues } from 'lightning/uiObjectInfoApi';
import RESPONSE_REASON from '@salesforce/schema/Opportunity.Offer_Response_Reason__c';
import RESPONSE from '@salesforce/schema/Opportunity.Offer_Response__c';
const columns = [
    //{ label: 'PlanId', fieldName: 'linkName', type:'url' ,typeAttributes: { label: {fieldName: 'caseNumber'}, target:'_blank'}},
    //{ label: 'Date', fieldName: 'createdDate' },
    { label: 'PlanId', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra' },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', type: 'url', typeAttributes: { label: { fieldName: 'offerName' } } },



];
const fields = [
    { label: 'Owner', fieldName: 'OwnerId' },
    { label: 'Response', fieldName: 'Offer_Response__c' },
    { label: 'Response Reason', fieldName: 'Offer_Response_Reason__c' },
];

export default class CTargetedMessage extends LightningElement {

    isDetailTabVisible = false;
    @track data = [];
    @track columns = columns;
    @track fields = fields;
    Owner;
    Response;
    ResponseReason;
    wiredRecords;
    @track Responses;
    @track pickListValues;
    @track responsePickListValues;
    @track error;
    @track values;

    @track ResponseReasons;
    Rvalue = '';
    RRvalue = '';



    @wire(getPicklistValues, {
        recordTypeId: '012000000000000AAA',
        fieldApiName: RESPONSE_REASON,
        //RESPONSE
    })


    wiredPickListValue({ data, error }) {
        if (data) {
            console.log(`Picklist values are ${data.values}`);
            this.pickListValues = data.values;
            //this.responsePickListValues = data.values;
            this.error = undefined;
        }
        if (error) {
            console.log(` Error while fetching Picklist values  ${error}`);
            this.error = error;
            this.pickListValues = undefined;
            //this.responsePickListValues = undefined;
        }
    }
    handleChange() {

    }

    handleResponse(event) {
        this.Rvalue = event.detail.value;
    }
    handleResponseReason(event) {
        this.RRvalue = event.detail.value;
    }
    handleOfferNameClick(event) {
        // Prevent the default behavior of the click event (opening the URL)
        event.preventDefault();

        // Set a property to indicate that the detail tab should be visible
        this.isDetailTabVisible = true;

        // Dynamically create a new tab
        this.createNewTab('Detail', 'Detail Tab Label');
    }

    //createNewTab(tabName, tabLabel) {
    //  const tabset = this.template.querySelector('lightning-tabset');

    ////  if (tabset) {
    //   const newTab = document.createElement('lightning-tab');
    //  newTab.label = tabLabel;
    //  newTab.key = tabName;

    // You can add additional content or components to the new tab as needed

    // Append the new tab to the existing tabset
    //  tabset.appendChild(newTab);
    // }
    //  }

    connectedCallback() {
        // Parse the URL and get the 'passedValue' parameter				 
        const urlParams = new URLSearchParams(window.location.search);
        this.clientSSN = urlParams.get('clientSSN');
        console.log('ssn should appear here' + this.clientSSN);
        console.log('inside connected callback');
    }

    @wire(getLightningOppController) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            console.log(data);
            let tempRecords = JSON.parse(JSON.stringify(data));
            tempRecords = tempRecords.map(row => {
                return {...row, OwnerId: row.Owner, Offer_Response__c: row.Offer_Response__c, Offer_Response_Reason__c: row.Offer_Response_Reason__c };

            });


        }
    }
    handleOwner(event) {
            this.Owner = event.target.value;
        }
        //handleResponse(event) {
        //  this.Response = event.target.value;
        //}
        //handleResponseReason(event) {
        //  this.ResponseReason = event.target.value;
        //}



    @wire(initClientOffers, { clientSSN: '$clientSSN' }) wiredCases(value) {
        //console.log("Columns stringified 1st  --" , this.columns);
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            this.data = data;

        }

    }
}
