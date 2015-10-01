Ext.define('BrazilJS.controller.Devices', {
    extend: 'Ext.app.Controller',

    stores: ['Devices'],

    models: ['Device'],

    views: ['device.Edit', 'device.List'],

    refs: [{
            ref: 'devicesPanel',
            selector: 'panel'
        },{
            ref: 'devicelist',
            selector: 'devicelist'
        }
    ],

    init: function() {
        this.control({
            'devicelist dataview': {
                itemdblclick: this.editDevice
            },
            'devicelist button[action=add]': {
            	click: this.editDevice
            },
            'devicelist button[action=delete]': {
                click: this.deleteDevice
            },
            'deviceedit button[action=save]': {
                click: this.updateDevice
            }
        });
    },

    editDevice: function(grid, record) {
        var edit = Ext.create('BrazilJS.view.device.Edit').show();
        
        if(record){
        	edit.down('form').loadRecord(record);
        }
    },
    
    updateDevice: function(button) {
        var win    = button.up('window'),
            form   = win.down('form'),
            record = form.getRecord(),
            values = form.getValues();
        
        
		if (values.id > 0){
			record.set(values);
		} else{
			record = Ext.create('BrazilJS.model.Device');
			record.set(values);
			record.setId(0);
			this.getDevicesStore().add(record);
		}
        
		win.close();
        this.getDevicesStore().sync();
    },
    
    deleteDevice: function(button) {
    	
    	var grid = this.getDevicelist(),
    	record = grid.getSelectionModel().getSelection(), 
        store = this.getDevicesStore();

	    store.remove(record);
	    this.getDevicesStore().sync();
    }
});
