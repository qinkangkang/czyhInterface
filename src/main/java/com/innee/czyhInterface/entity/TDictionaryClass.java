package com.innee.czyhInterface.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_dictionary_class")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TDictionaryClass implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields
	private Long id;
	private String name;
	private String code;
	private Integer editable;
	private Set<TDictionary> TDictionaries = new HashSet<TDictionary>(0);

	// Constructors

	/** default constructor */
	public TDictionaryClass() {
	}

	/** minimal constructor */
	public TDictionaryClass(Long id) {
		this.id = id;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "assigned")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "editable")
	public Integer getEditable() {
		return this.editable;
	}

	public void setEditable(Integer editable) {
		this.editable = editable;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TDictionaryClass")
	public Set<TDictionary> getTDictionaries() {
		return this.TDictionaries;
	}

	public void setTDictionaries(Set<TDictionary> TDictionaries) {
		this.TDictionaries = TDictionaries;
	}

}