package utils;

/**
 * Reports for Operators. Contains the following info:
 * A summary of conducted changes
 * Whether an changes have been conducted
 * Whether an unhandled issue arised while trying to transform
 *
 * @author ceichler
 */
public class Report {
	private static final String MSG_FORMAT = "[%s] %s\n";
	private static final String MSG_BEGIN = "BEGIN TREATMENT";
	private static final String MSG_END = "END TREATMENT";

	/**
	 * Did the KB change?
	 */
	private boolean actionConducted = false;

	/**
	 * Did something terrible happen?
	 */
	private boolean unhandledIssue = false;

	/**
	 * Logs
	 */
	private StringBuilder message = new StringBuilder();

	/**
	 * Operator the report is about; helps logging
	 */
	private String operator;


	public Report() {
		this("");
	}

	public Report(String fact) {
		this(fact, MSG_BEGIN);
	}

	public Report(String operator, String message) {
		this.operator = operator;
		this.addMessage(message);
	}


	public String getMessage() {
		return message.toString().trim();
	}

	private void setMessage(String message) {
		this.message.replace(0, this.message.length(), message);
	}

	public void addMessage(String message) {
		this.message.append(String.format(MSG_FORMAT, this.operator, message.trim()));
	}


	public boolean isOk() {
		return !unhandledIssue;
	}

	public void setOk(boolean ok) {
		this.unhandledIssue = !ok;
	}


	public boolean isActionConducted() {
		return actionConducted;
	}




	public void mergeReport(Report report) {
		this.actionConducted = this.actionConducted || report.isActionConducted();
		this.setOk(this.isOk() && report.isOk());
		this.message
				.append('\t')
				.append(report.getMessage().replaceAll("\n", "\n\t"))
				.append('\n');
	}

	/**
	 * An unhandled issue occured
	 *
	 * @param issueMsg information on the issue
	 */
	public void issueOccured(String issueMsg) {
		this.setOk(false);
		this.addMessage("UNHANDLED ISSUE; " + issueMsg);
	}
	

	

	public void end() {
		this.addMessage(MSG_END);
	}



	@Override
	public String toString() {
		return this.getMessage();
	}
}


