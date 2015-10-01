package br.com.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dao.ContactDAO;
import br.com.dao.DeviceDAO;
import br.com.model.Contact;
import br.com.model.DeviceDTO;
import br.com.model.MIDevice;

/**
 * Contact Service
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
@Service
public class DeviceService {
	
	private DeviceDAO deviceDAO;

	/**
	 * Spring use - DI
	 * @param contactDAO
	 */
	@Autowired
	public void setContactDAO(DeviceDAO deviceDAO) {
		this.deviceDAO = deviceDAO;
	}
	
	/**
	 * Get all contacts
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<MIDevice> getDeviceList(int start, int limit){
		
		return deviceDAO.getDevices(start, limit);
	}
	
	/**
	 * Create new Contact/Contacts
	 * @param data - json data from request
	 * @return created contacts
	 */
	@Transactional
	public List<DeviceDTO> create(MIDevice device){
		
		List<DeviceDTO> newDevices= new ArrayList<DeviceDTO>();
		DeviceDTO deviceDto = new DeviceDTO();
        
		mapMIDeviceToDeviceDTO(device,deviceDto);
		
		newDevices.add(deviceDAO.insert(deviceDto));
		
		return newDevices;
	}
	
	private void mapMIDeviceToDeviceDTO(MIDevice device,DeviceDTO deviceDto) {
		
		deviceDto.setId(device.getId());
		
		deviceDto.setCarrier(device.getCarrier());
		deviceDto.setClientId(device.getClientId().toString());
		deviceDto.setManufacturer(device.getManufacturer());
		deviceDto.setModel(device.getModel());
		deviceDto.setPhoneNumber(device.getPhoneNumber());
		deviceDto.setRetired(String.valueOf(device.isRetired()));
		deviceDto.setRoaming(String.valueOf(device.getRoaming()));
		deviceDto.setUuid(device.getUuid());
		
	}
	/**
	 * Update contact/contacts
	 * @param data - json data from request
	 * @return updated contacts
	 */
	@Transactional
	public List<DeviceDTO> update(MIDevice device){
		
		List<DeviceDTO> updatedDevices = new ArrayList<DeviceDTO>();
		DeviceDTO deviceDto = new DeviceDTO();
        
		mapMIDeviceToDeviceDTO(device,deviceDto);
		updatedDevices.add(deviceDAO.save(deviceDto));
		
		return updatedDevices;
	}
	
	/**
	 * Delete contact/contacts
	 * @param contact - json data from request
	 */
	@Transactional
	public void delete(MIDevice device){

		
		deviceDAO.delete(device.getId());
	}
	
	
	 /* Get total of Contacts from database.
	 * Need to set this value on ExtJS Store
	 * @return
	 */
	public int getTotalDevices(){

		return deviceDAO.getTotalDevices();
	}

	
	
}
