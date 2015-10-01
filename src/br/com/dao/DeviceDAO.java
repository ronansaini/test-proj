package br.com.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import br.com.model.Contact;
import br.com.model.DeviceDTO;
import br.com.model.MIDevice;



@Repository
public class DeviceDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private SimpleJdbcInsert insertDevice;
	private JdbcTemplate jdbcTemplate;
	
	
	
	@Autowired
	public DeviceDAO(DataSource ds)
	{
		this.namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(ds);
		this.jdbcTemplate = new JdbcTemplate(ds);
		this.insertDevice=new SimpleJdbcInsert(ds).withTableName("mi_device").usingGeneratedKeyColumns("id");
	}
	

	public DeviceDAO() {
		super();
	}


	public DeviceDTO insert(DeviceDTO device) throws DataAccessException {
		
		/* BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(device);
	            Number newKey = this.insertDevice.executeAndReturnKey(parameterSource);    //createPetParameterSource(device));
	            device.setId(newKey.longValue());
	    */
	            
	            String insertSql = "INSERT INTO mi_device (id, uuid, phone_number, carrier, retired, is_roaming, mi_client_id, model, manufacturer) "
	            	//	+ "VALUES ('51', 'b7b06173-fd67-49cd-a72f-0570abb93576', 'PDA7', 'Verizon', 'f', 't', '1073741873', 'Nexus 10', 'Google')";
	            		+ "VALUES (NULL, :UUID, :PHONENUMBER, :CARRIER, :RETIRED, :ROAMING, :CLIENTID, :MODEL, :MANUFACTURER)";
	          
	            
	            final MapSqlParameterSource row = new MapSqlParameterSource();
	            row.addValue("UUID", device.getUuid());
	            row.addValue("PHONENUMBER", device.getPhoneNumber());
                row.addValue("CARRIER", device.getCarrier());
                row.addValue("RETIRED", device.getRetired());
                row.addValue("ROAMING", device.getRoaming());
                row.addValue("CLIENTID", device.getClientId());
                row.addValue("MODEL", device.getModel());
                row.addValue("MANUFACTURER", device.getManufacturer());
            
	        	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        	namedParameterJdbcTemplate.update(insertSql, row, keyHolder);
                device.setId(keyHolder.getKey().longValue());

	            /*row.addValue("ID", Integer.valueOf("53"));
                row.addValue("UUID", "f7b06273-fd57-49cd-a74f-0570acb93576");
	            row.addValue("PHONENUMBER", "PDA7");
                row.addValue("CARRIER", "Verizon");
                row.addValue("RETIRED", "f");
                row.addValue("ROAMING", "f");
                row.addValue("CLIENTID", "1073235732");   
                row.addValue("MODEL", "Nexus");
                row.addValue("MANUFACTURER", "Google");

                namedParameterJdbcTemplate.update(insertSql, row);*/
                
                
	         return device;
	    }
	
	 public MIDevice findByDeviceId(long id) throws DataAccessException {
	        MIDevice device;
	        try {
	        	 String sql = "SELECT id,uuid,phone_number,carrier,mi_client_id FROM mi_device WHERE id = :id";
	        	Map<String, Object> params = new HashMap<String, Object>();
	            params.put("id", id);
	            device = (MIDevice) this.namedParameterJdbcTemplate.queryForObject(sql,params,new BeanPropertyRowMapper(MIDevice.class));
	        } catch (EmptyResultDataAccessException ex) {
	            throw new ObjectRetrievalFailureException(MIDevice.class, id);
	        }
	       
	        return device;
	    }
	 
	 public  List<MIDevice> getDevices(int start, int limit) throws DataAccessException {
	       
	        try {
	        	 String sql = "SELECT id,uuid,phone_number,carrier,mi_client_id FROM mi_device limit " + limit + " offset " + start;
	        	Map<String, Object> params = new HashMap<String, Object>();
	            
	            return  namedParameterJdbcTemplate.query(sql,params,new BeanPropertyRowMapper(MIDevice.class));//ParameterizedBeanPropertyRowMapper.newInstance(MIDevice.class)
	        } catch (EmptyResultDataAccessException ex) {
	            throw  ex;
	        }
	              
	    }
	 
	 public DeviceDTO save(DeviceDTO device) throws DataAccessException {
	    
		 final String updateSql = "UPDATE mi_device SET " +
                 " uuid = :UUID, " +
                 " phone_number = :PHONENUMBER, " +
                 " carrier = :CARRIER, " +
                 " retired = :RETIRED, " +
                 " is_roaming = :ROAMING, " +
                 " mi_client_id = :CLIENTID, " +
                 " model = :MODEL, " +
                 " manufacturer = :MANUFACTURER " +
                 " WHERE id = :ID ";

	            final MapSqlParameterSource row = new MapSqlParameterSource();
	            row.addValue("UUID", device.getUuid());
	            row.addValue("PHONENUMBER", device.getPhoneNumber());
	            row.addValue("CARRIER", device.getCarrier());
	            row.addValue("RETIRED", device.getRetired());
	            row.addValue("ROAMING", device.getRoaming());
	            row.addValue("CLIENTID", device.getClientId());
	            row.addValue("MODEL", device.getModel());
	            row.addValue("MANUFACTURER", device.getManufacturer());
	            row.addValue("ID", device.getId());
         
	        	namedParameterJdbcTemplate.update(updateSql, row);
	        
	        	 /*  if (device.isNew()) {
	            			Number newKey = this.insertDevice.executeAndReturnKey(
	                    	createDeviceParameterSource(device));
	            			device.setId(newKey.longValue());
	        		 } else {
            				namedParameterJdbcTemplate.update("UPDATE mi_device SET name=:name, birth_date=:birth_date, type_id=:type_id, owner_id=:owner_id WHERE id=:id", createDeviceParameterSource(device));
            		}
	        */
            
	        	return device;
	    }
	 
	 
	// @Override
	 public int getTotalDevices() {
	    String SQL = "SELECT count(*) FROM mi_device";
	   // SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
	   return  jdbcTemplate.queryForInt(SQL);
	// int total = this.namedParameterJdbcTemplate.queryForInt(SQL, namedParameters);
	//  return total;
	 }

	 public void delete(Long deviceId) throws DataAccessException {

	        final String sqlDelete = "DELETE FROM mi_device WHERE id = :ID";
	        final MapSqlParameterSource row = new MapSqlParameterSource();
	        row.addValue("ID", deviceId);

	        namedParameterJdbcTemplate.update(sqlDelete, row);
	    }	
	 
	 
	 /**
	     * Creates a {@link MapSqlParameterSource} based on data values from the supplied {@link Pet} instance.
	     */
	    private MapSqlParameterSource createDeviceParameterSource(DeviceDTO device) {
	    	 
	    	final MapSqlParameterSource row = new MapSqlParameterSource();
	         
	    	 row.addValue("UUID", device.getUuid());
	         row.addValue("PHONENUMBER", device.getPhoneNumber());
             row.addValue("CARRIER", device.getCarrier());
             row.addValue("RETIRED", device.getRetired());
             row.addValue("ROAMING", device.getRoaming());
             row.addValue("CLIENTID", device.getClientId());
             row.addValue("MODEL", device.getModel());
             row.addValue("MANUFACTURER", device.getManufacturer());
             
             return row;
             
             /*    	return new MapSqlParameterSource().
	                .addValue("id", device.getId())
	                .addValue("mi_client_id", device.getClientId())
	                .addValue("carrier", device.getCarrier())
	                .addValue("phone_number", device.getPhoneNumber());*/
	    }

	
	
	
}
