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
	
}
