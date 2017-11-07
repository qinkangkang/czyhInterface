package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_poster")
public class TPoster extends UuidEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private String ftitle;
	private String fsubTitle;
	private Date fstartTime;
	private Date fendTime;
	private String fimage;
	private Integer fimageWidth;
	private Integer fimageHeight;
	private Integer fwaterMark;
	private String fheadImageWh;
	private String fheadImageXy;
	private String fqrcodeWh;
	private String fqrcodeXy;
	private String fdescription;
	private String fremark;
	private Integer fstatus;
	private Long fcreaterId;
	private Date fcreateTime;
	private Date fupdateTime;

	// Constructors

	/** default constructor */
	public TPoster() {
	}

	/** minimal constructor */
	public TPoster(String id) {
		this.id = id;
	}

	@Column(name = "fTitle")
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fSubTitle")
	public String getFsubTitle() {
		return this.fsubTitle;
	}

	public void setFsubTitle(String fsubTitle) {
		this.fsubTitle = fsubTitle;
	}

	@Column(name = "fStartTime", length = 19)
	public Date getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Date fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "fEndTime", length = 19)
	public Date getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Date fendTime) {
		this.fendTime = fendTime;
	}

	@Column(name = "fImage")
	public String getFimage() {
		return this.fimage;
	}

	public void setFimage(String fimage) {
		this.fimage = fimage;
	}

	@Column(name = "fWaterMark")
	public Integer getFwaterMark() {
		return this.fwaterMark;
	}

	public void setFwaterMark(Integer fwaterMark) {
		this.fwaterMark = fwaterMark;
	}

	@Column(name = "fHeadImageWH")
	public String getFheadImageWh() {
		return this.fheadImageWh;
	}

	public void setFheadImageWh(String fheadImageWh) {
		this.fheadImageWh = fheadImageWh;
	}

	@Column(name = "fHeadImageXY")
	public String getFheadImageXy() {
		return this.fheadImageXy;
	}

	public void setFheadImageXy(String fheadImageXy) {
		this.fheadImageXy = fheadImageXy;
	}

	@Column(name = "fQRCodeWH")
	public String getFqrcodeWh() {
		return this.fqrcodeWh;
	}

	public void setFqrcodeWh(String fqrcodeWh) {
		this.fqrcodeWh = fqrcodeWh;
	}

	@Column(name = "fQRCodeXY")
	public String getFqrcodeXy() {
		return this.fqrcodeXy;
	}

	public void setFqrcodeXy(String fqrcodeXy) {
		this.fqrcodeXy = fqrcodeXy;
	}

	@Column(name = "fDescription", length = 2048)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fRemark", length = 2048)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "fStatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fCreaterID")
	public Long getFcreaterId() {
		return this.fcreaterId;
	}

	public void setFcreaterId(Long fcreaterId) {
		this.fcreaterId = fcreaterId;
	}

	@Column(name = "fCreateTime", length = 19)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fUpdateTime", length = 19)
	public Date getFupdateTime() {
		return this.fupdateTime;
	}

	public void setFupdateTime(Date fupdateTime) {
		this.fupdateTime = fupdateTime;
	}

	@Column(name = "fImageWidth")
	public Integer getFimageWidth() {
		return fimageWidth;
	}

	public void setFimageWidth(Integer fimageWidth) {
		this.fimageWidth = fimageWidth;
	}

	@Column(name = "fImageHeight")
	public Integer getFimageHeight() {
		return fimageHeight;
	}

	public void setFimageHeight(Integer fimageHeight) {
		this.fimageHeight = fimageHeight;
	}
}