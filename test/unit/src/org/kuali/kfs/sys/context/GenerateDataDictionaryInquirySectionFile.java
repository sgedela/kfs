/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kfs.sys.context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.kfs.sys.ConfigureContext;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.kns.datadictionary.InquiryDefinition;
import org.kuali.rice.kns.datadictionary.InquirySectionDefinition;
import org.kuali.rice.kns.service.DataDictionaryService;

@ConfigureContext
public class GenerateDataDictionaryInquirySectionFile extends KualiTestBase {
    private static final Logger LOG = Logger.getLogger(GenerateDataDictionaryInquirySectionFile.class);
    private DataDictionary dataDictionary;

    protected void setUp() throws Exception {
        super.setUp();
        dataDictionary = SpringContext.getBean(DataDictionaryService.class).getDataDictionary();
    } 

    public void testGenerateInquirySections() throws Exception {
        TreeMap<String,List<InquirySectionDefinition>> boInquirySections = new TreeMap<String, List<InquirySectionDefinition>>();
        for(BusinessObjectEntry businessObjectEntry:dataDictionary.getBusinessObjectEntries().values()){
            if ( businessObjectEntry.getInquiryDefinition() != null ) {
                //LOG.info("Processing inquiry section for " + businessObjectEntry.getBusinessObjectClass().getName());
                InquiryDefinition inqDef = businessObjectEntry.getInquiryDefinition();
                boInquirySections.put(businessObjectEntry.getBusinessObjectClass().getName(), inqDef.getInquirySections() );
            }
        }
        LOG.info( "Class URI: " + getClass().getProtectionDomain().getCodeSource().getLocation().toURI() );
        File f = new File( new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI() ), getClass().getPackage().getName().replace(".", File.separator) + File.separator + "boInquirySections.properties" );
        f = new File( f.getAbsolutePath().replace( "/classes/", "/unit/src/" ) );
        LOG.info( "File path:" + f.getAbsolutePath() );
        FileWriter fw = new FileWriter( f );
        for ( String className : boInquirySections.keySet() ) {
            fw.write(className);
            fw.write('=');
            Iterator<InquirySectionDefinition> i = boInquirySections.get(className).iterator();
            while ( i.hasNext() ) {
                String title = i.next().getTitle();
                if ( title == null ) {
                    title = "(null)";
                } else if ( StringUtils.isBlank(title) ) {
                    title = "(blank)";
                }
                fw.write( title );
                if ( i.hasNext() ) {
                    fw.write( ',' );
                }
            }
            fw.write( '\n' );
        }
        fw.flush();
        fw.close();
    }
}
