package com.vyom.whatsapp.ImageDB.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AppUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;;

	private String login;;

	private byte[] image;

	public AppUser() {

	}

	public AppUser(String login) {
		this.login = login;

	}

	public Long getId() {
		return id;
	}

	public byte[] getImage() {
		return image;
	}

	public String getLogin() {
		return login;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	

}
