package com.huayi.doupo.base.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.io.InputStream;

import java.sql.PreparedStatement;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.huayi.doupo.base.dal.base.DALFather;
import com.huayi.doupo.base.model.player.PlayerMemObj;
import com.huayi.doupo.base.model.DictFightSoulHuntRule;

public class DictFightSoulHuntRuleDAL extends DALFather {
	@SuppressWarnings("rawtypes")
	public class ItemMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			DictFightSoulHuntRule dictFightSoulHuntRule = new DictFightSoulHuntRule();
			dictFightSoulHuntRule.setId(rs.getInt("id"), 0);
			dictFightSoulHuntRule.setUiId(rs.getInt("uiId"), 0);
			dictFightSoulHuntRule.setNeedSilver(rs.getInt("needSilver"), 0);
			dictFightSoulHuntRule.setNeedGold(rs.getInt("needGold"), 0);
			dictFightSoulHuntRule.setActiveHuntRuleIdPer(rs.getInt("activeHuntRuleIdPer"), 0);
			dictFightSoulHuntRule.setActiveHuntRuleId(rs.getInt("activeHuntRuleId"), 0);
			dictFightSoulHuntRule.setFightSoulQualityPers(rs.getString("fightSoulQualityPers"), 0);
			dictFightSoulHuntRule.setDescription(rs.getString("description"), 0);
			dictFightSoulHuntRule.setVersion(rs.getInt("version"), 0);
			return dictFightSoulHuntRule;
		}
	}

	public DictFightSoulHuntRule add(final DictFightSoulHuntRule model, int instPlayerId) throws Exception {
		try {
			StringBuilder strSql = new StringBuilder();
			strSql.append(" insert into Dict_FightSoul_HuntRule (");
			strSql.append("uiId,needSilver,needGold,activeHuntRuleIdPer,activeHuntRuleId,fightSoulQualityPers,description,version");
			strSql.append(" )");
			strSql.append(" values (?,?,?,?,?,?,?,?) ");

			final String sql = strSql.toString();
			KeyHolder keyHolder = new GeneratedKeyHolder();

			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, model.getUiId());
					ps.setInt(2, model.getNeedSilver());
					ps.setInt(3, model.getNeedGold());
					ps.setInt(4, model.getActiveHuntRuleIdPer());
					ps.setInt(5, model.getActiveHuntRuleId());
					ps.setString(6, model.getFightSoulQualityPers());
					ps.setString(7, model.getDescription());
					ps.setInt(8, 0);
					return ps;
				}
			},keyHolder);

			model.setId(keyHolder.getKey().intValue());
			model.setVersion(0);
			PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				playerMemObj.dictFightSoulHuntRuleMap.put(model.getId(), model);
			}
		} catch (Exception e) {
			throw e;
		}
		return model;
	}

	public void batchAdd (final List<DictFightSoulHuntRule> list) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into Dict_FightSoul_HuntRule (");
		sql.append("uiId,needSilver,needGold,activeHuntRuleIdPer,activeHuntRuleId,fightSoulQualityPers,description,version");
		sql.append(" )");
		sql.append(" values (?,?,?,?,?,?,?,?) ");
		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter (){
			public void setValues(PreparedStatement ps, int i) throws SQLException{
				DictFightSoulHuntRule model = (DictFightSoulHuntRule)list.get(i);
					ps.setInt(1, model.getUiId());
					ps.setInt(2, model.getNeedSilver());
					ps.setInt(3, model.getNeedGold());
					ps.setInt(4, model.getActiveHuntRuleIdPer());
					ps.setInt(5, model.getActiveHuntRuleId());
					ps.setString(6, model.getFightSoulQualityPers());
					ps.setString(7, model.getDescription());
					ps.setInt(8, 0);
			}
			public int getBatchSize(){
				return list.size();
			}
		};
		getJdbcTemplate().batchUpdate(sql.toString(), setter);
	}

	public int deleteById(int id, int instPlayerId) throws Exception {
		try {
			PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				playerMemObj.dictFightSoulHuntRuleMap.remove(id);
			}
			String sql = "delete from Dict_FightSoul_HuntRule  where id = ?";
			Object[] params = new Object[]{id};
			return this.getJdbcTemplate().update(sql, params);
		} catch (Exception e) {
			throw e;
		}
	}

	public int deleteByWhere(String strWhere) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("delete from Dict_FightSoul_HuntRule where 1=1 ");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" and " + strWhere);
			}
			return this.getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	public int update(String sql) throws Exception {
		try {
			return this.getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	public DictFightSoulHuntRule update(DictFightSoulHuntRule model, int instPlayerId) throws Exception {
		try {
			Object[] params = null;
			int version = model.getVersion() + 1;
			StringBuffer sql = new StringBuffer("update Dict_FightSoul_HuntRule set ");
			sql.append("uiId=?,needSilver=?,needGold=?,activeHuntRuleIdPer=?,activeHuntRuleId=?,fightSoulQualityPers=?,description=?,version=? ");
			sql.append("where id=? and version=?");
			params = new Object[] { model.getUiId(),model.getNeedSilver(),model.getNeedGold(),model.getActiveHuntRuleIdPer(),model.getActiveHuntRuleId(),model.getFightSoulQualityPers(),model.getDescription(),version , model.getId(), model.getVersion() };
			int count = this.getJdbcTemplate().update(sql.toString(), params);
			if (count == 0) {
				throw new Exception("Concurrent Exception");
			} else {
				model.setVersion(version, 0);
				PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
				if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
					playerMemObj.dictFightSoulHuntRuleMap.put(model.getId(), model);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return model;
	}

	@SuppressWarnings("unchecked")
	public DictFightSoulHuntRule getModel(int id, int instPlayerId) {
		try {
			PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				DictFightSoulHuntRule model = playerMemObj.dictFightSoulHuntRuleMap.get(id);
				if (model == null) {
					String sql = "select * from Dict_FightSoul_HuntRule where id=?";
					Object[] params = new Object[]{id};
					playerMemObj.dictFightSoulHuntRuleMap.put(id, (DictFightSoulHuntRule) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper()));
				} else {
					int cacheVersion = model.getVersion();
					List<Map<String, Object>> list = sqlHelper("select version from Dict_FightSoul_HuntRule where id = " + id);
					 int dbVersion = (int)list.get(0).get("version");
					if (cacheVersion != dbVersion) {
						String sql = "select * from Dict_FightSoul_HuntRule where id=?";
						Object[] params = new Object[]{id};
						playerMemObj.dictFightSoulHuntRuleMap.put(id, (DictFightSoulHuntRule) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper()));
					}
				}
				model = playerMemObj.dictFightSoulHuntRuleMap.get(id);
				model.result = "";
				return model;
			} else {
				String sql = "select * from Dict_FightSoul_HuntRule where id=?";
				Object[] params = new Object[]{id};
				DictFightSoulHuntRule model = ( DictFightSoulHuntRule) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper());
				model.result = "";
				return model;
			}
		} catch (DataAccessException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DictFightSoulHuntRule> getList(String strWhere, int instPlayerId) {
		StringBuffer sql = null;
		PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
			sql = new StringBuffer("select id, version from Dict_FightSoul_HuntRule ");
		}else {
			sql = new StringBuffer("select * from Dict_FightSoul_HuntRule ");
		}
		if (strWhere != null && !strWhere.equals("")) {
			sql.append(" where " + strWhere);
		}
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
			return listCacheCommonHandler(sql.toString(), instPlayerId);
		} else {
			List<DictFightSoulHuntRule> dictFightSoulHuntRuleList = (List<DictFightSoulHuntRule>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
			return dictFightSoulHuntRuleList;
		}
	}

	public List<Long> getListIdByWhere(String strWhere) throws Exception {
		try {
			List<Long> listLong = new ArrayList<Long>();
			StringBuffer sql = new StringBuffer("select id from Dict_FightSoul_HuntRule ");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" where " + strWhere);
			}
			SqlRowSet rsSet = this.getJdbcTemplate().queryForRowSet(sql.toString());
			while (rsSet.next()) {
				listLong.add(rsSet.getLong("id"));
			}
			return listLong;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DictFightSoulHuntRule> getListPagination(int index, int size, String strWhere, int instPlayerId) throws Exception {
		try {
			StringBuffer sql = null;
			PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				sql = new StringBuffer("select id, version from Dict_FightSoul_HuntRule ");
			}else {
				sql = new StringBuffer("select * from Dict_FightSoul_HuntRule ");
			}
			if(index <= 0 || size <= 0){
				throw new Exception("index or size must bigger than zero");
			}else{
				index = (index - 1) * size;
			}
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" where " + strWhere);
			}
			sql.append(" limit " + index + "," + size + "");
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				return listCacheCommonHandler(sql.toString(), instPlayerId);
			} else {
				return (List<DictFightSoulHuntRule>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean isExist(long id, String strWhere) throws Exception {
		try {
			StringBuffer sql = new StringBuffer("select count(*) from Dict_FightSoul_HuntRule where id =?");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" or " + strWhere);
			}
			int count = this.getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
			return count > 0 ? true : false;
		} catch (Exception e) {
			throw e;
		}
	}

	public int getCount(String strWhere) throws Exception {
		try {
			StringBuffer sql = new StringBuffer("select count(*) from Dict_FightSoul_HuntRule");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" where " + strWhere);
			}
			return this.getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Long> getCounts(String strWhere) throws Exception {
		try {
			List<Long> listLong = new ArrayList<Long>();
			StringBuffer sql = new StringBuffer("select count(*) as cnt from Dict_FightSoul_HuntRule ");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(strWhere);
			}
			SqlRowSet rsSet = this.getJdbcTemplate().queryForRowSet(sql.toString());
			while (rsSet.next()) {
				listLong.add(rsSet.getLong("cnt"));
			}
			return listLong;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Map<String,Object>> sqlHelper(String sql) {
		return this.getJdbcTemplate().queryForList(sql);
	}

	@SuppressWarnings("unchecked")
	public List<DictFightSoulHuntRule> getListFromMoreTale(String afterSql, int instPlayerId) {
		StringBuffer sql = null;
		PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
			sql = new StringBuffer("select a.id, a.version from Dict_FightSoul_HuntRule a ");
		}else {
			sql = new StringBuffer("select a.* from Dict_FightSoul_HuntRule a ");
		}
		if (afterSql != null && !afterSql.equals("")) {
			sql.append(afterSql);
		}
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
			return listCacheCommonHandler(sql.toString(), instPlayerId);
		} else {
			return (List<DictFightSoulHuntRule>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
		}
	}

	public List<Long> getListIdFromMoreTable(String afterSql) throws Exception {
		try {
			List<Long> listLong = new ArrayList<Long>();
			StringBuffer sql = new StringBuffer("select a.id from Dict_FightSoul_HuntRule a ");
			if (afterSql != null && !afterSql.equals("")) {
				sql.append(afterSql);
			}
			SqlRowSet rsSet = this.getJdbcTemplate().queryForRowSet(sql.toString());
			while (rsSet.next()) {
				listLong.add(rsSet.getLong("id"));
			}
			return listLong;
		} catch (Exception e) {
			throw e;
		}
	}

	public int getStatResult(String statType, String conParam, String strWhere) throws Exception {
		int result = 0;
		try {
			StringBuffer sql = new StringBuffer("select "+statType+"("+conParam+") from  Dict_FightSoul_HuntRule");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" where " + strWhere);
			}
			return this.getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
		} catch (Exception e) {
			return result;
		}
	}

	private List<DictFightSoulHuntRule> listCacheCommonHandler(String sql, int instPlayerId){
		List<DictFightSoulHuntRule> modelList = new ArrayList<DictFightSoulHuntRule>();
		PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		SqlRowSet rsSet = this.getJdbcTemplate().queryForRowSet(sql.toString());
		while (rsSet.next()) {
			int id = rsSet.getInt("id");
			int dbVersion = rsSet.getInt("version");
			DictFightSoulHuntRule model = playerMemObj.dictFightSoulHuntRuleMap.get(id);
			if (model == null) {
				model = getModel(id, instPlayerId);
				model.result = "";
				modelList.add(model);
			} else {
				int cacheVersion = model.getVersion();
				if (cacheVersion != dbVersion) {
					model = getModel(id, instPlayerId);
				}
				model.result = "";
				modelList.add(model);
			}
		}
		return modelList;
	}

}