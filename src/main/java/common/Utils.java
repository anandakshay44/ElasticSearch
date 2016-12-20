package common;

public class Utils {
   public static String buildString(String... strings) {
      StringBuilder builder = new StringBuilder(50);
      for (String string : strings) {
          builder.append(string);
      }
      return builder.toString();
  }

}
