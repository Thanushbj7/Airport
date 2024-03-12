<template>
    <div class="slds-card" style="height: 400px; width: 610px">
        <lightning-card title="Targeted Messages" icon-name="standard:case">
            <h2 slot="title">
                <lightning-icon icon-name="action:new_note" size="small" title="Targeted Message"></lightning-icon>
                <b>Targeted Message</b>
            </h2>
            <div class="slds-p-horizontal_xx-small">
                <lightning-tabset>
                    <lightning-tab label="Available Targeted Messages">
                        <div class="demo-only demo-only--sizing slds-grid slds-wrap">
                            <!-- Your content here -->
                        </div>
                        <div style="height: 200px;">
                            <div class="slds-m-top_small slds-m-horizontal_x-small">
                                <c-clickable-mess-nam key-field="Id" data={data} columns={columns}></c-clickable-mess-nam>
                            </div>
                        </div>
                    </lightning-tab>
                    <lightning-tab label="Detail" if:true={isDetailTabVisible}>
                        <div class="slds-p-horizontal_xx-small">
                            <!-- Your detail tab content here -->
                        </div>
                    </lightning-tab>
                </lightning-tabset>
            </div>
        </lightning-card>
    </div>
</template>







// ... (your existing code)

export default class CTargetedMessage extends LightningElement {
    // ... (your existing code)

    handleOfferNameClick(event) {
        // Prevent the default behavior of the click event (opening the URL)
        event.preventDefault();

        // Set a property to indicate that the detail tab should be visible
        this.isDetailTabVisible = true;

        // Dynamically create a new tab
        this.createNewTab('Detail', 'Detail Tab Label');
    }

    createNewTab(tabName, tabLabel) {
        const tabset = this.template.querySelector('lightning-tabset');

        if (tabset) {
            const newTab = document.createElement('lightning-tab');
            newTab.label = tabLabel;
            newTab.key = tabName;

            // You can add additional content or components to the new tab as needed

            // Append the new tab to the existing tabset
            tabset.appendChild(newTab);
        }
    }

    // ... (your existing code)
}













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
                                
                                <!--  <template if:true={isDetailTabVisible}> -->
                                <lightning-tab  label="Detail" >
                                        <div class="slds-p-horizontal_xx-small">
                                               
                                                                
              <div class="demo-only demo-only--sizing slds-grid slds-wrap"> 
                        <div class="slds-size_1-of-3">
                          <div class="slds-m-horizontal_x-small">
                                 <lightning-combobox name="Owner" label="Owner" value={Owner} 
                                     onchange={handleOwner} required="true"></lightning-combobox>
                                                <!--   <div class="error">{}</div>-->
                                                      </div>
                                                                </div>
                                                                     <div class="slds-size_1-of-3">
                                        <div class="slds-m-horizontal_x-small">
                                                <lightning-combobox name="Response"
                                                label="Response"
                                                value="value"
                                                placeholder="-Select-"
                                                options={Response}
                                                onchange={handleChangee}></lightning-combobox>
                                <!--   <div class="error">{}</div>-->
                        </div>
                                        </div>
                        <div class="slds-size_1-of-3">
                                                        <div class="slds-m-horizontal_x-small">
                                        <lightning-combobox name="ResponseReason"
                                        label="Response Reason"
                                        value="value"
                                        placeholder="-Select-"
                                        options={pickListValues}
                                        onchange={handleChange}></lightning-combobox>

                                         </div>
                                                </div>
                                                        
                                                                                                
                                             </div>
                                                                        

                                                               
                                                </div>
                                                <div class="slds-publisher slds-is-active">
                                                        <label for="comment-text-input2" class="slds-publisher__toggle-visibility slds-m-bottom_large">
                                                        Message Comments</label>
                                                        
                                                        
                                                        
                                                        
                                                                <div class="slds-box" >
                                                                       
                                                                        </div>
                                                        
                                                       
                                                       
                                                        <button class="slds-button slds-button_brand">Save</button>
                                                        
                                                        </div>
                                </lightning-tab>
                                <!--    </template>-->



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
const pickListFields = [RESPONSE_REASON, RESPONSE];
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
            fieldApiName: RESPONSE_REASON
                //RESPONSE
        })
        //get Response() {
        //  return getPicklistValues(this.pickListValues.data, RESPONSE_REASON);
        //}
        //get ResponseReason() {
        //  return getPicklistValues(this.responsePickListValues.data, RESPONSE);
        //}


    wiredPickListValue({ data, error }) {
        if (data) {
            console.log(`Picklist values are ${data.values}`);

            this.pickListValues = data.values;
            this.responsePickListValues = data.values;
            this.error = undefined;
        }
        if (error) {
            console.log(` Error while fetching Picklist values  ${error}`);
            this.error = error;
            this.pickListValues = undefined;
            this.responsePickListValues = undefined;
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
    // @wire(getPicklistValues, {
    // recordTypeId: '012000000000000BBB',
    // fieldApiName: RESPONSE
    //RESPONSE
    // })
    //wiredPickListValue({ data, error }) {
    //if (data) {
    // console.log(`Picklist values are ${data.values}`);
    //  this.responsePickListValues = data.values;
    //this.responsePickListValues = data.values;
    // this.error = undefined;
    // }
    //if (error) {
    //console.log(` Error while fetching Picklist values  ${error}`);
    //  this.error = error;
    // this.responsePickListValues = undefined;
    //this.responsePickListValues = undefined;
    // }
    // }
    //handleChangee() {

    //}

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
