<template>
    <div class="slds-card" style="height:400px; width:610px">
            <lightning-card title="Targeted Messages" icon-name="standard:case">

                    <h2 slot="title">
                    <lightning-icon icon-name="action:new_note"   size="small" title=" Targeted Message "></lightning-icon>
                    <b>Targeted Message</b>
            </h2>
                    

                    <div class="slds-p-horizontal_xx-small">
                            <lightning-tabset>
                                    <lightning-tab label="Available Targeted Messages">
                                            <div class="demo-only demo-only--sizing slds-grid slds-wrap"> 
                                                    
                                            </div>
                                            <div style="height: 200px;"> <div class="slds-m-top_small slds-m-horizontal_x-small">
                                                <c-clickable-mess-nam
                                                     key-field="Id"
                                                     data={data}
                                                     columns={columns}>
                                                </c-clickable-mess-nam>
                                            </div> 
                                            </div>
                                    </lightning-tab>
                                    <lightning-tab label="Detail">

                                    </lightning-tab>


                                    
                            </lightning-tabset>
                    </div>
            </lightning-card>
    </div>

    
</template>







                                                         import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';
import initClientOffers from '@salesforce/apex/TargetedMessage.initClientOffers';
const columns = [
    //{ label: 'PlanId', fieldName: 'linkName', type:'url' ,typeAttributes: { label: {fieldName: 'caseNumber'}, target:'_blank'}},
    //{ label: 'Date', fieldName: 'createdDate' },
    { label: 'PlanId', fieldName: 'offerPlanId' },
    { label: 'Plan Name', fieldName: 'offerPlanName', columnKey: 'tra', type: 'url', typeAttributes: { label: { fieldName: 'offerPlanName' } } },
    { label: 'Active Mailer', fieldName: 'activeMailer', columnKey: 'accM' },
    { label: 'Message Name', fieldName: 'offerName', type: 'url', typeAttributes: { label: { fieldName: 'offerName' } } },



];

export default class CTargetedMessage extends LightningElement {
    isvisible=false;
    @track data = [];
    @track columns = columns;
    wiredRecords;
    handleclick(){
        this.isvisible=true;
    }
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
