package com.innee.czyhInterface.util.bmap;

import java.io.Serializable;

public class BmapGpsLocationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;
	
	Result result;
	
	public class Result{

		AddressComponent addressComponent;
		
		public class AddressComponent {
	
			private String city;
	
			private int adcode;
	
			public String getCity() {
				return city;
			}
	
			public void setCity(String city) {
				this.city = city;
			}
	
			public int getAdcode() {
				return adcode;
			}
	
			public void setAdcode(int adcode) {
				this.adcode = adcode;
			}
	
		}
		public AddressComponent getAddressComponent() {
			return addressComponent;
		}
		
		public void setAddressComponent(AddressComponent addressComponent) {
			this.addressComponent = addressComponent;
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}