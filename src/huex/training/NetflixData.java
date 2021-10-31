package huex.training;

import java.util.List;

public class NetflixData {
	String showId;
	String type;
	String title;
	String director;
	String cast;
	List<String> countries;
	Long dateAdded;
	String releaseYear;
	String rating;
	String duration;
	List<String> listedIn;
	String description;
	
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public Long getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Long dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public List<String> getListedIn() {
		return listedIn;
	}

	public void setListedIn(List<String> listedIn) {
		this.listedIn = listedIn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "NetflixData [showId=" + showId + ", type=" + type + ", title=" + title + ", director=" + director
				+ ", cast=" + cast + ", countries=" + countries + ", dateAdded=" + dateAdded + ", releaseYear="
				+ releaseYear + ", rating=" + rating + ", duration=" + duration + ", listedIn=" + listedIn
				+ ", description=" + description + "]";
	}

	public NetflixData(Object...headers) {
		super();
		this.showId =(String) headers[0];
		this.type = (String)headers[1];
		this.title =(String) headers[2];
		this.director = (String)headers[3];
		this.cast = (String)headers[4];
		this.countries =(List<String>) headers[5];
		this.dateAdded =(long) headers[6];
		this.releaseYear =(String) headers[7];
		this.rating =(String) headers[8];
		this.duration =(String) headers[9];
		this.listedIn = (List<String>)headers[10];
		this.description =(String) headers[11];
	}
	
}
