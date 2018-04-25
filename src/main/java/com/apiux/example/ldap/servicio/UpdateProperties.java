package com.apiux.example.ldap.servicio;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class UpdateProperties {

	public static void updateUserPropeties(String uid, String password) {
		try {
			PropertiesConfiguration pc = new PropertiesConfiguration("C:\\Users\\APIUX\\Desktop\\users.properties");
			pc.setProperty(uid, password);
			pc.save();
		} catch (ConfigurationException ex) {
			System.out.println("Error.-"+ex.getCause());
		}
	}

}