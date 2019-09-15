package br.edu.utfpr.dv.siacoes.dao;

import br.edu.utfpr.dv.siacoes.log.UpdateEvent;
import br.edu.utfpr.dv.siacoes.model.SigacConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TemplateDAO<T> {

    protected abstract String getStringSQLSave();

    protected abstract String getStringSQLUpdate();

    protected abstract void ormSave(PreparedStatement stmt, T objeto) throws SQLException;

    public final void save(T config, int idUser) {
        boolean insert = (this.findByDepartment(config.getDepartment().getIdDepartment()) == null);
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            conn = ConnectionDAO.getInstance().getConnection();

            if(insert){
                stmt = conn.prepareStatement(getStringSQLSave());
            }else{
                stmt = conn.prepareStatement(getStringSQLUpdate());
            }

            ormSave(stmt, config);

            stmt.executeUpdate();

            new UpdateEvent(conn).registerUpdate(idUser, config);

            return config.getDepartment().getIdDepartment();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            if((stmt != null) && !stmt.isClosed())
                stmt.close();
            if((conn != null) && !conn.isClosed())
                conn.close();
        }
    }

    protected abstract String getStringSQLFindByDepartment();

    protected abstract T loadObject(ResultSet rs);

    public T findByDepartment(int idDepartment) throws SQLException{

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            conn = ConnectionDAO.getInstance().getConnection();
            stmt = conn.prepareStatement(getStringSQLFindByDepartment());

            stmt.setInt(1, idDepartment);

            rs = stmt.executeQuery();

            if(rs.next()){
                return this.loadObject(rs);
            }else{
                return null;
            }
        }finally{
            if((rs != null) && !rs.isClosed())
                rs.close();
            if((stmt != null) && !stmt.isClosed())
                stmt.close();
            if((conn != null) && !conn.isClosed())
                conn.close();
        }
    }


}
