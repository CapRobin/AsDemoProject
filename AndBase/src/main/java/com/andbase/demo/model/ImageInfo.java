package com.andbase.demo.model;

public class ImageInfo {

	private int id;
	private String path;
	private String thumbnailsPath;

	public ImageInfo() {
	}

	public ImageInfo(String path) {
		this.path = path;
	}

	public ImageInfo(int id, String path, String thumbnailsPath) {
		this.id = id;
		this.path = path;
		this.thumbnailsPath = thumbnailsPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getThumbnailsPath() {
		return thumbnailsPath;
	}

	public void setThumbnailsPath(String thumbnailsPath) {
		this.thumbnailsPath = thumbnailsPath;
	}
}
