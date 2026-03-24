package com.yash.security;

public class HashPassword {
	public static String hashBCryptPassword(final String password, int cost) {
		return at.favre.lib.crypto.bcrypt.BCrypt.withDefaults().hashToString(cost, password.toCharArray());
	}

	public static boolean checkBCryptPassword(final String password, final String hash) {
		final at.favre.lib.crypto.bcrypt.BCrypt.Result result = at.favre.lib.crypto.bcrypt.BCrypt.verifyer().verify(password.toCharArray(), hash);
		return result.verified;
	}

	public static String hashJBCryptPassword(final String password, final int cost) {
		final String salt = org.mindrot.jbcrypt.BCrypt.gensalt(cost);
		return org.mindrot.jbcrypt.BCrypt.hashpw(password, salt);
	}

	public static boolean checkJBCryptPassword(final String password, final int cost, final String hash) {
		final String salt = org.mindrot.jbcrypt.BCrypt.gensalt(cost);
		return org.mindrot.jbcrypt.BCrypt.checkpw(salt, hash);
	}
}
