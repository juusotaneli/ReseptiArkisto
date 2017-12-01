package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:arkisto.db");

        AnnosDao ad = new AnnosDao(database);
        RaakaAineDao rad = new RaakaAineDao(database);
        AnnosRaakaAineDao arad = new AnnosRaakaAineDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("reseptit", ad.findAll());

            return new ModelAndView(map, "etusivu");

        }, new ThymeleafTemplateEngine());

        post("/reseptit", (req, res) -> {

            String n = req.queryParams("uusi").toLowerCase();
            if (n != null) {
                ad.saveOrUpdate(new Annos(-1, n));
            }
            res.redirect("/reseptit");
            return "";
        });
        get("/reseptit", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Integer> mahdollisetjarjestysnumerot = new ArrayList<>();
            int i = rad.findAll().size();
            int j = 1;

            if (i > 0) {
                while (i >= j) {
                    mahdollisetjarjestysnumerot.add(j);
                    j++;
                }

            } else {
                mahdollisetjarjestysnumerot.add(j);
            }

            map.put("reseptit", ad.findAll());
            map.put("raakaAineet", rad.findAll());
            map.put("jarjestysnrot", mahdollisetjarjestysnumerot);

            return new ModelAndView(map, "reseptit");

        }, new ThymeleafTemplateEngine());

        get("/reseptit/poista/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            ad.delete(id);
            arad.delete(id);

            res.redirect("/reseptit");

            return "";
        });

        get("/:id/ainesosat", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            List<Integer> idt = new ArrayList<>();
            List<RaakaAine> ra = new ArrayList<>();
            List<String> maarat = new ArrayList<>();
            List<String> ohjeet = new ArrayList<>();
            HashMap map = new HashMap<>();
            //alla sortataan kaikki annosraakaaineet ja poimitaan oikeat annos.id:n perusteella.
            //ei luultavasti tehokkain tapa sortata, mutta toimiva kuitenkin.
            for (AnnosRaakaAine ara : arad.findAll().stream().sorted((o1, o2) -> o1.getJarjestysNumero().compareTo(o2.getJarjestysNumero())).collect(Collectors.toList())) {
                if (ara.getAnnos_id() == id) {
                    idt.add(ara.getRaakaAineId());
                    maarat.add(ara.getMaara());
                    ohjeet.add(ara.getOhje());
                }
            }
            for (Integer i : idt) {
                ra.add(rad.findOne(i));
            }
            map.put("nimi", ad.findOne(id).getNimi());
            map.put("ra", ra);
            map.put("maarat", maarat);
            map.put("ohjeet", ohjeet);
            map.put("resepti_id", ad.findOne(id).getId());

            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine());

        get("/ainesosat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", rad.findAll());
            return new ModelAndView(map, "ainesosat");
        }, new ThymeleafTemplateEngine());

        post("/ainesosat", (req, res) -> {
            String nimi = req.queryParams("uusi").toLowerCase();
            for (RaakaAine a : rad.findAll()) {
                if (a.getNimi().equals(nimi)) {
                    res.redirect("/ainesosat");
                    return "";
                }
            }
            rad.saveOrUpdate(new RaakaAine(-1, nimi));
            res.redirect("/ainesosat");
            return "";

        });
        get("/ainesosat/poista/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            rad.delete(id);
            arad.deleteRaakaAine(id);
            res.redirect("/ainesosat");
            return "";
        });
        post("/reseptit/tallenna", (req, res) -> {
            List<Integer> kaytossaolevatjarjestysnumerot = new ArrayList<>();
            int a = Integer.parseInt(req.queryParams("a"));
            int r = Integer.parseInt(req.queryParams("r"));
            int jarnro = Integer.parseInt(req.queryParams("jarjestysnro"));
            String maara = req.queryParams("maara").toLowerCase();
            String ohje = req.queryParams("ohje").toLowerCase();
            
            if(maara.isEmpty()) {
                maara = "ei annettu";
            }
            if (ohje.isEmpty()) {
                ohje = "ei annettu";
            }

            for (AnnosRaakaAine ara : arad.findAll()) {
                if (ara.getAnnos_id() == a && ara.getRaakaAineId() == r) {
                    res.redirect("/reseptit");
                    return "";
                }
            }
            
            for (AnnosRaakaAine ar : arad.findAll()) {
                if (ar.getAnnos_id() == a) {
                kaytossaolevatjarjestysnumerot.add(ar.getJarjestysNumero());
                }
            }
            if (!kaytossaolevatjarjestysnumerot.contains(jarnro)) {
                arad.saveOrUpdate(new AnnosRaakaAine(a, r, jarnro, maara, ohje));
            } else {
                for (AnnosRaakaAine ar : arad.findAll()) {
                    if (ar.getAnnos_id() == a && ar.getJarjestysNumero() >= jarnro) {
                        arad.updateOrderNo(ar);
                        arad.saveOrUpdate(new AnnosRaakaAine(a, r, jarnro, maara, ohje));
                    }
                }
            }
           
            res.redirect("/reseptit");
            return "";
        });
        get("/:aid/poista/:id", (req, res) -> {

            int a = Integer.parseInt(req.params(":aid"));
            int r = Integer.parseInt(req.params(":id"));

            for (AnnosRaakaAine ara : arad.findAll()) {
                if (ara.getAnnos_id() == a && ara.getRaakaAineId() == r) {
                    arad.deleteRaakaAine(r);
                }
            }
            
            res.redirect("/" + a + "/ainesosat");
            return "";
        });

    }
}
