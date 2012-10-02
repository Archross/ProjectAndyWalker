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
		
		
		public Task(String n, int ct,String co, Date d){
			name=n;
			category =ct;
			setComment(co);
			date = d;
		}
		
		public Task(int id,String n, int ct,String co, Date d){
			this.id=id;
			name=n;
			category =ct;
			setComment(co);
			date = d;
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
