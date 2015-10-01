package br.com.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.model.MIDevice;

public class DeviceMapper implements RowMapper{

  public MIDevice mapRow(ResultSet rs, int rowNum) throws SQLException {
    	MIDevice device = new MIDevice();
    	device.setId(rs.getLong("id"));
        device.setUuid(rs.getString("uuid"));
        device.setPhoneNumber(rs.getString("phone_number"));
        device.setCarrier(rs.getString("carrier"));
        device.setClientId(rs.getLong("mi_client_id"));
        device.setModel(rs.getString("model"));
        device.setManufacturer(rs.getString("manufacturer"));
        device.setRetired(rs.getBoolean("retired"));
        device.setRoaming(rs.getString("roaming").charAt(0));
        return device;
    }}