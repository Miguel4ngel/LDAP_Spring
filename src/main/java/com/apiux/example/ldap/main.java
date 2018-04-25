package com.apiux.example.ldap;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.apiux.example.ldap.gui.Ventana;
import com.apiux.example.ldap.model.Persona;
import com.apiux.example.ldap.repositorio.PersonRepo;
import com.apiux.example.ldap.servicio.MailService;
import com.apiux.example.ldap.servicio.PasswordService;
import com.apiux.example.ldap.servicio.PersonRepoImpl;
import com.apiux.example.ldap.servicio.UpdateProperties;

public class Main {

	public static void main(String[] args) {
		
//		Ventana v = new Ventana();
//		v.setVisible(true);

		PasswordService rs = new PasswordService();

		ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
		ApplicationContext contextEmail = new ClassPathXmlApplicationContext("Spring-Mail.xml");

		PersonRepo personRepo = context.getBean("personRepoImpl", PersonRepoImpl.class);
		MailService mailService = contextEmail.getBean("mailService", MailService.class);

		List<Persona> listaPersona = personRepo.obtenerPersonas();

		for (Persona p : listaPersona) {
			try {
				p.setPassword(rs.generarPassword(10));
				personRepo.actualizarContrasena(p);
				UpdateProperties.updateUserPropeties(p.getIdUsuario(), p.getPassword());
				String from = "miguel.sanjuanm@gmail.com";
				String to = p.getEmail();
				String subject = "Cambio automatico de contraseña";
				String text = "Estimado " + p.getNombre() + ", \n\n"
						+ "su contraseña para ingresar al portal www.xxx.com ha sido cambiada. \n\n "
						+ "Su nueva contraseña es: " + p.getPassword() + "\n\n" + "Saludos cordiales";
				mailService.sendMail(from, to, subject, text);
			} catch (Exception e) {
				System.out.println("Error.-" + e.getCause());
			}
		}
	}

}
