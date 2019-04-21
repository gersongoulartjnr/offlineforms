package br.unifesp.maritaca.business.answer.dto;

public class WrapperAnswers {

	private int numRows;	
	private int currentNumRows;	
	private int currentPage;
	private String orderBy;
	private String orderType;
	private int numPages;	
	private int total;	
	private AnswerListerDTO wrapper;

	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	public int getCurrentNumRows() {
		return currentNumRows;
	}
	public void setCurrentNumRows(int currentNumRows) {
		this.currentNumRows = currentNumRows;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPages() {
		return numPages;
	}
	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public AnswerListerDTO getWrapper() {
		return wrapper;
	}
	public void setWrapper(AnswerListerDTO wrapper) {
		this.wrapper = wrapper;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}	
}