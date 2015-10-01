package br.com.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import br.com.braziljs.loiane.dao.DeviceDAO;


//import br.com.braziljs.loiane.model.MIDevice;
//import br.com.braziljs.loiane.model.MIUser;

import br.com.model.Contact;
import br.com.model.ContactWrapper;
import br.com.service.ContactService;
import br.com.util.ExtJSReturn;

/**
 * Controller - Spring
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
@Controller
public class ContactController  {

	 private static final Log log = LogFactory.getLog(ContactController.class);
	
	private ContactService contactService;
	@Autowired
	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}
	
	/*@Autowired
	private DeviceDAO deviceRepository;*/
	
	@RequestMapping(value="/contact/view.action")
	public @ResponseBody Map<String,? extends Object> view(@RequestParam int start, @RequestParam int limit) throws Exception {

		try{

			List<Contact> contacts = contactService.getContactList(start,limit);
			
			int total = contactService.getTotalContacts();
			
		/*	final MIDevice	device = deviceRepository.findByDeviceId(1L);
			System.out.println(device);
			log.debug("Device: "+device);*/

			return ExtJSReturn.mapOK(contacts, total);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error retrieving Contacts from database.");
		}
	}
	
	@RequestMapping(value="/contact/create.action")
	public @ResponseBody Map<String,? extends Object> create(@RequestBody ContactWrapper data) throws Exception {

		try{

			List<Contact> contacts = contactService.create(data.getData());

			return ExtJSReturn.mapOK(contacts);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to create contact.");
		}
	}
	
	@RequestMapping(value="/contact/update.action")
	public @ResponseBody Map<String,? extends Object> update(@RequestBody ContactWrapper data) throws Exception {
		try{

			List<Contact> contacts = contactService.update(data.getData());

			return ExtJSReturn.mapOK(contacts);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to update contact.");
		}
	}
	
	@RequestMapping(value="/contact/delete.action")
	public @ResponseBody Map<String,? extends Object> delete(@RequestBody ContactWrapper data) throws Exception {
		
		try{
			
			contactService.delete(data.getData());

			Map<String,Object> modelMap = new HashMap<String,Object>(3);
			modelMap.put("success", true);

			return modelMap;

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to delete contact.");
		}
	}
	

	

}
