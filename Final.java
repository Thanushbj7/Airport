import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/caseHistoryLWC.fetchWrapperCases';

const columns = [
    { label: 'Case Number', fieldName: 'caseNumber' },
    { label: 'Date', fieldName: 'createdDate' },
    { label: 'Plan Id', fieldName: 'planId' },
    { label: 'Inquiry', fieldName: 'callTypeInquiry', columnKey: 'inq' },
    { label: 'Transactions', fieldName: 'callTypeTransaction', columnKey: 'tra' },
    { label: 'Account Maintenance', fieldName: 'callTypeAccountMaintenance', columnKey: 'accM' },
    { label: 'Forms', fieldName: 'callTypeForms', columnKey: 'for' },
    { label: 'Others', fieldName: 'callTypeOthers', columnKey: 'oth' },
];

export default class newLWC extends LightningElement {
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
        //{SSN:this.clientSSN}

    @wire(fetchWrapperCases) wiredCases(value) {
        //console.log("Columns stringified 1st  --" , this.columns);
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {

            let tempRecords = JSON.parse(JSON.stringify(data));
            let uniqueCaseNumbers = new Set();

            let uniqueCallTypesMap = new Map();

            function groupBy(objectArray, property) {
                return objectArray.reduce(function(acc, obj) {
                    var key = obj[property];
                    if (!acc[key]) {
                        acc[key] = [];
                    }
                    acc[key].push(obj);
                    return acc;
                }, {});
            }
            var groupedPlanIds;
            var groupedCaseNumbers = groupBy(tempRecords, 'caseNumber');
            let final = [];
            let val = Object.values(groupedCaseNumbers);
            console.log("Object.values--", val);
            console.log("groupedCaseNumbers!", groupedCaseNumbers);
            let cak = [];
            let copiedarr = [];
            for (let i = 0; i < val.length; i++) {

                //	console.log("Object.values for palnId--" , val[i]);
                groupedPlanIds = groupBy(val[i], 'planId')
                let planVal = Object.values(groupedPlanIds)
                console.log("Object.values for grouped PlanIds--", planVal);

                for (let j = 0; j < planVal.length; j++) {
                    let uniquePlanIds = new Set();
                    let sObjectArray = [];
                    const filteredCaseNumber = [];
                    const filteredCreatedDate = [];
                    const filteredPlanId = [];
                    const filteredCallTypeInquiry = [];
                    const filteredCallTypeTransaction = [];
                    const filteredCallTypeAccountMaintenance = [];
                    const filteredCallTypeForms = [];
                    const filteredCallTypeOthers = [];
                    console.log("planVal[j]--", planVal[j]);
                    planVal[j].forEach(sObject => {
                        filteredCaseNumber.push(sObject.caseNumber);
                        filteredCreatedDate.push(sObject.createdDate);
                        filteredPlanId.push(sObject.planId);
                        if (sObject.callTypeInquiry) {
                            filteredCallTypeInquiry.push(sObject.callTypeInquiry);
                        }
                        if (sObject.callTypeTransaction) {
                            filteredCallTypeTransaction.push(sObject.callTypeTransaction);
                        }
                        if (sObject.callTypeAccountMaintenance) {
                            filteredCallTypeAccountMaintenance.push(sObject.callTypeAccountMaintenance);
                        }
                        if (sObject.callTypeForms) {
                            filteredCallTypeForms.push(sObject.callTypeForms);
                        }
                        if (sObject.callTypeOthers) {
                            filteredCallTypeOthers.push(sObject.callTypeOthers);
                        }
                    });
                    /*	console.log("filteredCaseNumber--" ,filteredCaseNumber);
                    	console.log("filteredCreatedDate--" ,filteredCreatedDate);
                    	console.log("filteredPlanId--" ,filteredPlanId);
                    	console.log("filteredCallTypeInquiry--" ,filteredCallTypeInquiry);
                    	console.log("filteredCallTypeTransaction--" ,filteredCallTypeTransaction);
                    	console.log("filteredCallTypeAccountMaintenance--" ,filteredCallTypeAccountMaintenance);
                    	console.log("filteredCallTypeForms--" ,filteredCallTypeForms);
                    	console.log("filteredCallTypeOthers--" ,filteredCallTypeOthers);*/
                    var arrLength = filteredCaseNumber.length;
                    console.log("arrLength--", arrLength);
                    for (let i = 0; i < arrLength; i++) {
                        let title = {};
                        title.caseNumber = filteredCaseNumber[i];
                        title.createdDate = filteredCreatedDate[i];
                        title.planId = filteredPlanId[i];
                        title.callTypeInquiry = filteredCallTypeInquiry[i];
                        title.callTypeTransaction = filteredCallTypeTransaction[i];
                        title.callTypeAccountMaintenance = filteredCallTypeAccountMaintenance[i];
                        title.callTypeForms = filteredCallTypeForms[i];
                        title.callTypeOthers = filteredCallTypeOthers[i];
                        sObjectArray.push(title);
                        //		console.log("title--" ,title);
                    }
                    console.log("sObjectArray--", sObjectArray);
                    cak = sObjectArray.map(row => {
                        if (!uniquePlanIds.has(row.planId)) {
                            uniquePlanIds.add(row.planId);
                            return {...row, caseNumber: row.caseNumber, createdDate: row.createdDate };
                            //		copiedarr.push(cak);
                        }
                        return {...row, caseNumber: null, createdDate: null, planId: null };
                        //copiedarr.push(cak);
                        //console.log("copiedarr--" ,copiedarr);
                    });


                    copiedarr.push(cak);

                }

            }
            console.log("copiedarr--", copiedarr);

            const flattenedArr = copiedarr.flat(1);
            console.log("flattenedArr--", flattenedArr);
            let kk = flattenedArr.filter(sObj => {
                if (sObj.callTypeInquiry != null || sObj.callTypeTransaction != null || sObj.callTypeAccountMaintenance != null || sObj.callTypeForms != null || sObj.callTypeOthers != null) {
                    return sObj;
                }
            });
            this.data = kk;

            console.log("kk!", kk);
        }

        if (error) {
            console.log("error Occurred!", error);
        }

    }

}













<template>
    <div style="height: 300px;">
        <h2>
            <lightning-icon icon-name="action:new_note" title=" Case History"></lightning-icon>
            <b>Case History</b>
        </h2>

        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <template for:each={columns} for:item="col">
                        <th key={col.label}>{col.label}</th>
                    </template>
                </tr>
            </thead>
            <tbody>
                <template for:each={data} for:item="item" for:index="index">
                    <tr key={item.Id}>
                        <td>{index + rowOffset}</td>
                        <template for:each={columns} for:item="col">
                            <td key={col.field}>
                                {makeFieldClickable(item[col.field], col.field)}
                            </td>
                        </template>
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>

<script>
import { LightningElement } from 'lwc';

export default class YourComponent extends LightningElement {
    clickableFields = ['Field1', 'Field2']; // Add the fields you want to make clickable

    makeFieldClickable(value, field) {
        // Check if the field is in the clickableFields array
        if (this.clickableFields.includes(field)) {
            return `<a href="${value}">${value}</a>`;
        }
        return value;
    }
}
</script>








<template>
    <div style="height: 300px;">
        <h2>
            <lightning-icon icon-name="action:new_note" title=" Case History"></lightning-icon>
            <b>Case History</b>
        </h2>

        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <template for:each={columns} for:item="col">
                        <th key={col.label}>{col.label}</th>
                    </template>
                </tr>
            </thead>
            <tbody>
                <template for:each={data} for:item="item" for:index="index">
                    <tr key={item.Id}>
                        <td>{index + rowOffset}</td>
                        <template for:each={columns} for:item="col">
                            <td key={col.field}>{item[col.field]}</td>
                        </template>
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>






<template>
    <div style="height: 300px;">
        <h2> 
            <lightning-icon icon-name="action:new_note" title=" Case History"></lightning-icon>
            <b>Case History</b>
        </h2>

        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Column 1</th>
                    <th>Column 2</th>
                    <!-- Add more columns as needed -->
                </tr>
            </thead>
            <tbody>
                <template for:each={data} for:item="item" for:index="index">
                    <tr key={item.Id}>
                        <td>{index + rowOffset}</td>
                        <td>{item.Column1}</td>
                        <td>{item.Column2}</td>
                        <!-- Repeat for additional columns -->
                    </tr>
                </template>
            </tbody>
        </table>
    </div>
</template>








<template>
    <div style="height: 300px;">
            <h2 slot="title">
                    <lightning-icon icon-name="action:new_note"  title=" Case History"></lightning-icon>
                    <b>Case History</b>
            </h2>

            <lightning-datatable
                                                     data={data}
                                                     columns={columns}
                                                     key-field="Id"
                                                     show-row-number-column
                                                     row-number-offset={rowOffset}
                                                     hide-checkbox-column      
                                                     >
            </lightning-datatable>
    </div>
 
</template>
