package com.innee.czyhInterface.util.bmap;

import java.io.Serializable;

public class BmapIpLocationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;

	private String message;

	private String address;

	Content content;

	public class Content {

		private String address;

		private AddressDetail address_detail;

		private Point point;

		public class AddressDetail {

			private String city;

			private int city_code;

			private String district;

			private String province;

			private String street;

			private String street_number;

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public int getCity_code() {
				return city_code;
			}

			public void setCity_code(int city_code) {
				this.city_code = city_code;
			}

			public String getDistrict() {
				return district;
			}

			public void setDistrict(String district) {
				this.district = district;
			}

			public String getProvince() {
				return province;
			}

			public void setProvince(String province) {
				this.province = province;
			}

			public String getStreet() {
				return street;
			}

			public void setStreet(String street) {
				this.street = street;
			}

			public String getStreet_number() {
				return street_number;
			}

			public void setStreet_number(String street_number) {
				this.street_number = street_number;
			}

		}

		public class Point {
			private String x;

			private String y;

			public String getX() {
				return x;
			}

			public void setX(String x) {
				this.x = x;
			}

			public String getY() {
				return y;
			}

			public void setY(String y) {
				this.y = y;
			}
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public AddressDetail getAddress_detail() {
			return address_detail;
		}

		public void setAddress_detail(AddressDetail address_detail) {
			this.address_detail = address_detail;
		}

		public Point getPoint() {
			return point;
		}

		public void setPoint(Point point) {
			this.point = point;
		}

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}