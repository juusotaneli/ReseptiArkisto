/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.AnnosRaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;

    public AnnosRaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Integer id = rs.getInt("id");   
        Integer annos_id = rs.getInt("annos_id");
        Integer raakaAine_id = rs.getInt("raakaAine_id");
        Integer jarjetysNumero = rs.getInt("jarjestynro");
        Integer maara = rs.getInt("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine a = new AnnosRaakaAine(id, annos_id, raakaAine_id, jarjetysNumero, maara, ohje);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> opiskelijat = new ArrayList<>();
        while (rs.next()) {
            
            Integer id = rs.getInt("id");
            Integer annos_id = rs.getInt("annos_id");
            Integer raakaAine_id = rs.getInt("RaakaAine_id");
            Integer jarjetysNumero = rs.getInt("jarjestynro");
            Integer maara = rs.getInt("maara");
            String ohje = rs.getString("ohje");

            AnnosRaakaAine a = new AnnosRaakaAine(id, annos_id, raakaAine_id, jarjetysNumero, maara, ohje);
        }

        rs.close();
        stmt.close();
        connection.close();

        return opiskelijat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    @Override
    public boolean saveOrUpdate(AnnosRaakaAine key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
