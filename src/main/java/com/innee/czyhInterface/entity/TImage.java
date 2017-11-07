package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "t_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TImage extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String fileName;
	private String storeFileName;
	private String storefileExt;
	private String relativePath;
	private Long fileSize;
	private Integer imageWidth;
	private Integer imageHeight;
	private Integer isThumbnail;
	private String thumbnailFileName;
	private Integer thumbnailWidth;
	private Integer thumbnailHeight;
	private Date uploadTime;
	private Long uploaderId;
	private Integer status;
	private String entityId;
	private Integer entityType;
	private Integer flag;

	// Constructors

	/** default constructor */
	public TImage() {
	}

	// Property accessors
	@Column(name = "fileName")
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "storeFileName")
	public String getStoreFileName() {
		return this.storeFileName;
	}

	public void setStoreFileName(String storeFileName) {
		this.storeFileName = storeFileName;
	}

	@Column(name = "storefileExt", length = 32)
	public String getStorefileExt() {
		return this.storefileExt;
	}

	public void setStorefileExt(String storefileExt) {
		this.storefileExt = storefileExt;
	}

	@Column(name = "relativePath", length = 2048)
	public String getRelativePath() {
		return this.relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	@Column(name = "fileSize")
	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "imageWidth")
	public Integer getImageWidth() {
		return this.imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Column(name = "imageHeight")
	public Integer getImageHeight() {
		return this.imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Column(name = "isThumbnail")
	public Integer getIsThumbnail() {
		return this.isThumbnail;
	}

	public void setIsThumbnail(Integer isThumbnail) {
		this.isThumbnail = isThumbnail;
	}

	@Column(name = "thumbnailFileName")
	public String getThumbnailFileName() {
		return this.thumbnailFileName;
	}

	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
	}

	@Column(name = "thumbnailWidth")
	public Integer getThumbnailWidth() {
		return this.thumbnailWidth;
	}

	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	@Column(name = "thumbnailHeight")
	public Integer getThumbnailHeight() {
		return this.thumbnailHeight;
	}

	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	@Column(name = "uploadTime", length = 19)
	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Column(name = "uploaderId")
	public Long getUploaderId() {
		return this.uploaderId;
	}

	public void setUploaderId(Long uploaderId) {
		this.uploaderId = uploaderId;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "entityId", length = 36)
	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@Column(name = "entityType")
	public Integer getEntityType() {
		return this.entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	@Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}