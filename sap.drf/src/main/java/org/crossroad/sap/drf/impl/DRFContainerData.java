package org.crossroad.sap.drf.impl;

import java.util.ArrayList;
import java.util.List;

public class DRFContainerData {
	private List<String> matnrList = new ArrayList<String>();
	private List<String> maraList = new ArrayList<String>();
	private List<String> carList = new ArrayList<String>();

	public DRFContainerData() {
	}
	
	public void addArticle(String article)
	{
		this.matnrList.add(article);
	}
	
	public void addCARArticle(String article)
	{
		this.carList.add(article);
	}

	public void addECCArticle(String article)
	{
		this.maraList.add(article);
	}

}
