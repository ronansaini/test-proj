package br.com.test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessTemplateData
{
    static{
        System.setProperty("log4j.configuration", "upgrade-log4j.properties");
    }
    
    private static final Log log = LogFactory.getLog(ProcessTemplateData.class);
    
    private static final String MIFSJDBCFILE = "mifs-jdbc.properties";
    private static final String JDBC_CLASSNAME = "jdbc.driverClassName";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.username";
    private static final String JDBC_PASSWORD = "jdbc.password";

    private static final String SYSTEMTEMPLATEBASE = "mifsTemplateData_";
    private static final String CUSTOMTEMPLATEBASE = "mifsTemplateData_";

    private static final String ADMIN = "admin";
    private static final String SYSTEM = "system";

    private static class TemplateParams
    {
        private Long id;
        private Long languageId;
        private String langCode;
        private Long countryId;
        private Integer version;
        private String category;
        private String name;
        private Character platformType;
        private String rowType;
        private Long createdAt;
        private Long modifiedAt;
        private Long data_id;
        private String type;
        private String template;

        public TemplateParams(Long a, Long b, String c, Long d, Integer e, String f, String g, Character h, String i,  Long j, Long k, Long l, String m, String n)
        {
            id = a;
            languageId = b;
            langCode = c;
            countryId = d;
            version = e;
            category = f;
            name = g;
            platformType = h;
            rowType = i;
            createdAt = j;
            modifiedAt = k;
            data_id = l;
            type = m;
            template = n;
        }

        public Long getId()
        {
            return(id);
        }

        public Long getLanguageId()
        {
            return(languageId);
        }

        public String getLangCode()
        {
            return(langCode);
        }

        public String getCategory()
        {
            return(category);
        }

        public String getName()
        {
            return(name);
        }

        public Long getCreatedAt()
        {
            return(createdAt);
        }

        public Long getModifiedAt()
        {
            return(modifiedAt);
        }

        public Long getDataId()
        {
            return(data_id);
        }

        public String getType()
        {
            return(type);
        }

        public String getTemplate()
        {
            return(template);
        }
    }

//        select * from mi_lang_template;
//        select * from mi_lang_template_data;
//        select t.*, d.* from mi_lang_template t inner join mi_lang_template_data d on d.template_id=t.id;
    private static void getLanguageMap(Connection connection, Map<String, Long> langMap)
    {
        PreparedStatement langQueryPstmt = null;
        try
        {
            langQueryPstmt = connection.prepareStatement("select  l.* from mi_language l where l.appliance_enabled = 't'") ;
            ResultSet resultSet = langQueryPstmt.executeQuery();
            while(resultSet.next())
            {
                Long langId = resultSet.getLong("l.id");
                String langCode = resultSet.getString("l.iso_639_1");
                langMap.put(langCode, langId);
            }
            try
            {
                resultSet.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while fetching language map", se);
                se.printStackTrace();
                System.exit(-1);
            }
        }
        catch(SQLException se2)
        {
            log.error("Exception while fetching language map", se2);
            se2.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            if(langQueryPstmt != null)
            {
                try
                {
                    langQueryPstmt.close();
                }
                catch(SQLException se)
                {
                    log.error("Exception while closing statement", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

//        select * from mi_lang_template;
//        select * from mi_lang_template_data;
//        select t.*, d.* from mi_lang_template t inner join mi_lang_template_data d on d.template_id=t.id;
    private static void getCountryMap(Connection connection, Map<String, Long> map)
    {
        PreparedStatement queryPstmt = null;
        try
        {
            queryPstmt = connection.prepareStatement("select  c.* from mi_country c") ;
            ResultSet resultSet = queryPstmt.executeQuery();
            while(resultSet.next())
            {
                Long id = resultSet.getLong("c.id");
                String code = resultSet.getString("c.iso_alpha2_code");
                map.put(code.toLowerCase(), id);
            }
            try
            {
                resultSet.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while fetching country map", se);
                se.printStackTrace();
                System.exit(-1);
            }
        }
        catch(SQLException se2)
        {
            log.error("Exception while fetching country map", se2);
            se2.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            if(queryPstmt != null)
            {
                try
                {
                    queryPstmt.close();
                }
                catch(SQLException se)
                {
                    log.error("Exception while closing statement", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    private static long getTemplates(Connection connection, Map<String, Map<String, TemplateParams>> templatesByKeyMap, Map<String, Map<String, TemplateParams>> sysTemplatesByKeyMap, Map<String, Map<String, TemplateParams>> adminTemplatesByKeyMap, Map<String, Set<String>> adminChanges)
    {
        long maxTemplateId = 1000000000; // default id
        long templateId = 0;
        //PreparedStatement templateQueryPstmt = connection.prepareStatement("select * from mi_lang_template");
        // PreparedStatement templateDataQueryPstmt = connection.prepareStatement("select * from mi_lang_template_data");
        //ResultSet allTemplateResultSet = templateQueryPstmt.executeQuery();
        // ResultSet allTemplateDataResultSet = templateDataQueryPstmt.executeQuery();
        PreparedStatement templateQueryPstmt = null;
        try
        {
            templateQueryPstmt = connection.prepareStatement("select t.*, l.*, d.* from mi_lang_template t inner join mi_language l on l.id=t.language_id inner join mi_lang_template_data d on d.template_id=t.id order by t.language_id asc, t.id asc");
            ResultSet allTemplateDataResultSet = templateQueryPstmt.executeQuery();
    //            Map<String, TemplateParams> sysTemplatesByKey = new HashMap<String, TempateParams>();
    //            Map<String, TemplateParams> adminTemplatesByKey = new HashMap<String, TempateParams>();
    //            Map<String, TemplateParams> templatesByKey = new HashMap<String, TempateParams>();
    //            Map<Long, TemplateParams> allTemplatesById = new HashMap<Long, TempateParams>();
    
            while(allTemplateDataResultSet.next())
            {
                Long template_id = allTemplateDataResultSet.getLong("t.id");
                if((template_id.longValue() < maxTemplateId) && (template_id.longValue() > templateId))
                {
                    templateId = template_id.longValue();
                }
                Long langId = allTemplateDataResultSet.getLong("t.language_id");
                String langCode = allTemplateDataResultSet.getString("l.iso_639_1");
                Long countryId = allTemplateDataResultSet.getLong("t.country_id");
                String category = allTemplateDataResultSet.getString("t.category");
                String name = allTemplateDataResultSet.getString("t.name");
                String platformTypeStr = allTemplateDataResultSet.getString("t.platform_type");
                Character platformType = null;
                if(platformTypeStr != null)
                {
                    platformType = Character.valueOf(platformTypeStr.charAt(0));
                }
                String rowType = allTemplateDataResultSet.getString("t.row_type");
                Long data_id= allTemplateDataResultSet.getLong("d.id");
                System.out.println("data_id: " + data_id);
                Long createdAt = allTemplateDataResultSet.getLong("t.created_at");
                Long modifiedAt = allTemplateDataResultSet.getLong("t.modified_at");
                String type = null;
                String template = null;
                if(data_id != null)
                {
                    type = allTemplateDataResultSet.getString("d.type");
                    template = allTemplateDataResultSet.getString("d.template");
                }
                StringBuilder code = new StringBuilder();
                StringBuilder tcode = new StringBuilder();
                if((category != null) && !category.equals(""))
                {
                    code.append(category);
                    tcode.append(category);
                }
                if((type != null) && !type.equals(""))
                {
                    if(code.length() > 0)
                    {
                        code.append("_");
                    }
                    code.append(type);
                }
                if(platformType != null)
                {
                    if(code.length() > 0)
                    {
                        code.append("_");
                        tcode.append("_");
                    }
                    code.append(platformType.toString());
                    tcode.append(platformType.toString());
                }
                if((name != null) && !name.equals(""))
                {
                    if(code.length() > 0)
                    {
                        code.append("_");
                        tcode.append("_");
                    }
                    code.append(name);
                    tcode.append(name);
                }
                if(tcode.length() > 0)
                {
                    tcode.append("_");
                }
                tcode.append(rowType);
                int version = 0;
                TemplateParams param = new TemplateParams(template_id, langId, langCode, countryId, version, category, name, platformType, rowType, createdAt, modifiedAt, data_id, type, template);
                System.out.println("paramdata_id: " + param.getDataId());
                log.info("paramdata_id: " + param.getDataId());
                StringBuilder mKey = new StringBuilder(langCode);
                if((countryId != null) && !countryId.equals(0L))
                {
                    mKey.append("_");
                    mKey.append(countryId.toString());
                }
                System.out.println("mKey: " + mKey.toString());
                log.info("mKey: " + mKey.toString());
                Map<String, TemplateParams> templatesByKey = templatesByKeyMap.get(mKey.toString());
                if(templatesByKey == null)
                {
                    templatesByKey = new HashMap<String, TemplateParams>();
                    templatesByKeyMap.put(mKey.toString(), templatesByKey);
                }
                if(templatesByKey.get(tcode.toString()) == null)
                {
                    templatesByKey.put(tcode.toString(), param);
                    //System.out.println("template: " + langCode + " : "  + tcode.toString() + " : " + templatesByKey.size());
                }
                if(rowType.equals(ADMIN))
                {
                    Map<String, TemplateParams> adminTemplatesByKey  = adminTemplatesByKeyMap.get(mKey.toString());
                    if(adminTemplatesByKey == null)
                    {
                        adminTemplatesByKey = new HashMap<String, TemplateParams>();
                        adminTemplatesByKeyMap.put(mKey.toString(), adminTemplatesByKey);
                    }
                    adminTemplatesByKey.put(code.toString(), param);
                    //System.out.println("admin: " + langCode + " : " + code.toString() + " : " + adminTemplatesByKey.size());
                }
                else
                {
                    Map<String, TemplateParams> sysTemplatesByKey  = sysTemplatesByKeyMap.get(mKey.toString());
                    if(sysTemplatesByKey == null)
                    {
                        sysTemplatesByKey = new HashMap<String, TemplateParams>();
                        sysTemplatesByKeyMap.put(mKey.toString(), sysTemplatesByKey);
                    }
                    sysTemplatesByKey.put(code.toString(), param);
                    //System.out.println("system: " + langCode + " : "  + code.toString() + " : " + sysTemplatesByKey.size());
                }
            }
            try
            {
                allTemplateDataResultSet.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while fetching templates", se);
                se.printStackTrace();
                System.exit(-1);
            }
            Set<String> mKeys = adminTemplatesByKeyMap.keySet();
            for(String mKey : mKeys)
            {
                Map<String, TemplateParams> adminMap = adminTemplatesByKeyMap.get(mKey);
                if((adminMap != null) && !adminMap.isEmpty())
                {
                    Map<String, TemplateParams> sysMap = sysTemplatesByKeyMap.get(mKey);
                    if((sysMap != null) && !sysMap.isEmpty())
                    {
                        Set<String> tKeys = adminMap.keySet();
                        for(String tKey : tKeys)
                        {
                            TemplateParams aParam = adminMap.get(tKey);
                            TemplateParams sParam = sysMap.get(tKey);
                            if((aParam != null) && (sParam != null))
                            {
                                String aTemplate = aParam.getTemplate();
                                String sTemplate = sParam.getTemplate();
                                if((aTemplate != null) && (sTemplate != null) && !aTemplate.equals(sTemplate))
                                {
                                    Set<String> aChanges = adminChanges.get(mKey);
                                    if(aChanges == null)
                                    {
                                        aChanges = new HashSet<String>();
                                        adminChanges.put(mKey, aChanges);
                                    }
                                    aChanges.add(tKey);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(SQLException se2)
        {
            log.error("Exception while fetching templates", se2);
            se2.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            if(templateQueryPstmt != null)
            {
                try
                {
                    templateQueryPstmt.close();
                }
                catch(SQLException se)
                {
                    log.error("Exception while closing statement", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return(templateId);
    }

    private static class MIPropertiesFileFilter implements FilenameFilter
    {
        String basename;
        public MIPropertiesFileFilter(String basename)
        {
            this.basename = basename;
        }
        public boolean accept(File dir, String name)
        {
            return(name.endsWith(".properties") && name.startsWith(basename));
        }
    }

    private static File[] getFiles(String tomcatPropertiesDir, String baseName)
    {
        File dirTomcatProps = new File(tomcatPropertiesDir);
        FilenameFilter filter = new MIPropertiesFileFilter(baseName);
        File[] files = dirTomcatProps.listFiles(filter);
        return(files);
    }

    private static long processTemplateDataFilesSystem(Connection connection, String tomcatPropertiesDir, String base, Map<String, Long> langMap, Map<String, Long> countryMap, Map<String, Map<String, TemplateParams>> templatesByKeyMap, Map<String, Map<String, TemplateParams>> dataTemplatesByKeyMap, List<TemplateParams> insertTemplateData, List<TemplateParams> updateTemplateData, long templateId, Map<String, Set<String>> adminChanges)
    {
        return processTemplateDataFiles(connection,tomcatPropertiesDir,base, langMap, countryMap, templatesByKeyMap, dataTemplatesByKeyMap, insertTemplateData, updateTemplateData, templateId, adminChanges, false);
    }

    private static long processTemplateDataFilesAdmin(Connection connection, String tomcatPropertiesDir, String base, Map<String, Long> langMap, Map<String, Long> countryMap, Map<String, Map<String, TemplateParams>> templatesByKeyMap, Map<String, Map<String, TemplateParams>> dataTemplatesByKeyMap, List<TemplateParams> insertTemplateData, List<TemplateParams> updateTemplateData, long templateId, Map<String, Set<String>> adminChanges)

    {
        return processTemplateDataFiles(connection,tomcatPropertiesDir,base, langMap, countryMap, templatesByKeyMap, dataTemplatesByKeyMap, insertTemplateData, updateTemplateData, templateId, adminChanges, true);
    }

    private static long processTemplateDataFiles(Connection connection, String tomcatPropertiesDir, String base, Map<String, Long> langMap, Map<String, Long> countryMap, Map<String, Map<String, TemplateParams>> templatesByKeyMap, Map<String, Map<String, TemplateParams>> dataTemplatesByKeyMap, List<TemplateParams> insertTemplateData, List<TemplateParams> updateTemplateData, long templateId, Map<String, Set<String>> adminChanges, boolean isAdmin)
    {
        PreparedStatement insertLangTemplatePstmt = null;
        try
        {
            insertLangTemplatePstmt = connection.prepareStatement("insert into mi_lang_template (id, language_id, country_id, version, category, name, platform_type, row_type, created_at, modified_at) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?)");
            File[] files = getFiles(tomcatPropertiesDir, base);
            String rowType = SYSTEM;
            if(isAdmin)
            {
                rowType = ADMIN;
            }
            Date date = new Date();
            if((files != null) && (files.length > 0))
            {
                for(File file : files)
                {
                    try
                    {
                        String[] fileTokens = Pattern.compile("_", Pattern.LITERAL).split((String) file.getName());
                        String langCode = fileTokens[fileTokens.length - 1];
                        String countryCode = fileTokens[fileTokens.length - 2];
                        if(countryCode.length() != 2)
                        {
                            countryCode = null;
                        }
                        fileTokens = Pattern.compile(".", Pattern.LITERAL).split(langCode);
                        langCode = fileTokens[0];

                        System.out.println("langCode: " + langCode + " : " + countryCode);
                        log.info("langCode: " + langCode + " : " + countryCode);
                        Long langId = null;
                        Long countryId = null;
                        //If countryCode isn't null, then langCode and countryCode are swapped.
                        StringBuilder mKey = new StringBuilder();
                        if(countryCode != null)
                        {
                            langId = langMap.get(countryCode);
                            countryId = countryMap.get(langCode);
                            mKey.append(countryCode);
                            if((countryId != null) && !countryId.equals(0L))
                            {
                                mKey.append("_");
                                mKey.append(countryId.toString());
                            }
                        }
                        else
                        {
                            langId = langMap.get(langCode);
                            mKey.append(langCode);
                        }
                        log.info("mKey: " + mKey.toString());
                        System.out.println("mKey: " + mKey.toString());
                        if(langId == null)
                        {
                            continue;
                        }
                        Map<String, TemplateParams> templatesByKey = templatesByKeyMap.get(mKey.toString());
                        Map<String, TemplateParams> dataTemplatesByKey = dataTemplatesByKeyMap.get(mKey.toString());
                        if(dataTemplatesByKey == null)
                        {
                            dataTemplatesByKey = new HashMap<String, TemplateParams>();
                            dataTemplatesByKeyMap.put(mKey.toString(), dataTemplatesByKey);
                        }
                        Set<String> adminChange = null;
                        if(adminChanges != null)
                        {
                            adminChange = adminChanges.get(mKey.toString());
                        }
    
                        Properties props = new Properties();
                        InputStreamReader iReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
                        props.load(iReader);
                        iReader.close();
                        for(Object key : props.keySet())
                        {
                            log.info("key: " + key.toString());
                            System.out.println("key: " + key.toString());
                            String[] tokens = Pattern.compile("_", Pattern.LITERAL).split((String) key);
                            System.out.println("tokens: " + tokens.length);
                            log.info("tokens: " + tokens.length);
                            if(tokens.length <= 1)
                            {
                                continue;
                            }
                            String category = tokens[0];
                            String name = null;
                            Character platformType = null;
                            if(tokens.length > 3)
                            {
                                if(tokens[tokens.length - 2].length() == 1)
                                {
                                    platformType = Character.valueOf(tokens[tokens.length - 2].charAt(0));
                                }
                                else
                                {
                                    name = tokens[tokens.length - 2];
                                }
                            }
                            String type = tokens[1];
                            String template = props.getProperty((String) key);
                            StringBuilder code = new StringBuilder();
                            StringBuilder tcode = new StringBuilder();
                            if((category != null) && !category.equals(""))
                            {
                                code.append(category);
                                tcode.append(category);
                            }
                            if((type != null) && !type.equals(""))
                            {
                                if(code.length() > 0)
                                {
                                    code.append("_");
                                }
                                code.append(type);
                            }
                            if(platformType != null)
                            {
                                if(code.length() > 0)
                                {
                                    code.append("_");
                                    tcode.append("_");
                                }
                                code.append(platformType.toString());
                                tcode.append(platformType.toString());
                            }
                            if((name != null) && !name.equals(""))
                            {
                                if(code.length() > 0)
                                {
                                    code.append("_");
                                    tcode.append("_");
                                }
                                code.append(name);
                                tcode.append(name);
                            }
                            if(tcode.length() > 0)
                            {
                                tcode.append("_");
                            }
                            tcode.append(rowType);
                            TemplateParams param = null;
                            log.info("code: " + code.toString());
                            log.info("tcode: " + tcode.toString());
                            System.out.println("code: " + code.toString());
                            System.out.println("tcode: " + tcode.toString());
                            if(templatesByKey != null)
                            {
                                param = templatesByKey.get(tcode.toString());
                            }
                            else
                            {
                                templatesByKey = new HashMap<String, TemplateParams>();
                                templatesByKeyMap.put(mKey.toString(), templatesByKey);
                            }
                            if(param == null)
                            {
                                insertLangTemplatePstmt.setLong(1, ++templateId);
                                insertLangTemplatePstmt.setLong(2, langId);
                                if(countryId != null)
                                {
                                    insertLangTemplatePstmt.setLong(3, countryId);
                                }
                                else
                                {
                                    insertLangTemplatePstmt.setNull(3, Types.BIGINT);
                                }
                                insertLangTemplatePstmt.setInt(4, 0);
                                insertLangTemplatePstmt.setString(5, category);
                                if((name != null) && !name.equals(""))
                                {
                                    insertLangTemplatePstmt.setString(6, name);
                                }
                                else
                                {
                                    insertLangTemplatePstmt.setNull(6, Types.VARCHAR);
                                }
                                if(platformType == null)
                                {
                                    insertLangTemplatePstmt.setNull(7, Types.CHAR);
                                }
                                else
                                {
                                    insertLangTemplatePstmt.setString(7, platformType.toString());
                                }
                                insertLangTemplatePstmt.setString(8, rowType);
                                insertLangTemplatePstmt.setLong(9, date.getTime());
                                insertLangTemplatePstmt.setLong(10, date.getTime());
                                System.out.println("insert template: " + tcode.toString());
                                log.info("insert template: " + tcode.toString());
                                int c = insertLangTemplatePstmt.executeUpdate();
                                ResultSet result = insertLangTemplatePstmt.getGeneratedKeys();
                                if(result != null && result.next())
                                {
                                    Long id = result.getLong(1);
                                    param = new TemplateParams(id, langId, langCode, countryId, 0, category, name, platformType, rowType, date.getTime(), date.getTime(), null, type, template);
                                    templatesByKey.put(tcode.toString(), param);
                                }
                                result.close();
                            }
                            TemplateParams paramData = dataTemplatesByKey.get(code.toString());
                            if(paramData == null)
                            {
                                paramData = new TemplateParams(param.getId(), langId, langCode, countryId, 0, category, name, platformType, rowType, date.getTime(), date.getTime(), null, type, template);
                                insertTemplateData.add(paramData);
                            }
                            else
                            {
                                boolean adminChanged = false;
                                if(adminChange != null)
                                {
                                    adminChanged = adminChange.contains(code.toString());
                                }
                                //Only update system type rows, assume admin are customer based, or admin type rows that were the same as the old system row
                                if((!paramData.getTemplate().equals(template) && rowType.equals(SYSTEM)) || (!adminChanged && rowType.equals(ADMIN) && !paramData.getTemplate().equals(template)))
                                {
                                    System.out.println("dataid: " + paramData.getDataId());
                                    log.info("dataid: " + paramData.getDataId());
                                    TemplateParams newparamData = new TemplateParams(param.getId(), langId, langCode, countryId, 0, category, name, platformType, rowType, date.getTime(), date.getTime(), paramData.getDataId(), type, template);
                                    updateTemplateData.add(newparamData);
                                }
                            }
                        }
                    }
                    catch(Exception e1)
                    {
                        log.error("Exception while processing template data files", e1);
                        e1.printStackTrace();
                    }
                }
            }
        }
        catch(SQLException se2)
        {
            log.error("Exception while processing template data files", se2);
            se2.printStackTrace();
            System.exit(-1);
        }
        finally
        {
            if(insertLangTemplatePstmt != null)
            {
                try
                {
                    insertLangTemplatePstmt.close();
                }
                catch(SQLException se)
                {
                    log.error("Exception while closing statement", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return(templateId);
    }

    public static void processTemplateData(String[] args)
    {
        final String tomcatPropertiesDir = args[0];
        Date date = new Date();
        try
        {
            Connection connection = getConnection(tomcatPropertiesDir);
            if(connection == null)
            {
                log.error("Could not connect to db.");
                System.out.println("Could not connect to db.");
                System.exit(-1);
            }

            PreparedStatement insertLangTemplateDataPstmt = connection.prepareStatement("insert into mi_lang_template_data (template_id, type, template, created_at, modified_at) values(?, ?, ?, ?, ?)");
            PreparedStatement updateLangTemplateDataPstmt = connection.prepareStatement("update mi_lang_template_data set template = ?, modified_at = ? where id = ?");

            Map<String, Long> langMap = new HashMap<String, Long>();
            getLanguageMap(connection, langMap);
            Map<String, Long> countryMap = new HashMap<String, Long>();
            getCountryMap(connection, countryMap);
//            Map<String, Map<String, TemplateParams>> allTemplatesByIdMap = new HashMap<Long, Map<Long, TemplateParams>>();
            Map<String, Map<String, TemplateParams>> templatesByKeyMap = new HashMap<String, Map<String, TemplateParams>>();
            Map<String, Map<String, TemplateParams>> sysTemplatesByKeyMap = new HashMap<String, Map<String, TemplateParams>>();
            Map<String, Map<String, TemplateParams>> adminTemplatesByKeyMap = new HashMap<String, Map<String, TemplateParams>>();
            Map<String, Set<String>> adminChanges = new HashMap<String, Set<String>>();
            long highestId = getTemplates(connection, templatesByKeyMap, sysTemplatesByKeyMap, adminTemplatesByKeyMap, adminChanges);

            List<TemplateParams> updateTemplateData = new ArrayList<TemplateParams>();
            List<TemplateParams> insertTemplateData = new ArrayList<TemplateParams>();
            highestId = processTemplateDataFilesSystem(connection, tomcatPropertiesDir, SYSTEMTEMPLATEBASE, langMap, countryMap, templatesByKeyMap, sysTemplatesByKeyMap, insertTemplateData, updateTemplateData, highestId, null);
            
            log.info("system insert size: " + insertTemplateData.size() + " : update size: " + updateTemplateData.size());
            System.out.println("system insert size: " + insertTemplateData.size() + " : update size: " + updateTemplateData.size());
            
            highestId = processTemplateDataFilesAdmin(connection, tomcatPropertiesDir, CUSTOMTEMPLATEBASE, langMap, countryMap, templatesByKeyMap, adminTemplatesByKeyMap, insertTemplateData, updateTemplateData, highestId, adminChanges);
            
            log.info("admin insert size: " + insertTemplateData.size() + " : update size: " + updateTemplateData.size());
            System.out.println("admin insert size: " + insertTemplateData.size() + " : update size: " + updateTemplateData.size());
            
            for(TemplateParams param : insertTemplateData)
            {
                insertLangTemplateDataPstmt.setLong(1, param.getId());
                insertLangTemplateDataPstmt.setString(2, param.getType());
                insertLangTemplateDataPstmt.setString(3, param.getTemplate());
                insertLangTemplateDataPstmt.setLong(4, date.getTime());
                insertLangTemplateDataPstmt.setLong(5, date.getTime());
                insertLangTemplateDataPstmt.addBatch();
            }
            int[] count = null;
            if (!insertTemplateData.isEmpty()) {
            	count = insertLangTemplateDataPstmt.executeBatch();
            }

            for(TemplateParams param : updateTemplateData)
            {
                log.info("updateparamdata: " + param.getDataId() + " : " + param.getId() + " : " + param.getTemplate());
                System.out.println("updateparamdata: " + param.getDataId() + " : " + param.getId() + " : " + param.getTemplate());
                updateLangTemplateDataPstmt.setString(1, param.getTemplate());
                updateLangTemplateDataPstmt.setLong(2, date.getTime());
                updateLangTemplateDataPstmt.setLong(3, param.getDataId());
                updateLangTemplateDataPstmt.addBatch();
            }
            if (!updateTemplateData.isEmpty()) {
            	count = updateLangTemplateDataPstmt.executeBatch();
            }

            try
            {
                insertLangTemplateDataPstmt.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while closing statement", se);
                se.printStackTrace();
                System.exit(-1);
            }
            try
            {
                updateLangTemplateDataPstmt.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while closing statement", se);
                se.printStackTrace();
                System.exit(-1);
            }
            try
            {
                connection.close();
            }
            catch(SQLException se)
            {
                log.error("Exception while closing DB connection", se);
                se.printStackTrace();
                System.exit(-1);
            }
        }
        catch(ClassNotFoundException cnfe)
        {
            log.error("Exception while processing template data", cnfe);
            cnfe.printStackTrace();
            System.exit(-1);
        }
        catch(SQLException se2)
        {
            log.error("Exception while processing template data", se2);
            se2.printStackTrace();
            System.exit(-1);
        }
    }

    private static Connection getConnection(String tomcatPropertiesDir) throws ClassNotFoundException, SQLException 
    {
        try
        {
            StringBuilder jdbcFile = new StringBuilder(tomcatPropertiesDir);
            jdbcFile.append(MIFSJDBCFILE);
            File file = new File(jdbcFile.toString());
            if(!file.exists())
            {
                log.error("JDBC properties file could not be found");
                System.out.println("JDBC properties file could not be found");
                System.exit(-1);
            }
            InputStreamReader iReader = new InputStreamReader(new FileInputStream(file));
            Properties props = new Properties();
            props.load(iReader);
            iReader.close();
            String jdbcClassName = props.getProperty(JDBC_CLASSNAME);
            String jdbcUrl = props.getProperty(JDBC_URL);
            String user = props.getProperty(JDBC_USER);
            String password = props.getProperty(JDBC_PASSWORD);
    
            Class.forName(jdbcClassName);
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            return(connection);
        }
        catch(Exception e)
        {
            log.error("Exception while establishing DB connection", e);
            e.printStackTrace();
            System.exit(-1);
        }
        return(null);
    }
    public static void fixEventSubscriptionTemplate(String[] args){
        final String tomcatPropertiesDir = args[0];
        Connection connection = null;
        try
        {
            connection = getConnection(tomcatPropertiesDir);
            if(connection == null)
            {
                log.error("Could not connect to db.");
                System.out.println("Could not connect to db.");
                System.exit(-1);
            }
            // Step-1 : Identify count of missing Rows in notify_event_subscription_template. Find lang_template_Ids belong to which Template-Name & which event they are subscribed to.
            Map <String,Map <Long,Integer>> eventSubscriptionTemplateMap = new HashMap <String,Map <Long,Integer>>();
            getEventSubscriptionTemplateMap(connection, eventSubscriptionTemplateMap);
            System.out.println("Event-Subscription-Template-Map: " + eventSubscriptionTemplateMap.toString());
            log.info("Event-Subscription-Template-Map: " + eventSubscriptionTemplateMap.toString());
            if (!eventSubscriptionTemplateMap.isEmpty()) {
                for (Map.Entry<String,Map <Long,Integer>>  entry : eventSubscriptionTemplateMap.entrySet()) {
                    String templateName = entry.getKey();
                    Map <Long,Integer> templateIdsCountMap = entry.getValue();
                    List<Long> templateIds = new ArrayList<Long>();
                    Map.Entry<Long,Integer>  templateIdsCountMapEntry = templateIdsCountMap.entrySet().iterator().next();
                    Long eventSubscriptionId = templateIdsCountMapEntry.getKey();
                    Integer missingRowCount = templateIdsCountMapEntry.getValue();
                    // Get TemplateIds for the TemplateName
                    getTemplateIds(connection, templateName, templateIds);
                    System.out.println("Missing-Row-Count: " + missingRowCount + "" + ", Actual-Template-Row-Count: " + templateIds.size());
                    log.info("Missing-Row-Count: " + missingRowCount + "" + ", Actual-Template-Row-Count: " + templateIds.size());
                    // Check the count mismatch in the two tables for templateIDs. If count mismatched, then delete & re-insert into notify_event_subscription_template table
                    if (missingRowCount != templateIds.size() && missingRowCount < templateIds.size()) {
                        int deleteCount = deleteNotifySubscriptionTemplate(connection, eventSubscriptionId);
                        System.out.println("Rows Deleted : " + deleteCount);
                        log.info("Rows Deleted : " + deleteCount);
                        int[] insertCount = insertNotifySubscriptionTemplate(connection, templateIds, eventSubscriptionId);
                        System.out.println("Rows Inserted : " + insertCount.length);
                        log.info("Rows Inserted : " + insertCount.length);
                    }
                }
            }
        } catch(ClassNotFoundException cnfe) {
            log.error("Exception while processing & fixing template data", cnfe);
            cnfe.printStackTrace();
            System.exit(-1);
        } catch(SQLException se2) {
            log.error("Exception while processing & fixing template data", se2);
            se2.printStackTrace();
            System.exit(-1);
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch(SQLException se) {
                    log.error("Exception while closing DB connection", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    //  Step-3 : For each templateId corresponding to a TemplateName, insert event_subcription_id & lang_template_Id into  notify_event_subscription_template table
    private static int[] insertNotifySubscriptionTemplate(Connection connection, List<Long> templateIds, Long eventSubscriptionId) {
        PreparedStatement insertNotifySubscriptionTemplatePstmt =  null;
        int[] insertCount = null;
        try {
            insertNotifySubscriptionTemplatePstmt = connection.prepareStatement("insert into notify_event_subscription_template (event_subscription_id,lang_template_id) values(?, ?)");
            for (Long langTemplateId : templateIds) {
                insertNotifySubscriptionTemplatePstmt.setLong(1, eventSubscriptionId);
                insertNotifySubscriptionTemplatePstmt.setLong(2, langTemplateId);
                insertNotifySubscriptionTemplatePstmt.addBatch();
            }
            insertCount = insertNotifySubscriptionTemplatePstmt.executeBatch();
        } catch (SQLException se) {
            log.error("Exception while inserting TemplateIds", se);
            se.printStackTrace();
            System.exit(-1);
        } finally {
            if(insertNotifySubscriptionTemplatePstmt != null) {
                try {
                    insertNotifySubscriptionTemplatePstmt.close();
                } catch(SQLException se2) {
                    log.error("Exception while closing insertNotifySubscriptionTemplate statement", se2);
                    se2.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return insertCount;
    }
 
    // Step-2 ---------------Delete from notify_event_subscription_template----------------
    private static int deleteNotifySubscriptionTemplate(Connection connection, Long eventSubscriptionId) {
        int deleteCount = 0; 
        PreparedStatement deleteNotifyTemplatePstmt = null;
        try {
            deleteNotifyTemplatePstmt = connection.prepareStatement("delete from notify_event_subscription_template where event_subscription_id = ?");
            deleteNotifyTemplatePstmt.setLong(1, eventSubscriptionId);
            deleteCount = deleteNotifyTemplatePstmt.executeUpdate();
        } catch (SQLException se) {
            log.error("Exception while deleting TemplateIds ", se);
            se.printStackTrace();
            System.exit(-1);
        } finally {
            if(deleteNotifyTemplatePstmt != null) {
                try {
                    deleteNotifyTemplatePstmt.close();
                } catch(SQLException se2) {
                    log.error("Exception while closing deleteNotifyTemplatePstmt statement", se2);
                    se2.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return deleteCount;
    }

//  Step-3 : Get templateIds corresponding to a TemplateName from mi_lang_template
     private static void getTemplateIds(Connection connection, String templateName, List<Long> templateIds) {
         PreparedStatement templateIdsQueryPstmt = null;
         ResultSet resultSet = null;
        try {
            templateIdsQueryPstmt = connection.prepareStatement("select id from mi_lang_template where name = ?");
            templateIdsQueryPstmt.setString(1, templateName);
            resultSet = templateIdsQueryPstmt.executeQuery();
            while (resultSet.next()) {
                Long templateId = resultSet.getLong("id");
                templateIds.add(templateId);
            }
        } catch(SQLException se2) {
            log.error("Exception while fetching TemplateIds", se2);
            se2.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if(templateIdsQueryPstmt != null) {
                    templateIdsQueryPstmt.close(); 
                 }
            } catch(SQLException se) {
                log.error("Exception while closing ResultSet/Statement", se);
                se.printStackTrace();
                System.exit(-1);
            }
        }
    }

    // Get Map of templateName, eventSubscriptionId & total templateIdCount from notify_event_subscription_template
    private static void getEventSubscriptionTemplateMap(Connection connection, Map <String,Map <Long,Integer>> eventSubscriptionTemplateMap) {
        PreparedStatement eventSubscriptionQueryPstmt = null;
        ResultSet resultSet = null;
        try {
            eventSubscriptionQueryPstmt = connection.prepareStatement("select name as template_name, event_subscription_id, count(*) as template_Id_count from notify_event_subscription_template nest INNER JOIN mi_lang_template mlt ON mlt.id = nest.lang_template_id where mlt.category like '%eventcenter.%'  group by template_name");
            resultSet = eventSubscriptionQueryPstmt.executeQuery();
            while (resultSet.next()) {
                   String templateName = resultSet.getString("template_name");
                   Long eventSubscriptionId = resultSet.getLong("event_subscription_id");
                   Integer templateIdCount = resultSet.getInt("template_Id_count");
                   Map <Long,Integer> templateIDCountMap = new HashMap <Long,Integer>();
                   templateIDCountMap.put(eventSubscriptionId, templateIdCount);
                   eventSubscriptionTemplateMap.put(templateName,templateIDCountMap);
            }
        } catch(SQLException se2) {
            log.error("Exception while fetching EventSubscriptionTemplate map", se2);
            se2.printStackTrace();
            System.exit(-1);
        } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if(eventSubscriptionQueryPstmt != null) {
                        eventSubscriptionQueryPstmt.close(); 
                     }
                } catch(SQLException se) {
                    log.error("Exception while closing ResultSet/Statement", se);
                    se.printStackTrace();
                    System.exit(-1);
                }
            }
    }

    public static void main(String[] args)
    {
        System.out.println("args: " + args.length);
        log.info("Arguments passed: "+args.length);
        if(args.length == 0)
        {
            log.error("Need /mi/tomcat-properties/ directory as argument");
            System.out.println("Need /mi/tomcat-properties/ directory as argument");
            System.exit(-1);
        }
        processTemplateData(args);
        fixEventSubscriptionTemplate(args);
        System.exit(0);
    }
}

