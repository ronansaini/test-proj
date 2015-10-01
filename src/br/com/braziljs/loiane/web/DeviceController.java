package br.com.braziljs.loiane.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.braziljs.loiane.dao.DeviceDAO;
import br.com.braziljs.loiane.model.Contact;
import br.com.braziljs.loiane.model.ContactWrapper;
import br.com.braziljs.loiane.model.DeviceDTO;
import br.com.braziljs.loiane.model.DeviceWrapper;
import br.com.braziljs.loiane.model.MIDevice;
import br.com.braziljs.loiane.model.MIUser;
import br.com.braziljs.loiane.service.ContactService;
import br.com.braziljs.loiane.service.DeviceService;
import br.com.braziljs.loiane.util.CommonUtil;
import br.com.braziljs.loiane.util.ExtJSReturn;

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
public class DeviceController {

	 private static final Log log = LogFactory.getLog(DeviceController.class);
	 @Autowired
	private DeviceService deviceService;
	 
	 @Value (value =  "${dev.app.jdbc.driver}") 
	 private  String jdbcDriver;
	 
	 @Value(value = "${ios.nonce.valid.time}")
	 private  long VALID_TIME;
	 
	@Autowired
	private DeviceDAO deviceRepository;
	
	@RequestMapping(value="/device/view.action")
	public @ResponseBody Map<String,? extends Object> view(@RequestParam int start, @RequestParam int limit) throws Exception {

		try{
			
			System.out.println("JDBC Driver : "+jdbcDriver);
			System.out.println("VALID_TIME : "+VALID_TIME);
			CommonUtil.print();
			
			List<MIDevice> devices = deviceService.getDeviceList(start,limit);
			
			int total = deviceService.getTotalDevices();
			System.out.println("total"+total);
			
			final MIDevice	device = deviceRepository.findByDeviceId(5L);
			System.out.println(device);
			log.debug("Device: "+device);

			return ExtJSReturn.mapDeviceOK(devices, total);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error retrieving Devices from database.");
		}
	}
	

	
	
	@RequestMapping(value="/device/create.action")
	public @ResponseBody Map<String,? extends Object> create(@RequestBody DeviceWrapper data) throws Exception {

		try{

			List<DeviceDTO> devices = deviceService.create(data.getData());

			return ExtJSReturn.mapDeviceOK(devices);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to create device.");
		}
	}
	

	@RequestMapping(value="/device/update.action")
	public @ResponseBody Map<String,? extends Object> update(@RequestBody DeviceWrapper data) throws Exception {
		try{

			List<DeviceDTO> devices = deviceService.update(data.getData());

			return ExtJSReturn.mapDeviceOK(devices);

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to update contact.");
		}
	}
	
	@RequestMapping(value="/device/delete.action")
	public @ResponseBody Map<String,? extends Object> delete(@RequestBody DeviceWrapper data) throws Exception {
		
		try{
			
			deviceService.delete(data.getData());

			Map<String,Object> modelMap = new HashMap<String,Object>();
			modelMap.put("success", true);

			return modelMap;

		} catch (Exception e) {

			return ExtJSReturn.mapError("Error trying to delete contact.");
		}
	}
	

	

}
