package com.mililu.moneypower.classobject;

public class Wallet {
	private int id_wallet;
	private String name;
	private String money;
	
	public int getId_wallet() {
		return id_wallet;
	}
	public void setId_wallet(int id_wallet) {
		this.id_wallet = id_wallet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	 public String toString() {
	 // TODO Auto-generated method stub
	 return this.name +" - "+this.money;
	 }
}
