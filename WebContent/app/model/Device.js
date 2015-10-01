Ext.define('BrazilJS.model.Device', {
    extend: 'Ext.data.Model',
    fields: ['id','uuid','phoneNumber','carrier','roaming','retired','clientId','model','manufacturer']
});