Ext.define('BrazilJS.view.device.List' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.devicelist',
    loadMask: true,
    autoheight: true,
    //requires: ['Ext.toolbar.Paging'],
    
    iconCls: 'icon-grid',

    title : 'Devices',
    store: 'Devices',

    columns: [
              {
    	header: "Device-Id",
		width: 150,
		flex:1,
		dataIndex: 'id'
	},
	{
		header: "Device UUID",
		width: 150,
		flex:1,
		dataIndex: 'uuid'
	},
	{
		header: "Phone #",
		width: 150,
		flex:1,
		dataIndex: 'phoneNumber'
	}
	,{
		header: "Carrier",
		width: 150,
		flex:1,
		dataIndex: 'carrier'
	},
	{
		header: "Retired",
		width: 150,
		flex:1,
		dataIndex: 'retired'
	},
	{
		header: "Roaming",
		width: 150,
		flex:1,
		dataIndex: 'roaming'
	},
	{
		header: "MI Client Id",
		width: 150,
		flex:1,
		dataIndex: 'clientId'
	},
	{
		header: "Model",
		width: 150,
		flex:1,
		dataIndex: 'model'
	},
	{
		header: "Manufacturer",
		width: 150,
		flex:1,
		dataIndex: 'manufacturer'
	}
	],
	
	initComponent: function() {
		
		this.dockedItems = [{
            xtype: 'toolbar',
            dock: 'top',
            displayInfo: true,
            items: [{
                iconCls: 'icon-save',
                itemId: 'add',
                text: 'Add',
                action: 'add'
            },{
                iconCls: 'icon-delete',
                text: 'Delete',
                action: 'delete'
            }]
        },
        {
            xtype: 'pagingtoolbar',
            dock:'bottom',
            store: 'Devices',
            displayInfo: true,
            displayMsg: 'Displaying Devices {0} - {1} of {2}',
            emptyMsg: "No Devices to display"
        }];
		
		this.callParent(arguments);
	}
});
