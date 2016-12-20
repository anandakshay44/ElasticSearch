package data;

public class Bug {
   private String bug_id;
   private String severity;
   private String priority;
   private String status;
   private String assignee;
   private String reporter;
   private String category;
   private String component;
   private String summary;

   public String getBug_id() {
      return bug_id;
   }

   public void setBug_id(String bug_id) {
      this.bug_id = bug_id;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

   public String getPriority() {
      return priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getAssignee() {
      return assignee;
   }

   public void setAssignee(String assignee) {
      this.assignee = assignee;
   }

   public String getReporter() {
      return reporter;
   }

   public void setReporter(String reporter) {
      this.reporter = reporter;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getComponent() {
      return component;
   }

   public void setComponent(String component) {
      this.component = component;
   }

   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   @Override
   public String toString() {
      return this.getBug_id() + " is reported by: " + this.getReporter()
            + " with severity of: " + this.getSeverity() + "and abstract/summary of: "
            + this.getSummary();
   }

}
