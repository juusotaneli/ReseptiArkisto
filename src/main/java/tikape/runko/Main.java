package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:arkisto.db");

        AnnosDao ad = new AnnosDao(database);
        RaakaAineDao rad = new RaakaAineDao(database);
        AnnosRaakaAineDao arad = new AnnosRaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            List<String> reseptit = new ArrayList<>();

            for (Annos a : ad.findAll()) {
                reseptit.add(a.getNimi());
            }
            System.out.println(reseptit);
            map.put("reseptit", reseptit);
            return new ModelAndView(map, "etusivu");

        }, new ThymeleafTemplateEngine());

        post("/annos", (req, res) -> {

            String nimi = req.queryParams("uusi");

            for (Annos a : ad.findAll()) {
                if (a.getNimi().equals(nimi)) {
                    res.redirect("/annos");
                    return "";
                }
            }
            ad.saveOrUpdate(new Annos(-1, nimi));
            int id = ad.findOne(ad.findAll().size()).getId();
            res.redirect("/annos");
            return "";

        });
        Spark.get("/annos", (req, res) -> {
            List<String> reseptit = new ArrayList<>();
            HashMap map = new HashMap<>();

            for (Annos a : ad.findAll()) {
                reseptit.add(a.getNimi());
            }
            map.put("reseptit", reseptit);
            return new ModelAndView(map, "annos");
            
        }, new ThymeleafTemplateEngine());

        Spark.get("/:nimi/ainesosat", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "raakaAineet");
        }, new ThymeleafTemplateEngine());

    }

}
