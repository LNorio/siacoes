package br.edu.utfpr.dv.siacoes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.edu.utfpr.dv.siacoes.log.UpdateEvent;
import br.edu.utfpr.dv.siacoes.model.SigacConfig;

public class SigacConfigDAO extends TemplateDAO<SigacConfig>{

	@Override
	protected String getStringSQLFindByDepartment(){
		return "SELECT * FROM sigacconfig WHERE idDepartment = ?"
	}

	@Override
	protected String getStringSQLSave(){
		return "INSERT INTO sigacconfig(minimumScore, maxfilesize, idDepartment) VALUES(?, ?, ?)";
	}

	@Override
	protected String getStringSQLUpdate(){
		return "UPDATE sigacconfig SET minimumScore=?, maxfilesize=? WHERE idDepartment=?";
	}

	@Override
	protected void ormSave(PreparedStatement stmt, SigacConfig config) throws SQLException{
		stmt.setDouble(1, config.getMinimumScore());
		stmt.setInt(2, config.getMaxFileSize());
		stmt.setInt(3, config.getDepartment().getIdDepartment());
	}

	@Override
	protected SigacConfig loadObject(ResultSet rs) throws SQLException{
		SigacConfig config = new SigacConfig();
		
		config.getDepartment().setIdDepartment(rs.getInt("idDepartment"));
		config.setMinimumScore(rs.getDouble("minimumScore"));
		config.setMaxFileSize(rs.getInt("maxfilesize"));
		
		return config;
	}

}
