package com.weixin.bean;

import java.util.List;

public class PicMessage extends BaseMessage{
	private int ArticleCount;
	private List<Pic> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<Pic> getArticles() {
		return Articles;
	}
	public void setArticles(List<Pic> articles) {
		Articles = articles;
	}
	
}
