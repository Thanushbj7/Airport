
<template>
    <!-- ... (your existing code) ... -->

    <lightning-card title="CASE HISTORY" icon-name="standard:add_contact">
        <div class="slds-col slds-col--padded slds-p-top--large">
            <div style="float: left; width: 20%; padding: 10px;">
                <h1>{pageTitle}</h1>
            </div>
            <div style="float: left; width: 80%; padding: 10px;">
                <table style="width: 100%; border-collapse: collapse;">
                    <thead>
                        <tr>
                            <th style="border: 1px solid #ddd; padding: 8px;">Case Number</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Date Time</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Plan ID</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Inquiry</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Transactions</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Account Maintenance</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Forms</th>
                            <th style="border: 1px solid #ddd; padding: 8px;">Other</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template for:each={cases} for:item="caseRecord">
                            <tr key={caseRecord.Id}>
                                <td style="border: 1px solid #ddd; padding: 8px;">{caseRecord.Case__r.CaseNumber}</td>
                                <td style="border: 1px solid #ddd; padding: 8px;">{caseRecord.Case__r.CreatedDate}</td>
                                <td style="border: 1px solid #ddd; padding: 8px;">{caseRecord.PlanID_Text__c}</td>
                                <td style="border: 1px solid #ddd; padding: 8px;">
                                    <template if:true={isInquiry(caseRecord.Call_Activity__c)}>
                                        {caseRecord.Call_Type__c}
                                    </template>
                                </td>
                                <!-- Repeat the pattern for other columns -->
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
        </div>
    </lightning-card>

    <!-- ... (rest of your code) ... -->
</template>

<script>
import { LightningElement, api, track } from 'lwc';

export default class YourComponent extends LightningElement {
    @api cases;
    @track pageTitle = "CASE HISTORY";

    isInquiry(callActivity) {
        return callActivity === 'Inquiry';
    }
}
</script>
