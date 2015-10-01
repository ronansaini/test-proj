Ext.define('BrazilJS.view.device.Edit', {
    extend: 'Ext.window.Window',
    alias : 'widget.deviceedit',

    requires: ['Ext.form.Panel','Ext.form.field.Text'],

    title : 'Edit Device',
    layout: 'fit',
    autoShow: true,
    width: 280,
    
    iconCls: 'icon-user',

    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                padding: '5 5 0 5',
                border: false,
                style: 'background-color: #fff;',
                
                fieldDefaults: {
                    anchor: '100%',
                    labelAlign: 'left',
                    allowBlank: false,
                    combineErrors: true,
                    msgTarget: 'side'
                },

                items: [
					{
					    xtype: 'textfield',
					    name : 'id',
					    fieldLabel: 'id',
					    hidden:true
					},
					{
					    xtype: 'textfield',
					    name : 'uuid',
					    fieldLabel: 'uuid',
					    hidden:true
					}, 
                    {
                        xtype: 'textfield',
                        name : 'phoneNumber',
                        fieldLabel: 'PhoneNumber'
                    },
                    {
                        xtype: 'textfield',
                        name : 'carrier',
                        fieldLabel: 'Carrier'
                    },
                    {
                        xtype: 'textfield',
                        name : 'retired',
                        fieldLabel: 'Retired'
                        	
                    },
                    {
                        xtype: 'textfield',
                        name : 'roaming',
                        fieldLabel: 'Roaming'
                        	
                    },
                    {
                        xtype: 'textfield',
                        name : 'clientId',
                        fieldLabel: 'MI-ClientId'
                        	
                    },
                    {
                        xtype: 'textfield',
                        name : 'model',
                        fieldLabel: 'Model'
                        	
                    },
                    {
                        xtype: 'textfield',
                        name : 'manufacturer',
                        fieldLabel: 'Manufacturer'
                        	
                    }
                ]
            }
        ];
        
        this.dockedItems = [{
            xtype: 'toolbar',
            dock: 'bottom',
            id:'buttons',
            ui: 'footer',
            items: ['->', {
                iconCls: 'icon-save',
                itemId: 'save',
                text: 'Save',
                action: 'save'
            },{
                iconCls: 'icon-reset',
                text: 'Cancel',
                scope: this,
                handler: this.close
            }]
        }];

        this.callParent(arguments);
    }
});
