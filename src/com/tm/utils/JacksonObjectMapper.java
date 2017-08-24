/**
 * 
 */
package com.tm.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

//import org.codehaus.jackson.JsonFactory;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig.Feature;
//import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Chen
 *
 */
public class JacksonObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = 1L;
	
	public JacksonObjectMapper() {
		super(new JsonFactory());
		this.setSerializationInclusion(Include.NON_NULL);
		this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
}
