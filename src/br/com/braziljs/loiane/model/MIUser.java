package br.com.braziljs.loiane.model;




import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
import com.mi.ldap.DirectoryUser;
import com.mi.ldapdb.LDAPSyncUser;
import com.mi.ldapdb.model.LDAPUser;
import com.mi.middleware.dao.BaseDAO;
import com.mi.middleware.dto.AssetBean;
import com.mi.middleware.dto.AssetType;*/

/**
 * 
 * @author smishra
 * 
 */
@Entity
@Table(name = "mi_user")
public class MIUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(MIUser.class);

    public static final String DELETED_TAG = "DELETED";
    public static final String ANONYMOUS = "misys_anonymous";
    public static final String ANONYMOUS_DISPLAY = "<Anonymous>";
    public static final String ANONYMOUS_UUID = "misys_anonymous_uuid";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, name = "principal", length = 50, unique = true)
    private String principal;

    @Column(name = "name", length = 64, unique = false, nullable = true)
    private String name;

    @Column(name = "first_name", length = 64, unique = false, nullable = true)
    private String firstName;

    @Column(name = "last_name", length = 64, unique = false, nullable = true)
    private String lastName;

    @Column(name = "email_address", length = 128, unique = true)
    private String emailAddress;

    @Column(name = "account_enabled", columnDefinition = "char(1) default 't'")
    private char enabled;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "password_hash", length = 40)
    private String passwordHash;

   /* @Column(name = "saml_pseudonymous_id", nullable = false, unique = true, length = 128)
    private String samlPseudonymousIdentifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = true, referencedColumnName = "uuid")
    private MIUser createdBy;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<MIUserGroup> userGroups = new HashSet<MIUserGroup>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<MIDevice> phones = new HashSet<MIDevice>();*/

    /*@ManyToMany
    @JoinTable(name = "mifs_map_users_to_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role0> roles = new HashSet<Role0>();*/
/*
    @Column(name = "source", columnDefinition = "char(1) default 'L'")
    private char source;

    @Column(name = "trash", columnDefinition = "char(1) default 'f'")
    private char trash;*/

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = true, referencedColumnName = "id")
    private MICountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = true, referencedColumnName = "id")
    private MILanguage language;*/

    @Column(name="modified_at", nullable=true, unique=false)
    private Long modifiedAt;

    @Column(name = "force_password_change", columnDefinition = "char(1) default 'f'")
    private char forcePasswordChange = 'f';

    @Column (name="last_admin_portal_login_time")
    private Long lastAdminPortalLoginTime;

    @Column (name="encryption_algorithm_version")
    private Integer encryptionAlgVersion = 0;

    @Column(name="last_login_ip")
    private String lastLoginIp;

    /**
     * Default constructor - creates a new instance with no values set.
     */
    public MIUser() {
     //   this.trash = 'f';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrincipal() {
        return this.principal;
    }

   /* public boolean isEnabled() {
        if (this.enabled == BaseDAO.CHAR_TRUE) {
            return true;
        }
        return false;
    }*/

    public void setPrincipal(String username) {
        this.principal = username;
    }

    /*public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enabled = BaseDAO.CHAR_TRUE;
        } else {
            this.enabled = BaseDAO.CHAR_FALSE;
        }
    }*/

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        if (!StringUtils.equalsIgnoreCase(this.firstName, firstName)) {
            this.modifiedAt = System.currentTimeMillis();
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        if (!StringUtils.equalsIgnoreCase(this.lastName, lastName)) {
            this.modifiedAt = System.currentTimeMillis();
        }
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

   /* public MIUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(MIUser createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }*/

    /*public Set<MIUserGroup> getUserGroups() {
        return this.userGroups;
    }

    public MIUserGroup removeGroup(MIGroup group) {
        for (MIUserGroup ug : this.userGroups) {
            if (ug.getGroup().equals(group)) {
                this.userGroups.remove(ug);
                log.warn("Deleted the group: " + group + ", total groupCount: "
                        + this.userGroups.size());
                return ug;
            }
        }
        return null;
    }*/

  /*  void addUserGroup(MIUserGroup userGroup) {
        if (!this.userGroups.contains(userGroup)) {
            this.userGroups.add(userGroup);
        }
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MIUser)) {
            return false;
        }

        final MIUser user = (MIUser) o;

        return !(this.principal != null ? !this.principal.equals(user
                .getPrincipal()) : user.getPrincipal() != null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (this.principal != null ? this.principal.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MIUser[");
        sb.append("Name: ").append(this.getName()).append(", ");
        sb.append("Principal: ").append(this.getPrincipal()).append(", ID: ")
        .append(this.getId()).append(", UUID: ").append(this.getUuid());
        sb.append("]");
        return sb.toString();
    }

  /*  public Collection<MIDevice> getPhones() {
        return this.phones;
    }

    public void setPhones(Set<MIDevice> phones) {
        this.phones = phones;
    }

    public void addPhone(MIDevice phone) {
        this.phones.add(phone);
     //   phone.setUser(this);
    }

    public MIDevice getPhoneByPhoneNumber(String deviceId) {
        for (MIDevice d : this.phones) {
            if (d.getPhoneNumber().equals(deviceId)) {
                return d;
            }
        }
        return null;
    }*/

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (!StringUtils.equalsIgnoreCase(this.name, name)) {
            this.modifiedAt = System.currentTimeMillis();
        }
        this.name = name;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        if (!StringUtils.equalsIgnoreCase(this.emailAddress, emailAddress)) {
            this.modifiedAt = System.currentTimeMillis();
        }
        this.emailAddress = emailAddress;
    }

    public Class<MIUser> getClazz() {
        return MIUser.class;
    }

    public String getAssetId() {
        return this.uuid;
    }

    public String getAssetName() {
        return this.getName();
    }

/*    public AssetType getType() {
        return AssetType.USER;
    }

    public Set<Role0> setRoles(Set<Role0> roles) {
        Set<Role0> oldRoles = this.roles;
        this.roles = roles;
        return oldRoles;
    }

    public Set<Role0> getRoles() {
        return this.roles;
    }

    public void addRole(Role0 role) {
        if (!this.roles.contains(role)) {
            log.info("Added ROLE " + role.getName() + " to user "
                    + this.principal);
            this.roles.add(role);
        }
    }

    public void removeRole(String role) {
        for (Role0 r : this.roles) {
            if (r.getName().equals(role)) {
                log.info("Removed ROLE " + role + " from user "
                        + this.principal);
                this.roles.remove(r);
                break;
            }
        }
    }*/

 /*   public char getSource() {
        return this.source;
    }

    public void setSource(char source) {
        this.source = source;
    }

    public void setTrash(char trash) {
        this.trash = trash;
    }

    public char getTrash() {
        return trash;
    }*/

    public static boolean isNull(String t) {
        if(StringUtils.isBlank(t)) {
            return true;
        }
        if(t.matches("^.*\\bnull\\b.*$")) {
            return true;
        }
        return false;
    }

    public void setUserNames(String principal, String firstName, String lastName, String displayName) {
        if (isNull(firstName)) {
            firstName = null;
        }
        if (isNull(lastName)) {
            lastName = null;
        }
        if (isNull(displayName)) {
            displayName = null;
        }

        if (StringUtils.isNotBlank(firstName)) {
            this.setFirstName(firstName);
        }
        if (StringUtils.isNotBlank(lastName)) {
            this.setLastName(lastName);
        }

        this.setName(getUserDisplayName(principal, firstName, lastName, displayName));
    }

    public static String getUserDisplayName(String principal, String firstName, String lastName, String displayName) {
        if(StringUtils.isNotBlank(displayName)) {
            return displayName;
        } else if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            return (firstName + " " + lastName);
        } else if (StringUtils.isNotBlank(firstName)) {
            return firstName;
        } else if (StringUtils.isNotBlank(lastName)) {
            return lastName;
        } else {
            return principal;
        }
    }

  /*  public void updateFromDirectoryUser(LDAPUser u) {
        this.setUserNames(u.getPrincipal(), u.getFirstName(), u.getLastName(), u.getDisplayName());
        this.setEmailAddress(u.getEmail());
    }

    public void updateFromDirectoryUser(LDAPSyncUser u) {
        this.setUserNames(u.getPrincipal(), u.getFirstName(), u.getLastName(), u.getDisplayName());
        this.setEmailAddress(u.getEmail());
    }


    public void updateFromDirectoryUser(DirectoryUser u) {
        this.setUserNames(u.getPrincipal(), u.getFirstName(), u.getLastName(), u.getDisplayName());
        this.setEmailAddress(u.getEmail());
    }

    public void setCountry(MICountry country){
        this.country = country;
    }

    public MICountry getCountry(){
        return(this.country);
    }

    public void setLanguage(MILanguage language){
        this.language = language;
    }

    public MILanguage getLanguage(){
        return(this.language);
    }*/

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public char getForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(char forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }
 /*   public void setForcePasswordChange(boolean enabled) {
        if (enabled) {
            this.forcePasswordChange = BaseDAO.CHAR_TRUE;
        } else {
            this.forcePasswordChange = BaseDAO.CHAR_FALSE;
        }
    }

    public boolean isForcePasswordChange() {
        if (this.forcePasswordChange == BaseDAO.CHAR_TRUE) {
            return true;
        }
        return false;
    }
*/

   /* public String getSamlPseudonymousIdentifier() {
        return samlPseudonymousIdentifier;
    }

    public void setSamlPseudonymousIdentifier(String samlPseudonymousIdentifier) {
        this.samlPseudonymousIdentifier = samlPseudonymousIdentifier;
    }*/

    public Long getLastAdminPortalLoginTime() {
        return lastAdminPortalLoginTime;
    }

    public void setLastAdminPortalLoginTime(Long lastAdminPortalLoginTime) {
        this.lastAdminPortalLoginTime = lastAdminPortalLoginTime;
    }

    public Integer getEncryptionAlgVersion() {
        return encryptionAlgVersion;
    }

    public void setEncryptionAlgVersion(Integer encryptionAlgVersion) {
        this.encryptionAlgVersion = encryptionAlgVersion;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

}
