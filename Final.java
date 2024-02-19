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
