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
import tikape.runko.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {

    private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Annos a = new Annos(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Annos> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet rs = stmt.executeQuery();
        List<Annos> opiskelijat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            opiskelijat.add(new Annos(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return opiskelijat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        for (Annos a : findAll()) {
            if (a.getId().equals(key)) {
                try (Connection conn = database.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM Annos WHERE id = ?");
                    stmt.setInt(1, key);
                    stmt.executeUpdate();
                }
            }
        }
    }
    @Override
    public boolean saveOrUpdate(Annos object) throws SQLException {
  
        List<String> reseptit = new ArrayList<>();
        List<Integer> idt = new ArrayList<>();
        int i = 1;

        for (Annos a : findAll()) {
            reseptit.add(a.getNimi());
        }
        if (reseptit.contains(object.getNimi())) {
            reseptit.removeAll(reseptit);
            return false;

        } else {

            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Annos (id, nimi) VALUES (?,?)");

            for (Annos a : findAll()) {
                idt.add(a.getId());
            }
            //tässä varmistetaan ettei id voi olla koskaan sama
            if (!idt.isEmpty()) {
                i = idt.get(idt.size() - 1);
                stmt.setInt(1, i + 1);
                stmt.setString(2, object.getNimi());

            } else {
                stmt.setInt(1, i);
                stmt.setString(2, object.getNimi());
            }
            stmt.executeUpdate();
            return true;
        }

    }

}
