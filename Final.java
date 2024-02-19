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
