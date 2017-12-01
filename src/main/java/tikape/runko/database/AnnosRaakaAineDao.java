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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer annos_id = rs.getInt("annos_id");
        Integer raakaAine_id = rs.getInt("ra_id");
        Integer jarjetysNumero = rs.getInt("jarjestysnro");
        String maara = rs.getString("maara");
        String ohje = rs.getString("ohje");

        AnnosRaakaAine a = new AnnosRaakaAine(annos_id, raakaAine_id, jarjetysNumero, maara, ohje);

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
        List<AnnosRaakaAine> ara = new ArrayList<>();
        while (rs.next()) {
            Integer annos_id = rs.getInt("annos_id");
            Integer raakaAine_id = rs.getInt("ra_id");
            Integer jarjetysNumero = rs.getInt("jarjestysnro");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");

            AnnosRaakaAine a = new AnnosRaakaAine(annos_id, raakaAine_id, jarjetysNumero, maara, ohje);
            ara.add(a);
        }

        rs.close();
        stmt.close();
        connection.close();

        return ara;
    }

    public void delete(Integer key) throws SQLException {
        for (AnnosRaakaAine a : findAll()) {
            if (a.getAnnos_id().equals(key)) {
                try (Connection conn = database.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
                    stmt.setInt(1, key);
                    stmt.executeUpdate();
                }
            }
        }
    }

    public void deleteRaakaAine(Integer key) throws SQLException {
        for (AnnosRaakaAine a : findAll()) {
            if (a.getRaakaAineId().equals(key)) {
                try (Connection conn = database.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM AnnosRaakaAine WHERE ra_id = ?");
                    stmt.setInt(1, key);
                    stmt.executeUpdate();
                }
            }
        }
    }

    @Override
    public boolean saveOrUpdate(AnnosRaakaAine object) throws SQLException {

        for (AnnosRaakaAine a : findAll()) {
            if (a.getAnnos_id().equals(object.getAnnos_id()) && a.getRaakaAineId().equals(object.getRaakaAineId())) {
            return false;
            }
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO AnnosRaakaAine (annos_id, ra_id, jarjestysnro, maara, ohje ) VALUES (?, ?, ?, ?, ?)");

        stmt.setInt(1, object.getAnnos_id());
        stmt.setInt(2, object.getRaakaAineId());
        stmt.setInt(3, object.getJarjestysNumero());
        stmt.setString(4, object.getMaara());
        stmt.setString(5, object.getOhje());

        stmt.executeUpdate();

        return true;

    }

    public boolean updateOrderNo(AnnosRaakaAine object) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE AnnosRaakaAine SET jarjestysnro = ? WHERE annos_id = ? AND ra_id = ?");

        stmt.setInt(1, object.getJarjestysNumero() + 1);
        stmt.setInt(2, object.getAnnos_id());
        stmt.setInt(3, object.getRaakaAineId());
        stmt.executeUpdate();
        
        return true;
    }

}
