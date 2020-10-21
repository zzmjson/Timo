package com.linln.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectConvertor {
	private final static ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	static {
		MultiDateDeserializer deserializer = new MultiDateDeserializer();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Date.class, deserializer);
		mapper.registerModule(module);
	}

	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
		return mapper.convertValue(map, beanClass);
	}

	public static <T> Map<String, Object> objectToMap(T beanClass) {
		return mapper.convertValue(beanClass, Map.class);
	}


	public static Map<String, Object> objectToMapNoClass(Object obj) throws Exception{
		if(obj == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.compareToIgnoreCase("class") == 0) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter!=null ? getter.invoke(obj) : null;
			map.put(key, value);
		}
		return map;
	}
}
