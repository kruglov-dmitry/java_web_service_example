package impl;

public enum Contact_Type {
	PHONE{
	      public String toString() {
	          return "PHONE";
	      }
	  }, 
	  EMAIL{
	      public String toString() {
	          return "EMAIL";
	      }
	  }, OTHER{
	      public String toString() {
	          return "OTHER";
	      }
	  }
}
