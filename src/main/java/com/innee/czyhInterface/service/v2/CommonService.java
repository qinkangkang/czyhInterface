package com.innee.czyhInterface.service.v2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Exceptions;

import com.google.common.collect.Lists;
import com.innee.czyhInterface.util.CommonPage;

@Component
@Transactional(readOnly = true)
public class CommonService {

	private static Logger logger = LoggerFactory.getLogger(CommonService.class);

	@PersistenceContext
	protected EntityManager em;

	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).getSingleResult();
	}

	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).getSingleResult();
	}

	public List<Map<String, Object>> find(String hql, final Object... values) {
		Query q = createQuery(hql, values);
		q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.getResultList();
	}

	public List<Map<String, Object>> find(String hql, final Map<String, ?> values) {
		Query q = createQuery(hql, values);
		q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.getResultList();
	}

	public void findPage(String hql, CommonPage page, final Object... values) {
		Validate.notBlank(hql, "hql不能为空", ArrayUtils.EMPTY_OBJECT_ARRAY);

		long totalCount = countHqlResult(hql, values);
		page.setTotalCount(totalCount);
		if (totalCount > 0) {
			Query q = createQuery(hql, values);
			q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			q.setFirstResult(page.getOffset());
			q.setMaxResults(page.getPageSize());
			page.setResult(q.getResultList());
		} else {
			page.setResult(Lists.newArrayList());
		}
	}

	public void findPage(String hql, CommonPage page, final Map<String, ?> values) {
		Validate.notBlank(hql, "hql不能为空", ArrayUtils.EMPTY_OBJECT_ARRAY);

		long totalCount = countHqlResult(hql, values);
		page.setTotalCount(totalCount);
		if (totalCount > 0) {
			Query q = createQuery(hql, values);
			q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			q.setFirstResult(page.getOffset());
			q.setMaxResults(page.getPageSize());
			page.setResult(q.getResultList());
		} else {
			page.setResult(Lists.newArrayList());
		}
	}

	public Query createQuery(String hql, final Object... values) {
		Validate.notBlank(hql, "hql不能为空", ArrayUtils.EMPTY_OBJECT_ARRAY);
		Query query = em.createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i + 1, values[i]);
			}
		}
		return query;
	}

	public Query createQuery(String hql, final Map<String, ?> values) {
		Validate.notBlank(hql, "hql不能为空", ArrayUtils.EMPTY_OBJECT_ARRAY);
		Query query = em.createQuery(hql);
		if (values != null) {
			for (Iterator it = values.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				query.setParameter(e.getKey().toString(), e.getValue());
			}
		}
		return query;
	}

	private long countHqlResult(String hql, final Object[] values) {
		String countHql = prepareCountHql(hql);
		try {
			Long count = (Long) createQuery(countHql, values).getSingleResult();
			return count.longValue();
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}

	}

	private long countHqlResult(String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);
		try {
			Long count = (Long) createQuery(countHql, values).getSingleResult();
			return count.longValue();
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}

	}

	private String prepareCountHql(String orgHql) {
		String countHql = "select count (*) " + removeSelect(removeOrders(orgHql));
		return countHql;
	}

	private static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}