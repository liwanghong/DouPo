
package com.huayi.doupo.base.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.huayi.doupo.base.dal.base.DALFather;
import com.huayi.doupo.base.model.InstPlayerTrain;
import com.huayi.doupo.base.model.player.PlayerMemObj;
import com.huayi.doupo.base.util.base.StringUtil;

public class InstPlayerTrainDAL extends DALFather {
    
    @SuppressWarnings("rawtypes")
    public class ItemMapper implements RowMapper {
        
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			InstPlayerTrain instPlayerTrain = new InstPlayerTrain();
            instPlayerTrain.setId(rs.getInt("id"), 0);
            instPlayerTrain.setInstPlayerId(rs.getInt("instPlayerId"), 0);
            instPlayerTrain.setInstPlayerCardId(rs.getInt("instPlayerCardId"), 0);
            instPlayerTrain.setFightPropId(rs.getInt("fightPropId"), 0);
            instPlayerTrain.setFightPropValue(rs.getInt("fightPropValue"), 0);
            instPlayerTrain.setVersion(rs.getInt("version"), 0);
            instPlayerTrain.setInsertTime(rs.getString("insertTime"), 0);
            instPlayerTrain.setUpdateTime(rs.getString("updateTime"), 0);
			return instPlayerTrain;
		}
	}
    
	public InstPlayerTrain add(final InstPlayerTrain model, int instPlayerId) throws Exception {
		
		if (model.getId() != 0) {
			return model;
		}
		
        try { 
            final String  updateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
            StringBuilder strSql = new StringBuilder();
	        strSql.append(" insert into Inst_Player_Train (");
	        strSql.append("instPlayerId,instPlayerCardId,fightPropId,fightPropValue,version,insertTime,updateTime");
	        strSql.append(" )");
	        strSql.append(" values (?,?,?,?,?,?,?) ");
            
            final String sql = strSql.toString();
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            this.getJdbcTemplate().update(new PreparedStatementCreator(){
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, model.getInstPlayerId());
                    ps.setInt(2, model.getInstPlayerCardId());
                    ps.setInt(3, model.getFightPropId());
                    ps.setInt(4, model.getFightPropValue());
                    ps.setInt(5, 0);
                    ps.setString(6, updateTime);
                    ps.setString(7, updateTime);
                    return ps;
                } 
            },keyHolder);
            
            model.setId(keyHolder.getKey().intValue());
            model.setVersion(0);
            model.setInsertTime(updateTime, 0);
            model.setUpdateTime(updateTime, 0);
            PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		    if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
                 playerMemObj.instPlayerTrainMap.put(model.getId(), model);
		    }
          
		} catch (Exception e) {
			throw e;
		}
		return model;
	}
    
    public void batchAdd (final List<InstPlayerTrain> list) {

        final String updateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        StringBuilder sql = new StringBuilder();
	    sql.append(" insert into Inst_Player_Train (");
	    sql.append("instPlayerId,instPlayerCardId,fightPropId,fightPropValue,version,insertTime,updateTime");
	    sql.append(" )");
	    sql.append(" values (?,?,?,?,?,?,?) ");
            
	    BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter (){
	          public void setValues(PreparedStatement ps, int i) throws SQLException{
	        	   InstPlayerTrain model = (InstPlayerTrain)list.get(i);
                   ps.setInt(1, model.getInstPlayerId());
                   ps.setInt(2, model.getInstPlayerCardId());
                   ps.setInt(3, model.getFightPropId());
                   ps.setInt(4, model.getFightPropValue());
                    ps.setInt(5, 0);
                    ps.setString(6, updateTime);
                    ps.setString(7, updateTime);
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
                playerMemObj.instPlayerTrainMap.remove(id);
			}
			String sql = "delete from Inst_Player_Train  where id = ?";
            Object[] params = new Object[]{id};
			return this.getJdbcTemplate().update(sql, params);
		} catch (Exception e) {
			throw e;
		}
	}
    
	public int deleteByWhere(String strWhere) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("delete from Inst_Player_Train where 1=1 ");

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
    
	public InstPlayerTrain update(InstPlayerTrain model, int instPlayerId) throws Exception {
		try {
            Object[] params = null;
            int version = model.getVersion() + 1;
            final String  updateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
			StringBuffer sql = new StringBuffer("update Inst_Player_Train set ");
            
            sql.append("instPlayerId=?,instPlayerCardId=?,fightPropId=?,fightPropValue=?,version=?,insertTime=?,updateTime=? ");
            
            sql.append("where id=? and version=?");
            params = new Object[] { model.getInstPlayerId(),model.getInstPlayerCardId(),model.getFightPropId(),model.getFightPropValue(),version,model.getInsertTime(),updateTime, model.getId(), model.getVersion() };

			int count = this.getJdbcTemplate().update(sql.toString(), params);
			if (count == 0) {
				throw new Exception("Concurrent Exception");
			}else{
				model.setVersion(version, 0);
                model.setUpdateTime(updateTime, 0);
                PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
				if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
	        		playerMemObj.instPlayerTrainMap.put(model.getId(), model);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return model;
	}
    
    @SuppressWarnings("unchecked")
	public InstPlayerTrain getModel(int id, int instPlayerId) {
		try {
            PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
        		InstPlayerTrain model = playerMemObj.instPlayerTrainMap.get(id);
        		if (model == null) {
        			String sql = "select * from Inst_Player_Train where id=?";
				    Object[] params = new Object[]{id};
        			playerMemObj.instPlayerTrainMap.put(id, (InstPlayerTrain) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper()));
			    } else {
                    int cacheVersion = model.getVersion();
                    List<Map<String, Object>> list = sqlHelper("select version from Inst_Player_Train where id = " + id);
                    int dbVersion = (int)list.get(0).get("version");
                    if (cacheVersion != dbVersion) {
                        String sql = "select * from Inst_Player_Train where id=?";
                        Object[] params = new Object[]{id};
                        playerMemObj.instPlayerTrainMap.put(id, (InstPlayerTrain) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper()));
                    }
			    }
                
                model = playerMemObj.instPlayerTrainMap.get(id);
                model.result = "";
        		return model;
			} else {
			    String sql = "select * from Inst_Player_Train where id=?";
			    Object[] params = new Object[]{id};
			    InstPlayerTrain model = ( InstPlayerTrain) this.getJdbcTemplate().queryForObject(sql, params, new ItemMapper());
			    model.result = "";
                return model;
			}
		} catch (DataAccessException e) {
			return null;
		}
	}
    
    @SuppressWarnings("unchecked")	
	public List<InstPlayerTrain> getList(String strWhere, int instPlayerId) {
		StringBuffer sql = null;
        PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
    		sql = new StringBuffer("select id, version from Inst_Player_Train ");
		}else {
			sql = new StringBuffer("select * from Inst_Player_Train ");
		}
        if (StringUtil.isNotEmpty(strWhere)) {
			sql.append(" where " + strWhere);
		}
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
    		return listCacheCommonHandler(sql.toString(), instPlayerId);
    	} else {
		    List<InstPlayerTrain> instPlayerTrainList = (List<InstPlayerTrain>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
		    return instPlayerTrainList;
		}
	}
    
	public List<Long> getListIdByWhere(String strWhere) throws Exception {
		try {
			List<Long> listLong = new ArrayList<Long>();
			StringBuffer sql = new StringBuffer("select id from Inst_Player_Train ");
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
	public List<InstPlayerTrain> getListPagination(int index, int size, String strWhere, int instPlayerId) throws Exception {
		try {
			StringBuffer sql = null;
			PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
            if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
                sql = new StringBuffer("select id, version from Inst_Player_Train ");
            }else {
                sql = new StringBuffer("select * from Inst_Player_Train ");
            }
            if(index <= 0 || size <= 0){
				throw new Exception("index or size must bigger than zero");
			}else{
				index = (index - 1) * size;
			}
			if (StringUtil.isNotEmpty(strWhere)) {
			sql.append(" where " + strWhere);
		    }
			sql.append(" limit " + index + "," + size + "");
			if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
				return listCacheCommonHandler(sql.toString(), instPlayerId);
	    	} else {
			    return (List<InstPlayerTrain>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
			}
		} catch (Exception e) {
			throw e;
		}
	}
    
	public boolean isExist(long id, String strWhere) throws Exception {
		try {
			StringBuffer sql = new StringBuffer("select count(*) from Inst_Player_Train where id =?");
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
			StringBuffer sql = new StringBuffer("select count(*) from Inst_Player_Train");
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
			StringBuffer sql = new StringBuffer("select count(*) as cnt from Inst_Player_Train ");
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
	public List<InstPlayerTrain> getListFromMoreTale(String afterSql, int instPlayerId) {
		StringBuffer sql = null;
        PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
        if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
            sql = new StringBuffer("select a.id, a.version from Inst_Player_Train a ");
        }else {
            sql = new StringBuffer("select a.* from Inst_Player_Train a ");
        }
		if (StringUtil.isNotEmpty(afterSql)) {
		    sql.append(afterSql);
	    }
		if (instPlayerId != 0 && isUseCach() && playerMemObj != null) {
			return listCacheCommonHandler(sql.toString(), instPlayerId);
		} else {
		    return (List<InstPlayerTrain>) this.getJdbcTemplate().query(sql.toString(), new ItemMapper());
		}
	}
    
	public List<Long> getListIdFromMoreTable(String afterSql) throws Exception {
		try {
			List<Long> listLong = new ArrayList<Long>();
			StringBuffer sql = new StringBuffer("select a.id from Inst_Player_Train a ");
			if (StringUtil.isNotEmpty(afterSql)) {
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
		try {
            StringBuffer sql = new StringBuffer("select "+statType+"("+conParam+") from  Inst_Player_Train");
			if (strWhere != null && !strWhere.equals("")) {
				sql.append(" where " + strWhere);
			}
			return this.getJdbcTemplate().queryForObject(sql.toString(), Integer.class);
		} catch (Exception e) {
			throw e;
		}
	}

	private List<InstPlayerTrain> listCacheCommonHandler(String sql, int instPlayerId){
		List<InstPlayerTrain> modelList = new ArrayList<InstPlayerTrain>();
		PlayerMemObj playerMemObj = getPlayerMemObjByPlayerId(instPlayerId);
		SqlRowSet rsSet = this.getJdbcTemplate().queryForRowSet(sql.toString());
		while (rsSet.next()) {
			int id = rsSet.getInt("id");
			int dbVersion = rsSet.getInt("version");
			InstPlayerTrain model = playerMemObj.instPlayerTrainMap.get(id);
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


