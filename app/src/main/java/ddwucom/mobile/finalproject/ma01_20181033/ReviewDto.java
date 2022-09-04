package ddwucom.mobile.finalproject.ma01_20181033;

import java.io.Serializable;

public class ReviewDto implements Serializable {

	private long id;
	private String title;
	private String date;
	private String genre;
	private String together;
	private int rating;
	private String memo;
	private String imageLink;
	private String imageFileName;
	private String address;

	public ReviewDto(String title, String date, String genre, String together, int rating, String memo, String imageLink, String imageFileName, String address) {
		this.title = title;
		this.date = date;
		this.genre = genre;
		this.together = together;
		this.rating = rating;
		this.memo = memo;
		this.imageLink = imageLink;
		this.imageFileName = imageFileName;
		this.address = address;
	}

	public ReviewDto() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return id + ". " + memo + " - " + title + " (" + date + ")";
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTogether() {
		return together;
	}

	public void setTogether(String together) {
		this.together = together;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
