Ext.define('BrazilJS.store.Devices', {
    extend: 'Ext.data.Store',
    model: 'BrazilJS.model.Device',
    autoLoad: true,
    pageSize: 20,
    autoLoad: {start: 0, limit: 20},
    
    proxy: {
        type: 'ajax',
        api: {
        	read : 'device/view.action',
            create : 'device/create.action',
            update: 'device/update.action',
            destroy: 'device/delete.action'
        },
        reader: {
            type: 'json',
            root: 'data',
            successProperty: 'success'
        },
        writer: {
            type: 'json',
            writeAllFields: true,
            encode: false,
            root: 'data'
        },
        listeners: {
            exception: function(proxy, response, operation){
                Ext.MessageBox.show({
                    title: 'REMOTE EXCEPTION',
                    msg: operation.getError(),
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.Msg.OK
                });
            }
        }
    }
});