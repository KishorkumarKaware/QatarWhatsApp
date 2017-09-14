package com.vyom.whatsapp.ImageDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.vyom.whatsapp.ImageDB.entity.AppUser;

public class App {
	static Session session =null;
	public static void getImageDB(String from) {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
		
	      try{
	         //tx = session.beginTransaction();
	         String sql = "select * from appuser where login='"+from+"';";
	         SQLQuery query = session.createSQLQuery(sql);
	         query.addEntity(AppUser.class);
	         List users = query.list();

	         for (Iterator iterator = 
	        		 users.iterator(); iterator.hasNext();){
	        	 AppUser user = (AppUser) iterator.next(); 
	            System.out.print("ID: " + user.getId()); 
	            AppUser appuserImage=(AppUser)session.get(AppUser.class,user.getId());
	    		
	    		byte[] bImage = appuserImage.getImage();
	    		try {
	    			FileOutputStream fileOutputStream = new FileOutputStream("D:\\HDFCWhatsapp\\Images\\DBImage123.png");
	    			fileOutputStream.write(bImage);
	    			fileOutputStream.close();
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	         }

	      }catch (HibernateException e) {
	     
	         e.printStackTrace(); 
	      }
	      session.getTransaction().commit();
			session.close();
	}
	/*
	 public static void main  (String[] args) {
		 @SuppressWarnings("deprecation")
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(saveImageDB("firstuser","C:\\Users\\admin\\Pictures\\Fire_policy.png"));
			session.getTransaction().commit();
			session.close();
			
			session = sessionFactory.openSession();
			session.beginTransaction();
			getImageDB("firstuser");
			session.getTransaction().commit();
			session.close();
			
	  	}
*/
	public static AppUser saveImageDB(String from, String filepath)
	{
		
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
		
		// Save Images in Postgresql Database
		File file = new File(filepath);
		byte[] bfile = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bfile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		AppUser user = new AppUser(from);
		user.setLogin(from);
		user.setImage(bfile);
		session.getTransaction().commit();
		session.close();
		return user;
		
		
	}

}
