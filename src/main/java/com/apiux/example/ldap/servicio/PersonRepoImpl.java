package com.apiux.example.ldap.servicio;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

import com.apiux.example.ldap.model.Persona;
import com.apiux.example.ldap.repositorio.PersonRepo;

@Repository
public class PersonRepoImpl implements PersonRepo {

	public static final String BASE_DN = "ou=system";

	@Autowired
	LdapTemplate ldapTemplate;
	
	protected LdapName buildDn(Persona p) {
		return LdapNameBuilder.newInstance()
                .add("ou","users")
                .add("uid", p.getIdUsuario())
                .build();
		}
	
	@Override
	public void helloWorld() {
		System.out.println("Hello World");
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Override
	public List<String> obtenerNombres() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
				return (String) attrs.get("cn").get().toString();
			}
		});
	}
	
	public List<String> obtenerCorreos() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new AttributesMapper<String>() {
			public String mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
				return (String) attrs.get("mail").get().toString();
				
			}
		});
	}

	private class PersonAttributesMapper implements AttributesMapper<Persona> {
		public Persona mapFromAttributes(Attributes attrs) throws NamingException, javax.naming.NamingException {
			Persona p = new Persona();
			p.setNombre((String) attrs.get("cn").get());
			p.setIdUsuario((String) attrs.get("uid").get());
			p.setEmail((String) attrs.get("mail").get());
			return p;
		}
	}

	@Override
	public List<Persona> obtenerPersonas() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper());
	}
	
	@Override
	public void actualizarContrasena(Persona p) {
		Attribute attr = new BasicAttribute("userPassword", p.getPassword());
		ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[] {item});
	}
	
	
	
}
