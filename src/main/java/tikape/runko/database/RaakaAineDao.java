package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine a = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> rat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            rat.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return rat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        for (RaakaAine a : findAll()) {
            if (a.getId().equals(key)) {
                try (Connection conn = database.getConnection()) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "DELETE FROM RaakaAine WHERE id = ?");
                    stmt.setInt(1, key);
                    stmt.executeUpdate();
                }
            }
        }
    }

    @Override
    public boolean saveOrUpdate(RaakaAine object) throws SQLException {
        List<String> aineet = new ArrayList<>();
        List<Integer> idt = new ArrayList<>();
        int i = 1;

        for (RaakaAine a : findAll()) {
            aineet.add(a.getNimi());
        }
        if (aineet.contains(object.getNimi())) {
            aineet.removeAll(aineet);
            return false;

        } else {

            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO RaakaAine (id, nimi) VALUES (?,?)");

            for (RaakaAine a : findAll()) {
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
