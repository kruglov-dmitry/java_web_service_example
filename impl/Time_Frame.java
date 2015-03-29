package impl;

public enum Time_Frame {
	WITHIN_48_HOURS{
	      public String toString() {
	          return "WITHIN_48_HOURS";
	      }
	  }, WITHIN_2_WEEKS{
	      public String toString() {
	          return "WITHIN_2_WEEKS";
	      }
	  }, WITHIN_1_MONTH{
	      public String toString() {
	          return "WITHIN_1_MONTH";
	      }
	  }, OTHER{
	      public String toString() {
	          return "OTHER";
	      }
	  }
}
