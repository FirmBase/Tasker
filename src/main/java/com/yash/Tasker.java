package com.yash;

public class Tasker {
	public static void main(final String[] args) {
		bCryptHash();
		bCryptHashWithSalt();
	}

	public static void bCryptHash() {
		final String password = "asdasd";
		final String bcryptHashString = at.favre.lib.crypto.bcrypt.BCrypt.withDefaults().hashToString(4, password.toCharArray());
		System.out.println("Hashed password: " + bcryptHashString);
		final at.favre.lib.crypto.bcrypt.BCrypt.Result result = at.favre.lib.crypto.bcrypt.BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
		System.out.println("Password match status: " + result.verified);
	}

	public static void bCryptHashWithSalt() {
		final String password = "asdasd";
		final String salt = org.mindrot.jbcrypt.BCrypt.gensalt(12);
		final String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, salt);
		System.out.println("Hashed password: " + hashed);
		System.out.println("Password match status: " + org.mindrot.jbcrypt.BCrypt.checkpw(password, hashed));
	}
}
