package br.com.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This class is used to represent supported mobile carriers in MI database.
 *
 * @author smishra
 */
@Entity
@Table(name = "mi_device")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class MIDevice implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Character STATUS_A = 'a';
	public static final Character STATUS_P = 'p';
	public static final Character STATUS_R = 'r';
	public static final Character STATUS_V = 'v';
	public static final Character STATUS_W = 'w';

	public static final Character ROAMING = 't';
	public static final Character NOT_ROAMING = 'f';

	private static final Log log = LogFactory.getLog(MIDevice.class);

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "uuid", nullable = false, unique = true)
	private String uuid = UUID.randomUUID().toString();

	@Column(name = "phone_number", length = 32)
	private String phoneNumber;

	@Column(name = "carrier")
	private String carrier;

	@Column(name = "model")
	private String model;

	@Column(name = "manufacturer")
	private String manufacturer;

	/*
	 * @Fetch(FetchMode.JOIN)
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinTable(name = "mi_user_device", joinColumns = {@JoinColumn(name =
	 * "id", nullable = false)}, inverseJoinColumns = {@JoinColumn(name =
	 * "user_id")}) private MIUser user;
	 */

	@Column(name = "mi_client_id")
	private Long clientId;

	@Column(name = "is_roaming", columnDefinition = "char(1) default 'f'")
	private char roaming = 'f';

	@Column(name = "retired", columnDefinition = "char(1) default 'f'")
	private char retired = 'f';

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public MIDevice() {

		this.roaming = 'f';
		this.retired = 'f';
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getClientId() {
		return this.clientId;
	}

	public void setClientId(Long id) {
		this.clientId = id;
	}

	/*
	 * public MIUser getUser() { return this.user; }
	 * 
	 * public void setUser(final MIUser user) { this.user = user; }
	 */

	public String getCarrier() {
		return this.carrier;
	}

	public void setCarrier(String carrier) {
		if (carrier != null) {
			this.carrier = carrier.intern();
		} else {
			this.carrier = null;
		}
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}


	public char getRoaming() {
		return roaming;
	}

	public void setRoaming(char roaming) {
		this.roaming = roaming;
	}

	public boolean isRetired() {
		return (this.retired == 't' || this.retired == 'T');
	}

	public void setRetired(boolean flag) {
		this.retired = flag ? 't' : 'f';
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MIDevice)) {
			return false;
		}
		final MIDevice that = (MIDevice) o;
		return this.uuid.equals(that.uuid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	/*
	 * Not to use reflection to return the string, it returns tons of
	 * information that is not very meaningful
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(this.id)
				.append(", UUID:").append(this.uuid)
				.append(", deviceId: ").append(this.phoneNumber)
				.append(", carrier: ").append(this.carrier)
				.append(", clientId: ").append(this.clientId)
				.append(", manufacturer: ").append(this.manufacturer)
				.append(", model: ").append(this.model);
		return sb.toString();
	}

	public Class<MIDevice> getClazz() {
		return MIDevice.class;
	}

}
