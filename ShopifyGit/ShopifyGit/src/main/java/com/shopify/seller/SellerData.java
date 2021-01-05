package com.shopify.seller;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;


@Data
public class SellerData {
	private String email;
	private String sellerId;
	private String emailVerified;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String company;
	private String companyEname;
	private String companyNum;
	private String rankId;
	private String useYn;
	private String regDate;
	private String findAuth;
	private String passwd;
	private String passwdConfirm;
	public PasswordEncoder passwordEncoder;
	private String privatechk;
	
	private int rankRate;

//	@Setter(AccessLevel.PROTECTED)
//	private String passwd;
//	
//	@Setter(AccessLevel.PROTECTED)
//	private String passwd_confirm;
//	
//    public void setPasswd(String passwd) {
//    	this.passwd = new BCryptPasswordEncoder().encode(passwd);
//    }
//    
//    public void setPasswd_confirm(String passwd_confirm) {
//    	this.passwd_confirm = new BCryptPasswordEncoder().encode(passwd_confirm);
//   }
}