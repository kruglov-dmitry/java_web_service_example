package impl;

public enum Purchase_Type {
	FINANCE{
	      public String toString() {
	          return "FINANCE";
	      }
	  }, CASH{
	      public String toString() {
	          return "CASH";
	      }
	  }, OTHER{
	      public String toString() {
	          return "OTHER";
	      }
	  }
}
