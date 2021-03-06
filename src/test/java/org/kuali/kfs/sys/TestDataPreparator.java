/*
 * The Kuali Financial System, a comprehensive financial management system for higher education.
 * 
 * Copyright 2005-2014 The Kuali Foundation
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.kfs.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.kfs.sys.context.SpringContext;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.PersistenceService;
import org.springframework.core.io.ClassPathResource;

/**
 * provide with a set of utilities that can be used to prepare test data for unit testing. The core idea is to convert Java
 * properties into a list of specified business objects or search criteria.
 */
public class TestDataPreparator {
    public static final String DEFAULT_FIELD_NAMES = "fieldNames";
    public static final String DEFAULT_DELIMINATOR = "deliminator";

    /**
     * load properties from the given class path resource. The class path is different than the absolute path. If a resource is
     * located at /project/test/org/kuali/kfs/util/message.properties, then its class path is org/kuali/kfs/util/message.properties,
     * which is the fully-qualified Java package name plus the resource name.
     * 
     * @param classPath the given class path of a resource
     * @return properties loaded from the given resource.
     */
    public static Properties loadPropertiesFromClassPath(String classPath) {
        Properties properties = new Properties();
        try {
            properties.load(TestDataPreparator.class.getClassLoader().getResourceAsStream(classPath));
        }
        catch (IOException e) {
            throw new RuntimeException("Invalid class path: " + classPath + e);
        }

        return properties;
    }

    /**
     * build a list of objects of type "clazz" from the test data provided by the given properties. The default fieldNames and
     * deliminator are used.
     * 
     * @param clazz the the specified object type
     * @param properties the given properties that contain the test data
     * @param propertyKeyPrefix the test data with the given key prefix can be used to construct the return objects
     * @param numberOfData the number of test data matching the search criteria
     * @return a list of objects of type "clazz" from the test data provided by the given properties
     */
    public static <T> List<T> buildTestDataList(Class<? extends T> clazz, Properties properties, String propertyKeyPrefix, int numberOfData) {
        String fieldNames = properties.getProperty(DEFAULT_FIELD_NAMES);
        String deliminator = properties.getProperty(DEFAULT_DELIMINATOR);
        return buildTestDataList(clazz, properties, propertyKeyPrefix, fieldNames, deliminator, numberOfData);
    }

    /**
     * build a list of objects of type "clazz" from the test data provided by the given properties
     * 
     * @param clazz the the specified object type
     * @param properties the given properties that contain the test data
     * @param propertyKeyPrefix the test data with the given key prefix can be used to construct the return objects
     * @param fieldNames the field names of the test data columns
     * @param deliminator the deliminator that is used to separate the field from each other
     * @param numberOfData the number of test data matching the search criteria
     * @return a list of objects of type "clazz" from the test data provided by the given properties
     */
    public static <T> List<T> buildTestDataList(Class<? extends T> clazz, Properties properties, String propertyKeyPrefix, String fieldNames, String deliminator, int numberOfData) {
        List<T> testDataList = new ArrayList<T>();
        for (int i = 1; i <= numberOfData; i++) {
            String propertyKey = propertyKeyPrefix + i;
            T testData = buildTestDataObject(clazz, properties, propertyKey, fieldNames, deliminator);
            testDataList.add(testData);
        }
        return testDataList;
    }

    /**
     * build an object of type "clazz" from the test data provided by the given properties
     * 
     * @param clazz the the specified object type
     * @param properties the given properties that contain the test data
     * @param propertyKey the test data with the given key
     * @param fieldNames the field names of the test data columns
     * @param deliminator the deliminator that is used to separate the field from each other
     * @return an object of type "clazz" from the test data provided by the given properties
     */
    public static <T> T buildTestDataObject(Class<? extends T> clazz, Properties properties, String propertyKey, String fieldNames, String deliminator) {
        T testData = null;
        try {
            testData = clazz.newInstance();
            ObjectUtil.populateBusinessObject(testData, properties, propertyKey, fieldNames, deliminator);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return testData;
    }

    /**
     * build an object of type "clazz" from the test data provided by the given properties
     * 
     * @param clazz the the specified object type
     * @param properties the given properties that contain the test data
     * @param propertyKey the test data with the given key
     * @return an object of type "clazz" from the test data provided by the given properties
     */
    public static <T> T buildTestDataObject(Class<? extends T> clazz, Properties properties, String propertyKey) {
        String fieldNames = properties.getProperty(DEFAULT_FIELD_NAMES);
        String deliminator = properties.getProperty(DEFAULT_DELIMINATOR);

        T testData = null;
        try {
            testData = clazz.newInstance();
            ObjectUtil.populateBusinessObject(testData, properties, propertyKey, fieldNames, deliminator);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return testData;
    }

    /**
     * build a list of objects of type "clazz" from the expected results provided by the given properties. The default fieldNames
     * and deliminator are used.
     * 
     * @param clazz the the specified object type. The instance of this type should be comparable through overriding Object.equals()
     * @param properties the given properties that contain the expected results
     * @param propertyKeyPrefix the expected results with the given key prefix can be used to construct the return objects
     * @param numberOfData the number of the expected results matching the search criteria
     * @return a list of objects of type "clazz" from the expected results provided by the given properties
     */
    public static <T> List<T> buildExpectedValueList(Class<? extends T> clazz, Properties properties, String propertyKeyPrefix, int numberOfData) {
        String fieldNames = properties.getProperty(DEFAULT_FIELD_NAMES);
        String deliminator = properties.getProperty(DEFAULT_DELIMINATOR);
        return buildExpectedValueList(clazz, properties, propertyKeyPrefix, fieldNames, deliminator, numberOfData);
    }

    /**
     * build a list of objects of type "clazz" from the expected results provided by the given properties
     * 
     * @param clazz the the specified object type. The instance of this type should be comparable through overriding Object.equals()
     * @param properties the given properties that contain the expected results
     * @param propertyKeyPrefix the expected results with the given key prefix can be used to construct the return objects
     * @param fieldNames the field names of the expected results columns
     * @param deliminator the deliminator that is used to separate the field from each other
     * @param numberOfData the number of the expected results matching the search criteria
     * @return a list of objects of type "clazz" from the expected results provided by the given properties
     */
    public static <T> List<T> buildExpectedValueList(Class<? extends T> clazz, Properties properties, String propertyKeyPrefix, String fieldNames, String deliminator, int numberOfData) {
        List<T> expectedDataList = new ArrayList<T>();
        for (int i = 1; i <= numberOfData; i++) {
            String propertyKey = propertyKeyPrefix + i;
            try {
                T expectedData = TestDataPreparator.buildTestDataObject(clazz, properties, propertyKey, fieldNames, deliminator);

                if (!expectedDataList.contains(expectedData)) {
                    expectedDataList.add(expectedData);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return expectedDataList;
    }

    /**
     * build the cleanup criteria for "clazz" from the given properties. The default fieldNames and deliminator are used.
     * 
     * @param clazz the the specified object type.
     * @param properties the given properties that contain the cleanup criteria fields and values
     * @param propertyKey the given property whose value provides the cleanup criteria values
     * @return the cleanup criteria for "clazz" from the given properties
     */
    public static <T> Map<String, Object> buildCleanupCriteria(Class<? extends T> clazz, Properties properties, String propertyKey) {
        String fieldNames = properties.getProperty(DEFAULT_FIELD_NAMES);
        String deliminator = properties.getProperty(DEFAULT_DELIMINATOR);
        return buildCleanupCriteria(clazz, properties, propertyKey, fieldNames, deliminator);
    }

    /**
     * build the cleanup criteria for "clazz" from the given properties.
     * 
     * @param clazz the the specified object type.
     * @param properties the given properties that contain the cleanup criteria fields and values
     * @param propertyKey the given property whose value provides the cleanup criteria values
     * @param fieldNames the field names of the cleanup columns
     * @param deliminator the deliminator that is used to separate the field from each other
     * @return the cleanup criteria for "clazz" from the given properties
     */
    public static <T> Map<String, Object> buildCleanupCriteria(Class<? extends T> clazz, Properties properties, String propertyKey, String fieldNames, String deliminator) {
        T instanceOfClazz = TestDataPreparator.buildTestDataObject(clazz, properties, propertyKey, fieldNames, deliminator);
        return ObjectUtil.buildPropertyMap(instanceOfClazz, Arrays.asList(StringUtils.split(fieldNames, deliminator)));
    }

    /**
     * persist the given data object if it is not in the persistent store
     * 
     * @param dataObject the given data object
     * @return return the data object persisted into the data store
     */
    public static <T extends PersistableBusinessObject> T persistDataObject(T dataObject) {
        T existingDataObject = (T) getBusinessObjectService().retrieve(dataObject);
        if (existingDataObject == null) {
            getBusinessObjectService().save(dataObject);
            getPersistenceService().retrieveNonKeyFields(dataObject);
            return dataObject;
        }
        else {
            ObjectUtil.buildObject(existingDataObject, dataObject);
        }

        return existingDataObject;
    }

    /**
     * persist the given data object if it is not in the persistent store
     * 
     * @param dataObject the given data object
     * @return return the data object persisted into the data store
     */
    public static <T extends PersistableBusinessObject> void persistDataObject(List<T> dataObjects) {
        for (T dataObject : dataObjects) {
            persistDataObject(dataObject);
        }
    }

    /**
     * remove the existing data from the database so that they cannot affact the test results
     */
    public static <T extends PersistableBusinessObject> void doCleanUpWithoutReference(Class<T> clazz, Properties properties, String propertykey, String fieldNames, String deliminator) throws Exception {
        Map<String, Object> fieldValues = buildFieldValues(clazz, properties, propertykey, fieldNames, deliminator);
        getBusinessObjectService().deleteMatching(clazz, fieldValues);
    }

    /**
     * remove the existing data from the database so that they cannot affact the test results
     */
    public static <T extends PersistableBusinessObject> void doCleanUpWithReference(Class<T> clazz, Properties properties, String propertykey, String fieldNames, String deliminator) throws Exception {
        List<T> dataObjects = findMatching(clazz, properties, propertykey, fieldNames, deliminator);

        for (T object : dataObjects) {
            getBusinessObjectService().delete(object);
        }
    }

    /**
     * remove the existing data from the database so that they cannot affact the test results
     */
    public static <T extends PersistableBusinessObject> List<T> findMatching(Class<T> clazz, Properties properties, String propertykey, String fieldNames, String deliminator) throws Exception {
        Map<String, Object> fieldValues = buildFieldValues(clazz, properties, propertykey, fieldNames, deliminator);
        return (List<T>) getBusinessObjectService().findMatching(clazz, fieldValues);
    }

    /**
     * build the field name and value pairs from the given properties in the specified properties file
     */
    public static <T> Map<String, Object> buildFieldValues(Class<T> clazz, Properties properties, String propertykey, String fieldNames, String deliminator) throws Exception {
        T cleanup = clazz.newInstance();
        ObjectUtil.populateBusinessObject(cleanup, properties, propertykey, fieldNames, deliminator);
        return ObjectUtil.buildPropertyMap(cleanup, Arrays.asList(StringUtils.split(fieldNames, deliminator)));
    }

    /**
     * test if the given object is in the given collection. The given key fields can be used for the comparison.
     * 
     * @return true if the given object is in the given collection; otherwise, false
     */
    public static <T> boolean contains(List<T> collection, T object, List<String> keyFields) {
        for (T objectInCollection : collection) {
            boolean contains = ObjectUtil.equals(objectInCollection, object, keyFields);

            if (contains) {
                return true;
            }
        }

        return false;
    }

    /**
     * test if the given two collections contain the exactly same elements. The given key fields can be used for the comparison.
     * 
     * @return true if the given two collections contain the exactly same elements; otherwise, false
     */
    public static <T> boolean hasSameElements(List<T> collection1, List<T> collection2, List<String> keyFields) {
        if (collection1 == collection2) {
            return true;
        }

        if (collection1 == null || collection2 == null) {
            return false;
        }

        if (collection1.size() != collection2.size()) {
            return false;
        }

        for (T object : collection2) {
            boolean contains = contains(collection1, object, keyFields);
            if (!contains) {
                return false;
            }
        }

        return true;
    }

    /**
     * Generates transaction data for a business object from properties
     * 
     * @param businessObject the transction business object
     * @return the transction business object with data
     * @throws Exception thrown if an exception is encountered for any reason
     */
    public static <T> T buildTestDataObject(Class<? extends T> clazz, Properties properties) {
        T testData = null;

        try {
            testData = clazz.newInstance();

            Iterator propsIter = properties.keySet().iterator();
            while (propsIter.hasNext()) {
                String propertyName = (String) propsIter.next();
                String propertyValue = (String) properties.get(propertyName);

                // if searchValue is empty and the key is not a valid property ignore
                if (StringUtils.isBlank(propertyValue) || !(PropertyUtils.isWriteable(testData, propertyName))) {
                    continue;
                }

                String propertyType = PropertyUtils.getPropertyType(testData, propertyName).getSimpleName();
                Object finalPropertyValue = ObjectUtil.valueOf(propertyType, propertyValue);
                
                if (finalPropertyValue != null) {
                    PropertyUtils.setProperty(testData, propertyName, finalPropertyValue);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot build a test data object with the given data. " + e);
        }

        return testData;
    }

    /**
     * get an instant of BusinessObjectService
     * 
     * @return an instant of BusinessObjectService
     */
    private static BusinessObjectService getBusinessObjectService() {
        return SpringContext.getBean(BusinessObjectService.class);
    }

    /**
     * get an instant of PersistenceService
     * 
     * @return an instant of PersistenceService
     */
    private static PersistenceService getPersistenceService() {
        return SpringContext.getBean(PersistenceService.class);
    }
}
