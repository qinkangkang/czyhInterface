package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomer;

public interface CustomerDAO extends JpaRepository<TCustomer, String>, JpaSpecificationExecutor<TCustomer> {

	@Query("select count(t.id) from TCustomer t where t.fusername = ?1 and t.fstatus < 999")
	Long checkUsername(String username);

	@Query("select count(t.id) from TCustomer t where t.id != (select s.TCustomer.id from TSponsor s where s.id = ?1) and t.fusername = ?2 and t.fstatus < 999")
	Long checkEditUsername(String id, String username);

	@Query("select t from TCustomer t where t.fusername = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TCustomer getByUsernameAndType(String fusername, Integer ftype);

	@Query("select t from TCustomer t where t.fusername = ?1 and t.ftype = ?2 and t.id != ?3 and t.fstatus < 999")
	TCustomer getByUsernameAndTypeAndNotCustomerId(String fusername, Integer ftype, String customerId);

	@Query("select t from TCustomer t where t.fphone = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TCustomer getByPhoneAndType(String fphone, Integer ftype);

	@Query("select t from TCustomer t where t.fweixinUnionId = ?1 and t.ftype = ?2 and t.fstatus < 999 and t.fphone is not null")
	TCustomer getByFweixinUnionIdAndFtype(String fweixinUnionId, Integer ftype);

	@Query("select t from TCustomer t where t.fticket = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TCustomer getByFticketAndFtype(String fticket, Integer ftype);

	@Modifying
	@Query("update TCustomer t set t.fstatus = ?2 where t.id = ?1")
	void updateStatus(String customerId, Integer status);

	@Modifying
	@Query("update TCustomer t set t.fticket = ?1 where t.id = ?2")
	void saveTciket(String ticket, String customerId);

	@Modifying
	@Query("update TCustomer t set t.fweixinId = ?1 where t.id = ?2")
	void saveWeixinId(String openid, String customerId);

	@Modifying
	@Query("update TCustomer t set t.fpassword = ?1, t.fsalt = ?2 where t.id = ?3")
	void savePasswordAndSalt(String password, String fsalt, String customerId);

	@Modifying
	@Query("update TCustomer t set t.fusername = ?1, t.fphone = ?2 where t.id = ?3")
	void saveUsernameAndPhone(String username, String phone, String customerId);

	@Modifying
	@Query("update TCustomer t set t.fphoto = ?1 where t.id = ?2")
	void savePhoto(String photo, String customerId);

	@Query("select t from TCustomer t where t.id = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TCustomer getByCustomerIdAndType(String customerId, Integer ftype);

	@Query("select t from TCustomer t where t.fweixinId = ?1 and t.fstatus < 999")
	TCustomer findOneByOpenId(String openid);

	@Query("select t.id from TCustomer t where t.ftype = ?1 and t.fstatus = ?2")
	List<String> findByEnableCustomer(Integer type, Integer status);

	@Query("select t from TCustomer t where t.fweixinUnionId = ?1 and t.fweixinId = ?2 and t.ftype = ?3 and t.fstatus < 999")
	TCustomer getByOpenIdAndUnId(String fweixinUnionId, String fweixinId, Integer ftype);

	@Query("select t from TCustomer t where t.fusername = ?1 and t.fstatus < 999 and t.ftype = 1 and fweixinId is not null")
	TCustomer findByUserName(String username);

	@Query("select t from TCustomer t where t.fweixinId = ?1 and t.fstatus < 999 and t.ftype = 1 and t.fusername is not null")
	TCustomer findByOpenid(String openid);

	@Query("select t from TCustomer t where t.fweixinUnionId = ?1 and t.ftype = ?2 and t.id != ?3 and t.fstatus < 999")
	TCustomer getByUnionidAndTypeAndNotCustomerId(String unionid, Integer ftype, String customerId);
	
	@Query("select t from TCustomer t where t.fweixinUnionId = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TCustomer getByFweixinUnionId(String fweixinUnionId, Integer ftype);
}