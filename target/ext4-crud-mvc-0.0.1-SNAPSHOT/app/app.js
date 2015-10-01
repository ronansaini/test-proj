/**
 * Ext JS Library 4.0.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 *	
 * Sample project presented at BrazilJS
 * Brazilian JavaScript Conference
 * Fortaleza - Cear� - 13-14 May 2011
 * http://braziljs.com.br/2011
 * 
 * @author Loiane Groner
 * http://loianegroner.com (English)
 * http://loiane.com (Portuguese)
 */
Ext.application({
    name: 'BrazilJS',

   
    controllers: [
        'Contacts','Devices'
    ],

    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'border',
            renderTo: Ext.getBody(),
            
            items: [
                 {
                	
            	    region: 'center',
            	    xtype: 'tabpanel',
            	  //  html: 'Center',
            	    align : 'center',
            	    	activeTab: 0,
            	    	   items: [{
            	    	   title: 'Contacts',
            	    	//   html: 'Center',
            	    	   xtype: 'contactlist'   
            	    	   },
            	    	   {
            	    		   title: 'Users',
            	    		   html: 'Users Info',
            	    	//	   xtype: 'contactlist',
            	    		  
            	    		      
            	    		},
             	    	   {
             	    		   title: 'Devices',
             	    		   html: 'Device Info',
             	    		   xtype: 'devicelist',
             	    		}
            	    	   ]
            	    }
          //  Original : Remove all the above & This just gets the contact list & renders it on the main page.	  {
          //            xtype: 'contactlist'
          //        }
            ]
        });
        var tabs=Ext.create('Ext.tab.Panel', {
            items: [
                {
                    title: 'Users',
                    html : 'A simple tab'
                },
                {
                    title: 'Devices',
                    html : 'Another one'
                },
                {
                    title: 'Contact',
                    html : 'Another one'
                }
               
            ],
            renderTo : Ext.getBody()        
           
        })
    }
});
