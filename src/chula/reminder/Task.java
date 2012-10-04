package chula.reminder;


import java.util.Date;




public class Task  {

		/**
	 * 
	 */

		private String name;
		private String comment;
		private int category;
		private Date date;
		private int id;
		private int latitude;
		private int longtitute;
		
		
		public Task(String n, int ct,String co, Date d,int la,int lo){
			name=n;
			category =ct;
			setComment(co);
			date = d;
			latitude =la;
			longtitute = lo;
		}
		
		public int getLatitude() {
			return latitude;
		}

		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}

		public int getLongtitute() {
			return longtitute;
		}

		public void setLongtitute(int longtitute) {
			this.longtitute = longtitute;
		}

		public Task(int id,String n, int ct,String co, Date d,int la,int lo){
			this.id=id;
			name=n;
			category =ct;
			setComment(co);
			date = d;
			latitude =la;
			longtitute = lo;
		}
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getCategory() {
			return category;
		}
		public void setCategory(int category) {
			this.category = category;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}
}
