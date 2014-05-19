package net.ascho.pokretaci.beans;

public class Comment {
	
	public String id;
	public Activist commenter;
	/**
	 * Sadrzaj komentara
	 */
	public String content;
	
	/**
	 * Obican datum
	 */
	public String created_at;
	/**
	 * Parsiran datum u obliku : pre 4 min ....
	 */
	public String parsed_date;
	/**
	 * Link ispod komentara ako ga ima
	 */
	public String link;
	/**
	 * Slika uz komentar ako je ima
	 */
	public String image;
	/**
	 * Mesto sastanaka ako ga ima
	 */
	public String meeting_location;
	/**
	 * Vreme sastanka ako ga ima
	 */
	public String meeting_date;
	
	public boolean likes, flags;
	
	public boolean userLikes() {
		return likes;
	}
	
	public boolean userFlagged() {
		return flags;
	}
	/**
	 * Interakcija sa komentarima, NEW_COMMENT, LIKE_COMMENT, FLAG_COMMENT
	 * @author Bojan
	 *
	 */
	public static final class COMMENTS_INTERACTION_TYPE {
		public static final int NEW_COMMENT = 1;
		public static final int LIKE_COMMENT = 2;
		public static final int FLAG_COMMENT = 3;
	}
	
}
